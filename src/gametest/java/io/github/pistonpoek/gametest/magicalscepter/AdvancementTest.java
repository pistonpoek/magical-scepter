package io.github.pistonpoek.gametest.magicalscepter;

import io.github.pistonpoek.magicalscepter.advancement.criterion.CastSpellCriterion;
import io.github.pistonpoek.magicalscepter.advancement.criterion.InfuseScepterCriterion;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionProgress;
import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ServerboundPlayerLoadedPacket;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.monster.illager.Evoker;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;
import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.BreezeWindCharge;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import java.lang.reflect.Method;

import static io.github.pistonpoek.gametest.magicalscepter.util.ContextUtil.setMagicalScepterInMainHand;

public class AdvancementTest implements CustomTestMethodInvoker {
    @Override
    public void invokeTestMethod(GameTestHelper context, Method method) throws ReflectiveOperationException {
        method.invoke(this, context);
    }

    @GameTest(structure="gametest:template/empty")
    public void existsCastScepter(GameTestHelper context) {
        String advancementPath = "adventure/cast_scepter";
        getEntry(context, advancementPath);
        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void obtainCastScepter(GameTestHelper context) {
        // Get cast scepter advancement entry.
        String advancementPath = "adventure/cast_scepter";
        AdvancementHolder entry = getEntry(context, advancementPath);

        // Get the advancement tracker for a mock player.
        ServerPlayer player = createMockServerPlayer(context, GameType.SURVIVAL);
        PlayerAdvancements tracker = player.getAdvancements();

        context.assertFalse(tracker.getOrStartProgress(entry).isDone(),
                Component.nullToEmpty("Cast scepter advancement is obtained without action."));

        ItemStack stack = setMagicalScepterInMainHand(context, player);
        player.giveExperiencePoints(100);
        stack.use(context.getLevel(), player, InteractionHand.MAIN_HAND);

        context.assertTrue(tracker.getOrStartProgress(entry).isDone(),
                Component.nullToEmpty("Cast scepter advancement is not obtained after using a magical scepter with experience."));

        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void matchesCastSpellCriterionConditions(GameTestHelper context) {
        Criterion<CastSpellCriterion.Conditions> criterion =
                CastSpellCriterion.Conditions.create(Items.DIRT);

        context.assertTrue(criterion.triggerInstance().matches(Items.DIRT.getDefaultInstance()),
                Component.nullToEmpty("Conditions does not match for expected item stack."));
        context.assertFalse(criterion.triggerInstance().matches(Items.WHEAT.getDefaultInstance()),
                Component.nullToEmpty("Conditions incorrectly matches for item stack."));

        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void existsAllScepterInfusions(GameTestHelper context) {
        String advancementPath = "adventure/all_scepter_infusions";
        getEntry(context, advancementPath);
        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void obtainAllScepterInfusions(GameTestHelper context) {
        // Get all scepter infusions advancement entry.
        String advancementPath = "adventure/all_scepter_infusions";
        AdvancementHolder entry = getEntry(context, advancementPath);

        // Get the advancement tracker for a mock player.
        ServerPlayer player = createMockServerPlayer(context, GameType.SURVIVAL);
        PlayerAdvancements tracker = player.getAdvancements();

        // Check no progress has been made yet.
        context.assertFalse(tracker.getOrStartProgress(entry).isDone(),
                Component.nullToEmpty("All scepter infusions advancement is obtained without action."));
        context.assertFalse(tracker.getOrStartProgress(entry).hasProgress(),
                Component.nullToEmpty("All scepter infusions advancement has obtained criteria without action."));

        ServerLevel world = context.getLevel();

        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.connection.handleAcceptPlayerLoad(new ServerboundPlayerLoadedPacket());
        player.getAbilities().invulnerable = false;

        // Infuse a magical scepter to each of the 9 scepters from the criteria.
        // Each consecutive damage is 1 higher to allow it to deal damage on the same tick.
        {
            setMagicalScepterInMainHand(context, player);
            Blaze blaze = new Blaze(EntityType.BLAZE, world);
            SmallFireball fireCharge = new SmallFireball(EntityType.SMALL_FIREBALL, world);
            player.hurtServer(world, world.damageSources().fireball(fireCharge, blaze), 1);

            CriterionProgress criterion = tracker.getOrStartProgress(entry)
                    .getCriterion("magicalscepter:blaze");
            context.assertTrue(criterion != null, Component.nullToEmpty("Blaze criterion is null"));
            assert criterion != null;
            context.assertTrue(criterion.isDone(),
                    Component.nullToEmpty("Blaze criterion of all scepter infusion advancement is not obtained " +
                            "after damage from blaze's fire charge"));
        }
        {
            setMagicalScepterInMainHand(context, player);
            Breeze breeze = new Breeze(EntityType.BREEZE, world);
            BreezeWindCharge windCharge = new BreezeWindCharge(EntityType.BREEZE_WIND_CHARGE, world);
            player.hurtServer(world, world.damageSources().windCharge(windCharge, breeze), 2);

            CriterionProgress criterion = tracker.getOrStartProgress(entry)
                    .getCriterion("magicalscepter:breeze");
            context.assertTrue(criterion != null, Component.nullToEmpty("Breeze criterion is null"));
            assert criterion != null;
            context.assertTrue(criterion.isDone(),
                    Component.nullToEmpty("Breeze criterion of all scepter infusion advancement is not obtained " +
                            "after damage from breeze's wind charge"));
        }
        {
            setMagicalScepterInMainHand(context, player);
            EnderDragon dragon = new EnderDragon(EntityType.ENDER_DRAGON, world);
            AreaEffectCloud effectCloud = new AreaEffectCloud(EntityType.AREA_EFFECT_CLOUD, world);
            player.hurtServer(world, world.damageSources().indirectMagic(effectCloud, dragon), 3);

            CriterionProgress criterion = tracker.getOrStartProgress(entry)
                    .getCriterion("magicalscepter:dragon");
            context.assertTrue(criterion != null, Component.nullToEmpty("Dragon criterion is null"));
            assert criterion != null;
            context.assertTrue(criterion.isDone(),
                    Component.nullToEmpty("Dragon criterion of all scepter infusion advancement is not obtained " +
                            "after damage from an ender dragon"));
        }
        {
            setMagicalScepterInMainHand(context, player);
            Evoker evoker = new Evoker(EntityType.EVOKER, world);
            EvokerFangs fangs = new EvokerFangs(EntityType.EVOKER_FANGS, world);
            player.hurtServer(world, world.damageSources().indirectMagic(fangs, evoker), 4);

            CriterionProgress criterion = tracker.getOrStartProgress(entry)
                    .getCriterion("magicalscepter:evoker");
            context.assertTrue(criterion != null, Component.nullToEmpty("Evoker criterion is null"));
            assert criterion != null;
            context.assertTrue(criterion.isDone(),
                    Component.nullToEmpty("Evoker criterion of all scepter infusion advancement is not obtained " +
                            "after damage from evoker's fangs"));
        }
        {
            setMagicalScepterInMainHand(context, player);
            Ghast ghast = new Ghast(EntityType.GHAST, world);
            LargeFireball fireball = new LargeFireball(EntityType.FIREBALL, world);
            player.hurtServer(world, world.damageSources().fireball(fireball, ghast), 5);

            CriterionProgress criterion = tracker.getOrStartProgress(entry)
                    .getCriterion("magicalscepter:ghast");
            context.assertTrue(criterion != null, Component.nullToEmpty("Ghast criterion is null"));
            assert criterion != null;
            context.assertTrue(criterion.isDone(),
                    Component.nullToEmpty("Ghast criterion of all scepter infusion advancement is not obtained " +
                            "after damage from ghast's fireball"));
        }
        {
            setMagicalScepterInMainHand(context, player);
            ElderGuardian guardian = new ElderGuardian(EntityType.ELDER_GUARDIAN, world);
            player.hurtServer(world, world.damageSources().indirectMagic(guardian, guardian), 6);

            CriterionProgress criterion = tracker.getOrStartProgress(entry)
                    .getCriterion("magicalscepter:guardian");
            context.assertTrue(criterion != null, Component.nullToEmpty("Guardian criterion is null"));
            assert criterion != null;
            context.assertTrue(criterion.isDone(),
                    Component.nullToEmpty("Guardian criterion of all scepter infusion advancement is not obtained " +
                            "after damage from elder guardian's beam"));
        }
        {
            setMagicalScepterInMainHand(context, player);
            Shulker shulker = new Shulker(EntityType.SHULKER, world);
            ShulkerBullet bullet = new ShulkerBullet(EntityType.SHULKER_BULLET, world);
            player.hurtServer(world, world.damageSources().mobProjectile(bullet, shulker), 7);

            CriterionProgress criterion = tracker.getOrStartProgress(entry)
                    .getCriterion("magicalscepter:shulker");
            context.assertTrue(criterion != null, Component.nullToEmpty("Shulker criterion is null"));
            assert criterion != null;
            context.assertTrue(criterion.isDone(),
                    Component.nullToEmpty("Shulker criterion of all scepter infusion advancement is not obtained " +
                            "after damage from shulker's bullet"));
        }
        {
            setMagicalScepterInMainHand(context, player);
            Warden warden = new Warden(EntityType.WARDEN, world);
            player.hurtServer(world, world.damageSources().sonicBoom(warden), 8);

            CriterionProgress criterion = tracker.getOrStartProgress(entry)
                    .getCriterion("magicalscepter:warden");
            context.assertTrue(criterion != null, Component.nullToEmpty("Warden criterion is null"));
            assert criterion != null;
            context.assertTrue(criterion.isDone(),
                    Component.nullToEmpty("Warden criteria of all scepter infusion advancement is not obtained " +
                            "after damage from warden's sonic boom"));
        }
        {
            setMagicalScepterInMainHand(context, player);
            WitherBoss wither = new WitherBoss(EntityType.WITHER, world);
            WitherSkull skull = new WitherSkull(EntityType.WITHER_SKULL, world);
            player.hurtServer(world, world.damageSources().witherSkull(skull, wither), 9);

            CriterionProgress criterion = tracker.getOrStartProgress(entry)
                    .getCriterion("magicalscepter:wither");
            context.assertTrue(criterion != null, Component.nullToEmpty("Wither criterion is null"));
            assert criterion != null;
            context.assertTrue(criterion.isDone(),
                    Component.nullToEmpty("Wither criterion of all scepter infusion advancement is not obtained " +
                            "after damage from wither's skull"));
        }

        context.assertTrue(tracker.getOrStartProgress(entry).isDone(),
                Component.nullToEmpty("All scepter infusions advancement is not obtained after infusing 9 scepters."));

        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void matchesInfuseScepterCriterionConditions(GameTestHelper context) {
        Registry<Scepter> registry = context.getLevel().registryAccess().lookupOrThrow(ModRegistryKeys.SCEPTER);
        Criterion<InfuseScepterCriterion.Conditions> criterion =
                InfuseScepterCriterion.Conditions.create(registry.getOrThrow(Scepters.GUARDIAN_KEY));

        context.assertTrue(criterion.triggerInstance().matches(registry.getOrThrow(Scepters.GUARDIAN_KEY)),
                Component.nullToEmpty("Conditions does not match for expected scepter."));
        context.assertFalse(criterion.triggerInstance().matches(registry.getOrThrow(Scepters.WITHER_KEY)),
                Component.nullToEmpty("Conditions incorrectly matches for scepter."));

        context.succeed();
    }

    private AdvancementHolder getEntry(GameTestHelper context, String path) {
        ServerAdvancementManager loader = context.getLevel().getServer().getAdvancements();
        AdvancementHolder entry = loader.get(ModIdentifier.of(path));

        if (entry == null) {
            throw context.assertionException(Component.nullToEmpty("Could not find advancement at %s".formatted(path)));
        }

        return entry;
    }

    private ServerPlayer createMockServerPlayer(GameTestHelper context, GameType gameMode) {
        Player player = context.makeMockPlayer(gameMode);
        ServerPlayer serverPlayer = new ServerPlayer(context.getLevel().getServer(), context.getLevel(),
                player.getGameProfile(), ClientInformation.createDefault());

        // Set the player to be loaded and have a network handler to mock server expectation.
        serverPlayer.connection = new ServerGamePacketListenerImpl(context.getLevel().getServer(),
                new Connection(PacketFlow.CLIENTBOUND), serverPlayer,
                CommonListenerCookie.createInitial(player.getGameProfile(), false));
        return serverPlayer;
    }
}
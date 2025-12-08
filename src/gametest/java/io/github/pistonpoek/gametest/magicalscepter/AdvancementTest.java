package io.github.pistonpoek.gametest.magicalscepter;

import io.github.pistonpoek.magicalscepter.advancement.criterion.CastSpellCriterion;
import io.github.pistonpoek.magicalscepter.advancement.criterion.InfuseScepterCriterion;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.CriterionProgress;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

import java.lang.reflect.Method;

public class AdvancementTest implements CustomTestMethodInvoker {
    @Override
    public void invokeTestMethod(TestContext context, Method method) throws ReflectiveOperationException {
        method.invoke(this, context);
    }

    @GameTest(structure="gametest:template/empty")
    public void existsCastScepter(TestContext context) {
        String advancementPath = "adventure/cast_scepter";
        getEntry(context, advancementPath);
        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void obtainCastScepter(TestContext context) {
        // Get cast scepter advancement entry.
        String advancementPath = "adventure/cast_scepter";
        AdvancementEntry entry = getEntry(context, advancementPath);

        // Get the advancement tracker for a mock player.
        ServerPlayerEntity player = createMockServerPlayer(context, GameMode.SURVIVAL);
        PlayerAdvancementTracker tracker = player.getAdvancementTracker();

        context.assertFalse(tracker.getProgress(entry).isDone(),
                Text.of("Cast scepter advancement is obtained without action."));

        ItemStack stack = setMagicalScepterInMainHand(context, player);
        player.addExperience(100);
        stack.use(context.getWorld(), player, Hand.MAIN_HAND);

        context.assertTrue(tracker.getProgress(entry).isDone(),
                Text.of("Cast scepter advancement is not obtained after using a magical scepter with experience."));

        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void matchesCastSpellCriterionConditions(TestContext context) {
        AdvancementCriterion<CastSpellCriterion.Conditions> criterion =
                CastSpellCriterion.Conditions.create(Items.DIRT);

        context.assertTrue(criterion.conditions().matches(Items.DIRT.getDefaultStack()),
                Text.of("Conditions does not match for expected item stack."));
        context.assertFalse(criterion.conditions().matches(Items.WHEAT.getDefaultStack()),
                Text.of("Conditions incorrectly matches for item stack."));

        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void existsAllScepterInfusions(TestContext context) {
        String advancementPath = "adventure/all_scepter_infusions";
        getEntry(context, advancementPath);
        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void obtainAllScepterInfusions(TestContext context) {
        // Get all scepter infusions advancement entry.
        String advancementPath = "adventure/all_scepter_infusions";
        AdvancementEntry entry = getEntry(context, advancementPath);

        // Get the advancement tracker for a mock player.
        ServerPlayerEntity player = createMockServerPlayer(context, GameMode.SURVIVAL);
        PlayerAdvancementTracker tracker = player.getAdvancementTracker();

        // Check no progress has been made yet.
        context.assertFalse(tracker.getProgress(entry).isDone(),
                Text.of("All scepter infusions advancement is obtained without action."));
        context.assertFalse(tracker.getProgress(entry).isAnyObtained(),
                Text.of("All scepter infusions advancement has obtained criteria without action."));

        ServerWorld world = context.getWorld();

        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.setLoaded(true);
        player.getAbilities().invulnerable = false;

        // Infuse a magical scepter to each of the 9 scepters from the criteria.
        // Each consecutive damage is 1 higher to allow it to deal damage on the same tick.
        {
            setMagicalScepterInMainHand(context, player);
            BlazeEntity blaze = new BlazeEntity(EntityType.BLAZE, world);
            SmallFireballEntity fireCharge = new SmallFireballEntity(EntityType.SMALL_FIREBALL, world);
            player.damage(world, world.getDamageSources().fireball(fireCharge, blaze), 1);

            CriterionProgress criterion = tracker.getProgress(entry)
                    .getCriterionProgress("magicalscepter:blaze");
            context.assertTrue(criterion != null, Text.of("Blaze criterion is null"));
            assert criterion != null;
            context.assertTrue(criterion.isObtained(),
                    Text.of("Blaze criterion of all scepter infusion advancement is not obtained " +
                            "after damage from blaze's fire charge"));
        }
        {
            setMagicalScepterInMainHand(context, player);
            BreezeEntity breeze = new BreezeEntity(EntityType.BREEZE, world);
            WindChargeEntity windCharge = new WindChargeEntity(EntityType.WIND_CHARGE, world);
            player.damage(world, world.getDamageSources().windCharge(windCharge, breeze), 2);

            CriterionProgress criterion = tracker.getProgress(entry)
                    .getCriterionProgress("magicalscepter:breeze");
            context.assertTrue(criterion != null, Text.of("Breeze criterion is null"));
            assert criterion != null;
            context.assertTrue(criterion.isObtained(),
                    Text.of("Breeze criterion of all scepter infusion advancement is not obtained " +
                            "after damage from breeze's wind charge"));
        }
        {
            setMagicalScepterInMainHand(context, player);
            EnderDragonEntity dragon = new EnderDragonEntity(EntityType.ENDER_DRAGON, world);
            AreaEffectCloudEntity effectCloud = new AreaEffectCloudEntity(EntityType.AREA_EFFECT_CLOUD, world);
            player.damage(world, world.getDamageSources().indirectMagic(effectCloud, dragon), 3);

            CriterionProgress criterion = tracker.getProgress(entry)
                    .getCriterionProgress("magicalscepter:dragon");
            context.assertTrue(criterion != null, Text.of("Dragon criterion is null"));
            assert criterion != null;
            context.assertTrue(criterion.isObtained(),
                    Text.of("Dragon criterion of all scepter infusion advancement is not obtained " +
                            "after damage from an ender dragon"));
        }
        {
            setMagicalScepterInMainHand(context, player);
            EvokerEntity evoker = new EvokerEntity(EntityType.EVOKER, world);
            EvokerFangsEntity fangs = new EvokerFangsEntity(EntityType.EVOKER_FANGS, world);
            player.damage(world, world.getDamageSources().indirectMagic(fangs, evoker), 4);

            CriterionProgress criterion = tracker.getProgress(entry)
                    .getCriterionProgress("magicalscepter:evoker");
            context.assertTrue(criterion != null, Text.of("Evoker criterion is null"));
            assert criterion != null;
            context.assertTrue(criterion.isObtained(),
                    Text.of("Evoker criterion of all scepter infusion advancement is not obtained " +
                            "after damage from evoker's fangs"));
        }
        {
            setMagicalScepterInMainHand(context, player);
            GhastEntity ghast = new GhastEntity(EntityType.GHAST, world);
            FireballEntity fireball = new FireballEntity(EntityType.FIREBALL, world);
            player.damage(world, world.getDamageSources().fireball(fireball, ghast), 5);

            CriterionProgress criterion = tracker.getProgress(entry)
                    .getCriterionProgress("magicalscepter:ghast");
            context.assertTrue(criterion != null, Text.of("Ghast criterion is null"));
            assert criterion != null;
            context.assertTrue(criterion.isObtained(),
                    Text.of("Ghast criterion of all scepter infusion advancement is not obtained " +
                            "after damage from ghast's fireball"));
        }
        {
            setMagicalScepterInMainHand(context, player);
            ElderGuardianEntity guardian = new ElderGuardianEntity(EntityType.ELDER_GUARDIAN, world);
            player.damage(world, world.getDamageSources().indirectMagic(guardian, guardian), 6);

            CriterionProgress criterion = tracker.getProgress(entry)
                    .getCriterionProgress("magicalscepter:guardian");
            context.assertTrue(criterion != null, Text.of("Guardian criterion is null"));
            assert criterion != null;
            context.assertTrue(criterion.isObtained(),
                    Text.of("Guardian criterion of all scepter infusion advancement is not obtained " +
                            "after damage from elder guardian's beam"));
        }
        {
            setMagicalScepterInMainHand(context, player);
            ShulkerEntity shulker = new ShulkerEntity(EntityType.SHULKER, world);
            ShulkerBulletEntity bullet = new ShulkerBulletEntity(EntityType.SHULKER_BULLET, world);
            player.damage(world, world.getDamageSources().mobProjectile(bullet, shulker), 7);

            CriterionProgress criterion = tracker.getProgress(entry)
                    .getCriterionProgress("magicalscepter:shulker");
            context.assertTrue(criterion != null, Text.of("Shulker criterion is null"));
            assert criterion != null;
            context.assertTrue(criterion.isObtained(),
                    Text.of("Shulker criterion of all scepter infusion advancement is not obtained " +
                            "after damage from shulker's bullet"));
        }
        {
            setMagicalScepterInMainHand(context, player);
            WardenEntity warden = new WardenEntity(EntityType.WARDEN, world);
            player.damage(world, world.getDamageSources().sonicBoom(warden), 8);

            CriterionProgress criterion = tracker.getProgress(entry)
                    .getCriterionProgress("magicalscepter:warden");
            context.assertTrue(criterion != null, Text.of("Warden criterion is null"));
            assert criterion != null;
            context.assertTrue(criterion.isObtained(),
                    Text.of("Warden criteria of all scepter infusion advancement is not obtained " +
                            "after damage from warden's sonic boom"));
        }
        {
            setMagicalScepterInMainHand(context, player);
            WitherEntity wither = new WitherEntity(EntityType.WITHER, world);
            WitherSkullEntity skull = new WitherSkullEntity(EntityType.WITHER_SKULL, world);
            player.damage(world, world.getDamageSources().witherSkull(skull, wither), 9);

            CriterionProgress criterion = tracker.getProgress(entry)
                    .getCriterionProgress("magicalscepter:wither");
            context.assertTrue(criterion != null, Text.of("Wither criterion is null"));
            assert criterion != null;
            context.assertTrue(criterion.isObtained(),
                    Text.of("Wither criterion of all scepter infusion advancement is not obtained " +
                            "after damage from wither's skull"));
        }

        context.assertTrue(tracker.getProgress(entry).isDone(),
                Text.of("All scepter infusions advancement is not obtained after infusing 9 scepters."));

        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void matchesInfuseScepterCriterionConditions(TestContext context) {
        Registry<Scepter> registry = context.getWorld().getRegistryManager().getOrThrow(ModRegistryKeys.SCEPTER);
        AdvancementCriterion<InfuseScepterCriterion.Conditions> criterion =
                InfuseScepterCriterion.Conditions.create(registry.getOrThrow(Scepters.GUARDIAN_KEY));

        context.assertTrue(criterion.conditions().matches(registry.getOrThrow(Scepters.GUARDIAN_KEY)),
                Text.of("Conditions does not match for expected scepter."));
        context.assertFalse(criterion.conditions().matches(registry.getOrThrow(Scepters.WITHER_KEY)),
                Text.of("Conditions incorrectly matches for scepter."));

        context.complete();
    }

    private AdvancementEntry getEntry(TestContext context, String path) {
        ServerAdvancementLoader loader = context.getWorld().getServer().getAdvancementLoader();
        AdvancementEntry entry = loader.get(ModIdentifier.of(path));

        if (entry == null) {
            throw context.createError(Text.of("Could not find advancement at %s".formatted(path)));
        }

        return entry;
    }

    private ServerPlayerEntity createMockServerPlayer(TestContext context, GameMode gameMode) {
        PlayerEntity player = context.createMockPlayer(gameMode);
        ServerPlayerEntity serverPlayer = new ServerPlayerEntity(context.getWorld().getServer(), context.getWorld(),
                player.getGameProfile(), SyncedClientOptions.createDefault());

        // Set the player to be loaded and have a network handler to mock server expectation.
        serverPlayer.networkHandler = new ServerPlayNetworkHandler(context.getWorld().getServer(),
                new ClientConnection(NetworkSide.CLIENTBOUND), serverPlayer,
                ConnectedClientData.createDefault(player.getGameProfile(), false));
        return serverPlayer;
    }

    private ItemStack setMagicalScepterInMainHand(TestContext context, PlayerEntity player) {
        ItemStack stack = ScepterHelper.createMagicalScepter(getScepter(context, Scepters.MAGICAL_KEY));
        player.setStackInHand(Hand.MAIN_HAND, stack);
        return stack;
    }

    private RegistryEntry<Scepter> getScepter(TestContext context, RegistryKey<Scepter> key) {
        return context.getWorld().getRegistryManager().getEntryOrThrow(key);
    }
}
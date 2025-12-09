package io.github.pistonpoek.gametest.magicalscepter;

import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

import java.lang.reflect.Method;
import java.util.Optional;

import static io.github.pistonpoek.gametest.magicalscepter.util.ContextUtil.*;

public class InfusionTest implements CustomTestMethodInvoker {
    @Override
    public void invokeTestMethod(TestContext context, Method method) throws ReflectiveOperationException {
        method.invoke(this, context);
    }

    @GameTest(structure="gametest:template/empty")
    public void infuses(TestContext context) {
        ServerPlayerEntity player = createMockServerPlayer(context, GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.setLoaded(true);
        player.getAbilities().invulnerable = false;

        setMagicalScepterInMainHand(context, player);

        BlazeEntity blaze = new BlazeEntity(EntityType.BLAZE, world);
        SmallFireballEntity fireCharge = new SmallFireballEntity(EntityType.SMALL_FIREBALL, world);
        player.damage(world, world.getDamageSources().fireball(fireCharge, blaze), 1);

        expectScepter(context, player.getMainHandStack(), Scepters.BLAZE_KEY);
        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void noInfusionZeroDamage(TestContext context) {
        ServerPlayerEntity player = createMockServerPlayer(context, GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.setLoaded(true);
        player.getAbilities().invulnerable = false;

        setMagicalScepterInMainHand(context, player);

        BlazeEntity blaze = new BlazeEntity(EntityType.BLAZE, world);
        SmallFireballEntity fireCharge = new SmallFireballEntity(EntityType.SMALL_FIREBALL, world);
        player.damage(world, world.getDamageSources().fireball(fireCharge, blaze), 0);

        expectScepter(context, player.getMainHandStack(), Scepters.MAGICAL_KEY);
        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void infusionOffhand(TestContext context) {
        ServerPlayerEntity player = createMockServerPlayer(context, GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.setLoaded(true);
        player.getAbilities().invulnerable = false;

        setMagicalScepterInHand(context, player, Hand.OFF_HAND);

        BlazeEntity blaze = new BlazeEntity(EntityType.BLAZE, world);
        SmallFireballEntity fireCharge = new SmallFireballEntity(EntityType.SMALL_FIREBALL, world);
        player.damage(world, world.getDamageSources().fireball(fireCharge, blaze), 0);

        expectScepter(context, player.getOffHandStack(), Scepters.MAGICAL_KEY);
        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void infusesOnlyMainHand(TestContext context) {
        ServerPlayerEntity player = createMockServerPlayer(context, GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.setLoaded(true);
        player.getAbilities().invulnerable = false;

        setMagicalScepterInMainHand(context, player);
        setMagicalScepterInHand(context, player, Hand.OFF_HAND);

        BlazeEntity blaze = new BlazeEntity(EntityType.BLAZE, world);
        SmallFireballEntity fireCharge = new SmallFireballEntity(EntityType.SMALL_FIREBALL, world);
        player.damage(world, world.getDamageSources().fireball(fireCharge, blaze), 1);

        expectScepter(context, player.getMainHandStack(), Scepters.BLAZE_KEY);
        expectScepter(context, player.getOffHandStack(), Scepters.MAGICAL_KEY);
        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void infusesBothHands(TestContext context) {
        ServerPlayerEntity player = createMockServerPlayer(context, GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.setLoaded(true);
        player.getAbilities().invulnerable = false;

        setMagicalScepterInMainHand(context, player);
        setMagicalScepterInHand(context, player, Hand.OFF_HAND);

        BlazeEntity blaze = new BlazeEntity(EntityType.BLAZE, world);
        SmallFireballEntity fireCharge = new SmallFireballEntity(EntityType.SMALL_FIREBALL, world);
        player.damage(world, world.getDamageSources().fireball(fireCharge, blaze), 1);

        GhastEntity ghast = new GhastEntity(EntityType.GHAST, world);
        FireballEntity fireball = new FireballEntity(EntityType.FIREBALL, world);
        player.damage(world, world.getDamageSources().fireball(fireball, ghast), 2);

        expectScepter(context, player.getMainHandStack(), Scepters.BLAZE_KEY);
        expectScepter(context, player.getOffHandStack(), Scepters.GHAST_KEY);
        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void allInfusions(TestContext context) {
        ServerPlayerEntity player = createMockServerPlayer(context, GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.setLoaded(true);
        player.getAbilities().invulnerable = false;

        {
            setMagicalScepterInMainHand(context, player);
            BlazeEntity blaze = new BlazeEntity(EntityType.BLAZE, world);
            SmallFireballEntity fireCharge = new SmallFireballEntity(EntityType.SMALL_FIREBALL, world);
            player.damage(world, world.getDamageSources().fireball(fireCharge, blaze), 1);

            expectScepter(context, player.getMainHandStack(), Scepters.BLAZE_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            BreezeEntity breeze = new BreezeEntity(EntityType.BREEZE, world);
            WindChargeEntity windCharge = new WindChargeEntity(EntityType.WIND_CHARGE, world);
            player.damage(world, world.getDamageSources().windCharge(windCharge, breeze), 2);

            expectScepter(context, player.getMainHandStack(), Scepters.BREEZE_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            EnderDragonEntity dragon = new EnderDragonEntity(EntityType.ENDER_DRAGON, world);
            AreaEffectCloudEntity effectCloud = new AreaEffectCloudEntity(EntityType.AREA_EFFECT_CLOUD, world);
            player.damage(world, world.getDamageSources().indirectMagic(effectCloud, dragon), 3);

            expectScepter(context, player.getMainHandStack(), Scepters.DRAGON_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            EvokerEntity evoker = new EvokerEntity(EntityType.EVOKER, world);
            EvokerFangsEntity fangs = new EvokerFangsEntity(EntityType.EVOKER_FANGS, world);
            player.damage(world, world.getDamageSources().indirectMagic(fangs, evoker), 4);

            expectScepter(context, player.getMainHandStack(), Scepters.EVOKER_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            GhastEntity ghast = new GhastEntity(EntityType.GHAST, world);
            FireballEntity fireball = new FireballEntity(EntityType.FIREBALL, world);
            player.damage(world, world.getDamageSources().fireball(fireball, ghast), 5);

            expectScepter(context, player.getMainHandStack(), Scepters.GHAST_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            ElderGuardianEntity guardian = new ElderGuardianEntity(EntityType.ELDER_GUARDIAN, world);
            player.damage(world, world.getDamageSources().indirectMagic(guardian, guardian), 6);

            expectScepter(context, player.getMainHandStack(), Scepters.GUARDIAN_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            ShulkerEntity shulker = new ShulkerEntity(EntityType.SHULKER, world);
            ShulkerBulletEntity bullet = new ShulkerBulletEntity(EntityType.SHULKER_BULLET, world);
            player.damage(world, world.getDamageSources().mobProjectile(bullet, shulker), 7);

            expectScepter(context, player.getMainHandStack(), Scepters.SHULKER_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            WardenEntity warden = new WardenEntity(EntityType.WARDEN, world);
            player.damage(world, world.getDamageSources().sonicBoom(warden), 8);

            expectScepter(context, player.getMainHandStack(), Scepters.WARDEN_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            WitherEntity wither = new WitherEntity(EntityType.WITHER, world);
            WitherSkullEntity skull = new WitherSkullEntity(EntityType.WITHER_SKULL, world);
            player.damage(world, world.getDamageSources().witherSkull(skull, wither), 9);

            expectScepter(context, player.getMainHandStack(), Scepters.WITHER_KEY);
        }
        context.complete();
    }

    // TODO Be more expansive in testing edge cases.
    @GameTest(structure="gametest:template/empty")
    public void noInfusionsMobAttack(TestContext context) {
        ServerPlayerEntity player = createMockServerPlayer(context, GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.setLoaded(true);
        player.getAbilities().invulnerable = false;

        {
            setMagicalScepterInMainHand(context, player);
            BlazeEntity blaze = new BlazeEntity(EntityType.BLAZE, world);
            player.damage(world, world.getDamageSources().mobAttack(blaze), 1);

            expectScepter(context, player.getMainHandStack(), Scepters.MAGICAL_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            BreezeEntity breeze = new BreezeEntity(EntityType.BREEZE, world);
            player.damage(world, world.getDamageSources().mobAttack(breeze), 2);

            expectScepter(context, player.getMainHandStack(), Scepters.MAGICAL_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            EnderDragonEntity dragon = new EnderDragonEntity(EntityType.ENDER_DRAGON, world);
            player.damage(world, world.getDamageSources().mobAttack(dragon), 3);

            expectScepter(context, player.getMainHandStack(), Scepters.MAGICAL_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            EvokerEntity evoker = new EvokerEntity(EntityType.EVOKER, world);
            player.damage(world, world.getDamageSources().mobAttack(evoker), 4);

            expectScepter(context, player.getMainHandStack(), Scepters.MAGICAL_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            GhastEntity ghast = new GhastEntity(EntityType.GHAST, world);
            player.damage(world, world.getDamageSources().mobAttack(ghast), 5);

            expectScepter(context, player.getMainHandStack(), Scepters.MAGICAL_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            ElderGuardianEntity guardian = new ElderGuardianEntity(EntityType.ELDER_GUARDIAN, world);
            player.damage(world, world.getDamageSources().mobAttack(guardian), 6);

            expectScepter(context, player.getMainHandStack(), Scepters.MAGICAL_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            ShulkerEntity shulker = new ShulkerEntity(EntityType.SHULKER, world);
            player.damage(world, world.getDamageSources().mobAttack(shulker), 7);

            expectScepter(context, player.getMainHandStack(), Scepters.MAGICAL_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            WardenEntity warden = new WardenEntity(EntityType.WARDEN, world);
            player.damage(world, world.getDamageSources().mobAttack(warden), 8);

            expectScepter(context, player.getMainHandStack(), Scepters.MAGICAL_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            WitherEntity wither = new WitherEntity(EntityType.WITHER, world);
            player.damage(world, world.getDamageSources().mobAttack(wither), 9);

            expectScepter(context, player.getMainHandStack(), Scepters.MAGICAL_KEY);
        }
        context.complete();
    }

    private void expectScepter(TestContext context, ItemStack itemStack, RegistryKey<Scepter> expected) {
        Optional<RegistryEntry<Scepter>> scepter = ScepterContentsComponent.getScepter(itemStack);

        context.assertTrue(scepter.isPresent(),
                Text.of("Expected infused scepter to be present, but no scepter found"));
        assert scepter.isPresent();

        context.assertEquals(getScepter(context, expected).getIdAsString(), scepter.get().getIdAsString(),
                Text.of("scepter of item stack"));
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
}

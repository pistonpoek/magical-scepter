package io.github.pistonpoek.gametest.magicalscepter;

import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundPlayerLoadedPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.monster.illager.Evoker;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;
import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.BreezeWindCharge;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;

import java.lang.reflect.Method;
import java.util.Optional;

import static io.github.pistonpoek.gametest.magicalscepter.util.ContextUtil.*;

public class InfusionTest implements CustomTestMethodInvoker {
    @Override
    public void invokeTestMethod(GameTestHelper context, Method method) throws ReflectiveOperationException {
        method.invoke(this, context);
    }

    @GameTest(structure="gametest:template/empty")
    public void infuses(GameTestHelper context) {
        ServerPlayer player = createMockServerPlayer(context, GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.connection.handleAcceptPlayerLoad(new ServerboundPlayerLoadedPacket());
        player.getAbilities().invulnerable = false;

        setMagicalScepterInMainHand(context, player);

        Blaze blaze = new Blaze(EntityTypes.BLAZE, world);
        SmallFireball fireCharge = new SmallFireball(EntityTypes.SMALL_FIREBALL, world);
        player.hurtServer(world, world.damageSources().fireball(fireCharge, blaze), 1);

        expectScepter(context, player.getMainHandItem(), Scepters.BLAZE_KEY);
        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void noInfusionZeroDamage(GameTestHelper context) {
        ServerPlayer player = createMockServerPlayer(context, GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.connection.handleAcceptPlayerLoad(new ServerboundPlayerLoadedPacket());
        player.getAbilities().invulnerable = false;

        setMagicalScepterInMainHand(context, player);

        Blaze blaze = new Blaze(EntityTypes.BLAZE, world);
        SmallFireball fireCharge = new SmallFireball(EntityTypes.SMALL_FIREBALL, world);
        player.hurtServer(world, world.damageSources().fireball(fireCharge, blaze), 0);

        expectScepter(context, player.getMainHandItem(), Scepters.MAGICAL_KEY);
        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void infusionOffhand(GameTestHelper context) {
        ServerPlayer player = createMockServerPlayer(context, GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.connection.handleAcceptPlayerLoad(new ServerboundPlayerLoadedPacket());
        player.getAbilities().invulnerable = false;

        setMagicalScepterInHand(context, player, InteractionHand.OFF_HAND);

        Blaze blaze = new Blaze(EntityTypes.BLAZE, world);
        SmallFireball fireCharge = new SmallFireball(EntityTypes.SMALL_FIREBALL, world);
        player.hurtServer(world, world.damageSources().fireball(fireCharge, blaze), 0);

        expectScepter(context, player.getOffhandItem(), Scepters.MAGICAL_KEY);
        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void infusesOnlyMainHand(GameTestHelper context) {
        ServerPlayer player = createMockServerPlayer(context, GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.connection.handleAcceptPlayerLoad(new ServerboundPlayerLoadedPacket());
        player.getAbilities().invulnerable = false;

        setMagicalScepterInMainHand(context, player);
        setMagicalScepterInHand(context, player, InteractionHand.OFF_HAND);

        Blaze blaze = new Blaze(EntityTypes.BLAZE, world);
        SmallFireball fireCharge = new SmallFireball(EntityTypes.SMALL_FIREBALL, world);
        player.hurtServer(world, world.damageSources().fireball(fireCharge, blaze), 1);

        expectScepter(context, player.getMainHandItem(), Scepters.BLAZE_KEY);
        expectScepter(context, player.getOffhandItem(), Scepters.MAGICAL_KEY);
        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void infusesBothHands(GameTestHelper context) {
        ServerPlayer player = createMockServerPlayer(context, GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.connection.handleAcceptPlayerLoad(new ServerboundPlayerLoadedPacket());
        player.getAbilities().invulnerable = false;

        setMagicalScepterInMainHand(context, player);
        setMagicalScepterInHand(context, player, InteractionHand.OFF_HAND);

        Blaze blaze = new Blaze(EntityTypes.BLAZE, world);
        SmallFireball fireCharge = new SmallFireball(EntityTypes.SMALL_FIREBALL, world);
        player.hurtServer(world, world.damageSources().fireball(fireCharge, blaze), 1);

        Ghast ghast = new Ghast(EntityTypes.GHAST, world);
        LargeFireball fireball = new LargeFireball(EntityTypes.FIREBALL, world);
        player.hurtServer(world, world.damageSources().fireball(fireball, ghast), 2);

        expectScepter(context, player.getMainHandItem(), Scepters.BLAZE_KEY);
        expectScepter(context, player.getOffhandItem(), Scepters.GHAST_KEY);
        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void allInfusions(GameTestHelper context) {
        ServerPlayer player = createMockServerPlayer(context, GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.connection.handleAcceptPlayerLoad(new ServerboundPlayerLoadedPacket());
        player.getAbilities().invulnerable = false;

        {
            setMagicalScepterInMainHand(context, player);
            Blaze blaze = new Blaze(EntityTypes.BLAZE, world);
            SmallFireball fireCharge = new SmallFireball(EntityTypes.SMALL_FIREBALL, world);
            player.hurtServer(world, world.damageSources().fireball(fireCharge, blaze), 1);

            expectScepter(context, player.getMainHandItem(), Scepters.BLAZE_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            Breeze breeze = new Breeze(EntityTypes.BREEZE, world);
            BreezeWindCharge windCharge = new BreezeWindCharge(EntityTypes.BREEZE_WIND_CHARGE, world);
            player.hurtServer(world, world.damageSources().windCharge(windCharge, breeze), 2);

            expectScepter(context, player.getMainHandItem(), Scepters.BREEZE_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            EnderDragon dragon = new EnderDragon(EntityTypes.ENDER_DRAGON, world);
            AreaEffectCloud effectCloud = new AreaEffectCloud(EntityTypes.AREA_EFFECT_CLOUD, world);
            player.hurtServer(world, world.damageSources().indirectMagic(effectCloud, dragon), 3);

            expectScepter(context, player.getMainHandItem(), Scepters.DRAGON_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            Evoker evoker = new Evoker(EntityTypes.EVOKER, world);
            EvokerFangs fangs = new EvokerFangs(EntityTypes.EVOKER_FANGS, world);
            player.hurtServer(world, world.damageSources().indirectMagic(fangs, evoker), 4);

            expectScepter(context, player.getMainHandItem(), Scepters.EVOKER_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            Ghast ghast = new Ghast(EntityTypes.GHAST, world);
            LargeFireball fireball = new LargeFireball(EntityTypes.FIREBALL, world);
            player.hurtServer(world, world.damageSources().fireball(fireball, ghast), 5);

            expectScepter(context, player.getMainHandItem(), Scepters.GHAST_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            ElderGuardian guardian = new ElderGuardian(EntityTypes.ELDER_GUARDIAN, world);
            player.hurtServer(world, world.damageSources().indirectMagic(guardian, guardian), 6);

            expectScepter(context, player.getMainHandItem(), Scepters.GUARDIAN_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            Shulker shulker = new Shulker(EntityTypes.SHULKER, world);
            ShulkerBullet bullet = new ShulkerBullet(EntityTypes.SHULKER_BULLET, world);
            player.hurtServer(world, world.damageSources().mobProjectile(bullet, shulker), 7);

            expectScepter(context, player.getMainHandItem(), Scepters.SHULKER_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            Warden warden = new Warden(EntityTypes.WARDEN, world);
            player.hurtServer(world, world.damageSources().sonicBoom(warden), 8);

            expectScepter(context, player.getMainHandItem(), Scepters.WARDEN_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            WitherBoss wither = new WitherBoss(EntityTypes.WITHER, world);
            WitherSkull skull = new WitherSkull(EntityTypes.WITHER_SKULL, world);
            player.hurtServer(world, world.damageSources().witherSkull(skull, wither), 9);

            expectScepter(context, player.getMainHandItem(), Scepters.WITHER_KEY);
        }
        context.succeed();
    }

    // TODO Be more expansive in testing edge cases.
    @GameTest(structure="gametest:template/empty")
    public void noInfusionsMobAttack(GameTestHelper context) {
        ServerPlayer player = createMockServerPlayer(context, GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.connection.handleAcceptPlayerLoad(new ServerboundPlayerLoadedPacket());
        player.getAbilities().invulnerable = false;

        {
            setMagicalScepterInMainHand(context, player);
            Blaze blaze = new Blaze(EntityTypes.BLAZE, world);
            player.hurtServer(world, world.damageSources().mobAttack(blaze), 1);

            expectScepter(context, player.getMainHandItem(), Scepters.MAGICAL_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            Breeze breeze = new Breeze(EntityTypes.BREEZE, world);
            player.hurtServer(world, world.damageSources().mobAttack(breeze), 2);

            expectScepter(context, player.getMainHandItem(), Scepters.MAGICAL_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            EnderDragon dragon = new EnderDragon(EntityTypes.ENDER_DRAGON, world);
            player.hurtServer(world, world.damageSources().mobAttack(dragon), 3);

            expectScepter(context, player.getMainHandItem(), Scepters.MAGICAL_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            Evoker evoker = new Evoker(EntityTypes.EVOKER, world);
            player.hurtServer(world, world.damageSources().mobAttack(evoker), 4);

            expectScepter(context, player.getMainHandItem(), Scepters.MAGICAL_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            Ghast ghast = new Ghast(EntityTypes.GHAST, world);
            player.hurtServer(world, world.damageSources().mobAttack(ghast), 5);

            expectScepter(context, player.getMainHandItem(), Scepters.MAGICAL_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            ElderGuardian guardian = new ElderGuardian(EntityTypes.ELDER_GUARDIAN, world);
            player.hurtServer(world, world.damageSources().mobAttack(guardian), 6);

            expectScepter(context, player.getMainHandItem(), Scepters.MAGICAL_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            Shulker shulker = new Shulker(EntityTypes.SHULKER, world);
            player.hurtServer(world, world.damageSources().mobAttack(shulker), 7);

            expectScepter(context, player.getMainHandItem(), Scepters.MAGICAL_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            Warden warden = new Warden(EntityTypes.WARDEN, world);
            player.hurtServer(world, world.damageSources().mobAttack(warden), 8);

            expectScepter(context, player.getMainHandItem(), Scepters.MAGICAL_KEY);
        }
        {
            setMagicalScepterInMainHand(context, player);
            WitherBoss wither = new WitherBoss(EntityTypes.WITHER, world);
            player.hurtServer(world, world.damageSources().mobAttack(wither), 9);

            expectScepter(context, player.getMainHandItem(), Scepters.MAGICAL_KEY);
        }
        context.succeed();
    }

    private void expectScepter(GameTestHelper context, ItemStack itemStack, ResourceKey<Scepter> expected) {
        Optional<Holder<Scepter>> scepter = ScepterContentsComponent.getScepter(itemStack);

        context.assertTrue(scepter.isPresent(),
                Component.nullToEmpty("Expected infused scepter to be present, but no scepter found"));
        assert scepter.isPresent();

        context.assertValueEqual(getScepter(context, expected).getRegisteredName(), scepter.get().getRegisteredName(),
                Component.nullToEmpty("scepter of item stack"));
    }
}

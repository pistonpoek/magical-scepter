package io.github.pistonpoek.gametest.magicalscepter;

import io.github.pistonpoek.gametest.TestBlockChecker;
import io.github.pistonpoek.magicalscepter.entity.effect.ModStatusEffects;
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundPlayerLoadedPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.level.GameType;

import java.lang.reflect.Method;

import static io.github.pistonpoek.gametest.magicalscepter.util.ContextUtil.createMockServerPlayer;

public class StatusEffectTest implements CustomTestMethodInvoker {
    private static final float DAMAGE_AMOUNT = 4.0F;

    @Override
    public void invokeTestMethod(GameTestHelper context, Method method) throws ReflectiveOperationException {
        method.invoke(this, context);
    }

    @GameTest(structure="gametest:template/empty")
    public void damageWithRepulsionEffect(GameTestHelper context) {
        ServerPlayer player = createMockServerPlayer(context, GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.connection.handleAcceptPlayerLoad(new ServerboundPlayerLoadedPacket());
        player.getAbilities().invulnerable = false;
        player.addEffect(new MobEffectInstance(ModStatusEffects.REPULSION));
        player.hurtServer(world, world.damageSources().magic(), DAMAGE_AMOUNT);
        context.assertValueEqual(player.getMaxHealth() - DAMAGE_AMOUNT, player.getHealth(), Component.nullToEmpty("player health"));
        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void damagePersistentProjectileWithoutRepulsionEffect(GameTestHelper context) {
        ServerPlayer player = createMockServerPlayer(context, GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.connection.handleAcceptPlayerLoad(new ServerboundPlayerLoadedPacket());
        player.getAbilities().invulnerable = false;
        Skeleton skeleton = new Skeleton(EntityTypes.SKELETON, world);
        Arrow arrow = new Arrow(EntityTypes.ARROW, world);
        player.hurtServer(world, world.damageSources().mobProjectile(arrow, skeleton), DAMAGE_AMOUNT);
        context.assertValueEqual(player.getMaxHealth() - DAMAGE_AMOUNT, player.getHealth(), Component.nullToEmpty("player health"));
        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void damagePersistentProjectileWithRepulsionEffect(GameTestHelper context) {
        ServerPlayer player = createMockServerPlayer(context, GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.connection.handleAcceptPlayerLoad(new ServerboundPlayerLoadedPacket());
        player.getAbilities().invulnerable = false;
        player.addEffect(new MobEffectInstance(ModStatusEffects.REPULSION));
        Skeleton skeleton = new Skeleton(EntityTypes.SKELETON, world);
        Arrow arrow = new Arrow(EntityTypes.ARROW, world);
        player.hurtServer(world, world.damageSources().mobProjectile(arrow, skeleton), DAMAGE_AMOUNT);
        context.assertValueEqual(player.getMaxHealth(), player.getHealth(), Component.nullToEmpty("player health"));
        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void bypassedDamageSourcePersistentProjectileWithRepulsionEffect(GameTestHelper context) {
        ServerPlayer player = createMockServerPlayer(context, GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.connection.handleAcceptPlayerLoad(new ServerboundPlayerLoadedPacket());
        player.getAbilities().invulnerable = false;
        player.addEffect(new MobEffectInstance(ModStatusEffects.REPULSION));
        Arrow arrow = new Arrow(EntityTypes.ARROW, world);
        player.hurtServer(world, world.damageSources().source(DamageTypes.GENERIC_KILL, arrow), DAMAGE_AMOUNT);
        context.assertValueEqual(player.getMaxHealth() - DAMAGE_AMOUNT, player.getHealth(), Component.nullToEmpty("player health"));
        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void damageWithStabilityEffect(GameTestHelper context) {
        ServerPlayer player = createMockServerPlayer(context, GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.connection.handleAcceptPlayerLoad(new ServerboundPlayerLoadedPacket());
        player.getAbilities().invulnerable = false;
        player.addEffect(new MobEffectInstance(ModStatusEffects.STABILITY));
        player.hurtServer(world, world.damageSources().magic(), DAMAGE_AMOUNT);
        context.assertValueEqual(player.getMaxHealth() - DAMAGE_AMOUNT, player.getHealth(), Component.nullToEmpty("player health"));
        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void damageExplosionWithoutStabilityEffect(GameTestHelper context) {
        ServerPlayer player = createMockServerPlayer(context, GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.connection.handleAcceptPlayerLoad(new ServerboundPlayerLoadedPacket());
        player.getAbilities().invulnerable = false;
        Creeper creeper = new Creeper(EntityTypes.CREEPER, world);
        player.hurtServer(world, world.damageSources().explosion(creeper, creeper), DAMAGE_AMOUNT);
        context.assertValueEqual(player.getMaxHealth() - DAMAGE_AMOUNT, player.getHealth(), Component.nullToEmpty("player health"));
        context.succeed();
        new TestBlockChecker(context).start();
        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void damageExplosionWithStabilityEffect(GameTestHelper context) {
        ServerPlayer player = createMockServerPlayer(context, GameType.SURVIVAL);
        ServerLevel world = context.getLevel();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.connection.handleAcceptPlayerLoad(new ServerboundPlayerLoadedPacket());
        player.getAbilities().invulnerable = false;
        player.addEffect(new MobEffectInstance(ModStatusEffects.STABILITY));
        Creeper creeper = new Creeper(EntityTypes.CREEPER, world);
        player.hurtServer(world, world.damageSources().explosion(creeper, creeper), DAMAGE_AMOUNT);
        context.assertValueEqual(player.getMaxHealth(), player.getHealth(), Component.nullToEmpty("player health"));
        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void checkKnockbackResistance(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);

        double valueWithout = player.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        context.assertValueEqual(0.0, valueWithout, Component.nullToEmpty("knockback resistance attribute"));

        player.addEffect(new MobEffectInstance(ModStatusEffects.STABILITY, 10, 0));
        double valueAmplifier0 = player.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        context.assertValueEqual(0.3, valueAmplifier0, Component.nullToEmpty("knockback resistance attribute"));

        player.addEffect(new MobEffectInstance(ModStatusEffects.STABILITY, 10, 1));
        double valueAmplifier1 = player.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        context.assertValueEqual(0.6, valueAmplifier1, Component.nullToEmpty("knockback resistance attribute"));

        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void checkExplosionKnockbackResistance(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);

        double valueWithout = player.getAttributeValue(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE);
        context.assertValueEqual(0.0, valueWithout, Component.nullToEmpty("explosion knockback resistance attribute"));

        player.addEffect(new MobEffectInstance(ModStatusEffects.STABILITY, 10, 0));
        double valueAmplifier0 = player.getAttributeValue(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE);
        context.assertValueEqual(0.3, valueAmplifier0, Component.nullToEmpty("explosion knockback resistance attribute"));

        player.addEffect(new MobEffectInstance(ModStatusEffects.STABILITY, 10, 1));
        double valueAmplifier1 = player.getAttributeValue(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE);
        context.assertValueEqual(0.6, valueAmplifier1, Component.nullToEmpty("explosion knockback resistance attribute"));

        context.succeed();
    }
}
package io.github.pistonpoek.gametest.magicalscepter;

import io.github.pistonpoek.gametest.TestBlockChecker;
import io.github.pistonpoek.magicalscepter.entity.effect.ModStatusEffects;
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.network.packet.c2s.play.PlayerLoadedC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;

import java.lang.reflect.Method;

import static io.github.pistonpoek.gametest.magicalscepter.util.ContextUtil.createMockServerPlayer;

public class StatusEffectTest implements CustomTestMethodInvoker {
    private static final float DAMAGE_AMOUNT = 4.0F;

    @Override
    public void invokeTestMethod(TestContext context, Method method) throws ReflectiveOperationException {
        method.invoke(this, context);
    }

    @GameTest(structure="gametest:template/empty")
    public void damageWithRepulsionEffect(TestContext context) {
        ServerPlayerEntity player = createMockServerPlayer(context, GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.networkHandler.onPlayerLoaded(new PlayerLoadedC2SPacket());
        player.getAbilities().invulnerable = false;
        player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.REPULSION));
        player.damage(world, world.getDamageSources().magic(), DAMAGE_AMOUNT);
        context.assertEquals(player.getMaxHealth() - DAMAGE_AMOUNT, player.getHealth(), Text.of("player health"));
        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void damagePersistentProjectileWithoutRepulsionEffect(TestContext context) {
        ServerPlayerEntity player = createMockServerPlayer(context, GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.networkHandler.onPlayerLoaded(new PlayerLoadedC2SPacket());
        player.getAbilities().invulnerable = false;
        SkeletonEntity skeleton = new SkeletonEntity(EntityType.SKELETON, world);
        ArrowEntity arrow = new ArrowEntity(EntityType.ARROW, world);
        player.damage(world, world.getDamageSources().mobProjectile(arrow, skeleton), DAMAGE_AMOUNT);
        context.assertEquals(player.getMaxHealth() - DAMAGE_AMOUNT, player.getHealth(), Text.of("player health"));
        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void damagePersistentProjectileWithRepulsionEffect(TestContext context) {
        ServerPlayerEntity player = createMockServerPlayer(context, GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.networkHandler.onPlayerLoaded(new PlayerLoadedC2SPacket());
        player.getAbilities().invulnerable = false;
        player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.REPULSION));
        SkeletonEntity skeleton = new SkeletonEntity(EntityType.SKELETON, world);
        ArrowEntity arrow = new ArrowEntity(EntityType.ARROW, world);
        player.damage(world, world.getDamageSources().mobProjectile(arrow, skeleton), DAMAGE_AMOUNT);
        context.assertEquals(player.getMaxHealth(), player.getHealth(), Text.of("player health"));
        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void bypassedDamageSourcePersistentProjectileWithRepulsionEffect(TestContext context) {
        ServerPlayerEntity player = createMockServerPlayer(context, GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.networkHandler.onPlayerLoaded(new PlayerLoadedC2SPacket());
        player.getAbilities().invulnerable = false;
        player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.REPULSION));
        ArrowEntity arrow = new ArrowEntity(EntityType.ARROW, world);
        player.damage(world, world.getDamageSources().create(DamageTypes.GENERIC_KILL, arrow), DAMAGE_AMOUNT);
        context.assertEquals(player.getMaxHealth() - DAMAGE_AMOUNT, player.getHealth(), Text.of("player health"));
        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void damageWithStabilityEffect(TestContext context) {
        ServerPlayerEntity player = createMockServerPlayer(context, GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.networkHandler.onPlayerLoaded(new PlayerLoadedC2SPacket());
        player.getAbilities().invulnerable = false;
        player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.STABILITY));
        player.damage(world, world.getDamageSources().magic(), DAMAGE_AMOUNT);
        context.assertEquals(player.getMaxHealth() - DAMAGE_AMOUNT, player.getHealth(), Text.of("player health"));
        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void damageExplosionWithoutStabilityEffect(TestContext context) {
        ServerPlayerEntity player = createMockServerPlayer(context, GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.networkHandler.onPlayerLoaded(new PlayerLoadedC2SPacket());
        player.getAbilities().invulnerable = false;
        CreeperEntity creeper = new CreeperEntity(EntityType.CREEPER, world);
        player.damage(world, world.getDamageSources().explosion(creeper, creeper), DAMAGE_AMOUNT);
        context.assertEquals(player.getMaxHealth() - DAMAGE_AMOUNT, player.getHealth(), Text.of("player health"));
        context.complete();
        new TestBlockChecker(context).start();
        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void damageExplosionWithStabilityEffect(TestContext context) {
        ServerPlayerEntity player = createMockServerPlayer(context, GameMode.SURVIVAL);
        ServerWorld world = context.getWorld();
        // Setting player to be loaded and not be invulnerable to allow damage to be taken.
        player.networkHandler.onPlayerLoaded(new PlayerLoadedC2SPacket());
        player.getAbilities().invulnerable = false;
        player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.STABILITY));
        CreeperEntity creeper = new CreeperEntity(EntityType.CREEPER, world);
        player.damage(world, world.getDamageSources().explosion(creeper, creeper), DAMAGE_AMOUNT);
        context.assertEquals(player.getMaxHealth(), player.getHealth(), Text.of("player health"));
        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void checkKnockbackResistance(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);

        double valueWithout = player.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE);
        context.assertEquals(0.0, valueWithout, Text.of("knockback resistance attribute"));

        player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.STABILITY, 10, 0));
        double valueAmplifier0 = player.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE);
        context.assertEquals(0.3, valueAmplifier0, Text.of("knockback resistance attribute"));

        player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.STABILITY, 10, 1));
        double valueAmplifier1 = player.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE);
        context.assertEquals(0.6, valueAmplifier1, Text.of("knockback resistance attribute"));

        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void checkExplosionKnockbackResistance(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);

        double valueWithout = player.getAttributeValue(EntityAttributes.EXPLOSION_KNOCKBACK_RESISTANCE);
        context.assertEquals(0.0, valueWithout, Text.of("explosion knockback resistance attribute"));

        player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.STABILITY, 10, 0));
        double valueAmplifier0 = player.getAttributeValue(EntityAttributes.EXPLOSION_KNOCKBACK_RESISTANCE);
        context.assertEquals(0.3, valueAmplifier0, Text.of("explosion knockback resistance attribute"));

        player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.STABILITY, 10, 1));
        double valueAmplifier1 = player.getAttributeValue(EntityAttributes.EXPLOSION_KNOCKBACK_RESISTANCE);
        context.assertEquals(0.6, valueAmplifier1, Text.of("explosion knockback resistance attribute"));

        context.complete();
    }
}
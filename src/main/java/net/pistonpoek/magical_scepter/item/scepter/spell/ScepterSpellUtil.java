package net.pistonpoek.magical_scepter.item.scepter.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.EntityEffectParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ScepterSpellUtil {

    public static void addScepterCastParticles(LivingEntity caster, int color, Vec3d position) {
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        int steps = 64;
        for (int i = 0; i < steps; i++) {
            double delta = i / (double) steps;
            double x = MathHelper.lerp(delta, caster.getX(), position.getX());
            double y = MathHelper.lerp(delta, caster.getY(), position.getY());
            double z = MathHelper.lerp(delta, caster.getZ(), position.getZ());
            caster.getWorld().addParticle(EntityEffectParticleEffect.create(ParticleTypes.ENTITY_EFFECT, red, green, blue),
                    x, y , z, 0, 0, 0);
        }
    }

    public static void addEffectParticles(LivingEntity caster, int color) {
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        for (int i = 0; i < 64; i++) {
            caster.getWorld().addParticle(EntityEffectParticleEffect.create(ParticleTypes.ENTITY_EFFECT, red, green, blue),
                    caster.getX(), caster.getY(), caster.getZ(), 0, 0, 0);
        }
    }

    public static Vec3d getProjectilePosition(LivingEntity caster) {
        return getProjectilePosition(caster, 3.0f);
    }

    public static Vec3d getProjectilePosition(LivingEntity caster, float forwardDistance) {
        Vec3d look = caster.getRotationVector().multiply(forwardDistance * caster.getScaleFactor());
        return caster.getPos().add(0, caster.getStandingEyeHeight() - 0.15f, 0).add(look);
    }

    public static Vec3d getProjectileRotation(LivingEntity caster) {
        return caster.getRotationVector().normalize();
    }
}

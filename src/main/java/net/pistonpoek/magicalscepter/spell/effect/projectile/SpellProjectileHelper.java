package net.pistonpoek.magicalscepter.spell.effect.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class SpellProjectileHelper {
    public static Vec3d getProjectilePosition(Entity caster) {
        if (caster instanceof PlayerEntity) {
            return getProjectilePosition((PlayerEntity)caster);
        } else if (caster instanceof LivingEntity) {
            return getProjectilePosition(caster, caster.getWidth() / 2.0F);
        } else {
            return getProjectilePosition(caster, 0.0F);
        }
    }

    private static Vec3d getProjectilePosition(Entity caster, float forwardDistance) {
        Vec3d look = caster.getRotationVector().multiply(forwardDistance * getScaleFactor(caster));
        return caster.getPos().add(0, caster.getStandingEyeHeight(), 0).add(look);
    }

    private static Vec3d getProjectilePosition(PlayerEntity caster) {
        Vec3d look = caster.getRotationVector().multiply(1.5F * getScaleFactor(caster));
        return caster.getPos().add(0, caster.getStandingEyeHeight() - 0.15f, 0).add(look);
    }

    private static float getScaleFactor(Entity caster) {
        return caster instanceof LivingEntity ? ((LivingEntity)caster).getScaleFactor() : 1.0f;
    }
}

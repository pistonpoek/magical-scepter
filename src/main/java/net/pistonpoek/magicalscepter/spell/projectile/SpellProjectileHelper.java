package net.pistonpoek.magicalscepter.spell.projectile;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

public class SpellProjectileHelper {
    public static Vec3d getProjectilePosition(LivingEntity caster) {
        return getProjectilePosition(caster, 3.0f);
    }

    public static Vec3d getProjectilePosition(LivingEntity caster, float forwardDistance) {
        Vec3d look = caster.getRotationVector().multiply(forwardDistance * caster.getScaleFactor());
        return caster.getPos().add(0, caster.getStandingEyeHeight() - 0.15f, 0).add(look);
    }
}

package net.pistonpoek.magical_scepter.item.scepter.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.function.Predicate;

public class WardenScepterSpell extends InstantScepterSpell {
    private final static float RANGE = 15;
    private final static float ATTACK_SIZE = 2;

    public WardenScepterSpell(int experienceCost, int castCooldown) {
        super(experienceCost, castCooldown);
    }

    @Override
    public void castSpell(LivingEntity caster) {
        Vec3d casterPosition = ScepterSpellUtil.getProjectilePosition(caster, 0);
        Vec3d casterRotation = ScepterSpellUtil.getProjectileRotation(caster).normalize();
        // Check what targets should be affected by the sonic boom attack that is being cast.
        Predicate<? super Entity> predicate = (entity) -> {
            if (!entity.isLiving()) return false;

            Vec3d difference = entity.getBoundingBox().getCenter().subtract(casterPosition);
            double value = difference.dotProduct(casterRotation);

            if (0 > value || value > RANGE) return false;

            Vec3d closedLinePoint = casterRotation.multiply(value);
            double distanceToCenterBoom = closedLinePoint.distanceTo(difference);

            return distanceToCenterBoom < ATTACK_SIZE;
        };
        for (Entity entity:caster.getWorld().getOtherEntities(caster, Box.of(casterPosition, RANGE * 2, RANGE * 2, RANGE * 2), predicate)) {
            if (!(entity instanceof LivingEntity livingEntity)) return;

            livingEntity.damage(caster.getWorld().getDamageSources().sonicBoom(caster), 3.0f); // Warden sonic boom deals 10.0f damage.
            double knockbackResistanceModifier = (1.0 - livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
            double verticalKnockback = 0.2 * knockbackResistanceModifier; // Warden sonic boom deals 0.5f vertical knockback.
            double horizontalKnockback = 1.0 * knockbackResistanceModifier; // Warden sonic boom deals 2.5f horizontal knockback.
            livingEntity.addVelocity(casterRotation.getX() * horizontalKnockback, casterRotation.getY() * verticalKnockback, casterRotation.getZ() * horizontalKnockback);
        }
    }

    @Override
    public void displaySpell(LivingEntity caster, int remainingCastTicks) {
        Vec3d casterPosition = ScepterSpellUtil.getProjectilePosition(caster);
        Vec3d casterRotation = ScepterSpellUtil.getProjectileRotation(caster).normalize();
        for (int i = 1; i < RANGE; ++i) {
            Vec3d vec3d4 = casterPosition.add(casterRotation.multiply(i));
            caster.getWorld().addParticle(ParticleTypes.SONIC_BOOM, vec3d4.x, vec3d4.y, vec3d4.z, 0.0, 0.0, 0.0);
        }
        caster.playSound(SoundEvents.ENTITY_WARDEN_SONIC_BOOM, 1.0f, 1.0f);
    }
}

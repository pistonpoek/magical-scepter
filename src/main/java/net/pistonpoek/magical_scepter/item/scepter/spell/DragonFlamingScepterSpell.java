package net.pistonpoek.magical_scepter.item.scepter.spell;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class DragonFlamingScepterSpell
        extends ScepterSpell {
    public DragonFlamingScepterSpell(int experienceCost, int castCooldown) {
        super(experienceCost, castCooldown);
    }

    public int getCastDuration() {
        return 10;
    }

    public void castSpell(LivingEntity caster) {
    }

    public void displaySpell(LivingEntity caster, int remainingCastTicks) {
        // Reference Sitting Flaming Phase, where dragon breaths.
        if (remainingCastTicks % 2 == 0 && remainingCastTicks > 0) {
            Vec3d vec3d = caster.getRotationVector();
            vec3d.rotateY(-0.7853982f);
            double d = caster.getX();
            double e = caster.getBodyY(0.5);
            double f = caster.getZ();
            for (int i = 0; i < 8; ++i) {
                double g = d + caster.getRandom().nextGaussian() / 2.0;
                double h = e + caster.getRandom().nextGaussian() / 2.0;
                double j = f + caster.getRandom().nextGaussian() / 2.0;
                for (int k = 0; k < 6; ++k) {
                    caster.getWorld().addParticle(ParticleTypes.DRAGON_BREATH, g, h, j, -vec3d.x * (double) 0.08f * (double) k, -vec3d.y * (double) 0.6f, -vec3d.z * (double) 0.08f * (double) k);
                }
                vec3d.rotateY(0.19634955f);
            }
        }
    }

    public void updateSpell(LivingEntity caster, int remainingCastTicks) {
        // Reference Sitting Flaming Phase, where dragon breaths.
        if (remainingCastTicks == 0) {
            double g;
            float f = 5.0f;
            double x = caster.getX();
            double z = caster.getZ();
            double y = g = caster.getEyeY(); //caster.getBodyY(0.5);
            BlockPos.Mutable mutable = new BlockPos.Mutable(x, y, z);
            while (caster.getWorld().isAir(mutable)) {
                if ((y -= 1.0) < 0.0) {
                    y = g;
                    break;
                }
                mutable.set(x, y, z);
            }
            y = MathHelper.floor(y) + 1;
            AreaEffectCloudEntity dragonBreathEntity = new AreaEffectCloudEntity(caster.getWorld(), x, y, z);
            dragonBreathEntity.setOwner(caster);
            dragonBreathEntity.setRadius(5.0f);
            dragonBreathEntity.setDuration(200);
            dragonBreathEntity.setParticleType(ParticleTypes.DRAGON_BREATH);
            dragonBreathEntity.addEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE));
            caster.getWorld().spawnEntity(dragonBreathEntity);
        }
    }

    public void endSpell(LivingEntity caster, int remainingCastTicks) {

    }
}

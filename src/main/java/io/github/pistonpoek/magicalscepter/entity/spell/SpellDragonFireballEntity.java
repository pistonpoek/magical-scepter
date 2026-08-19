package io.github.pistonpoek.magicalscepter.entity.spell;

import java.util.List;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.PowerParticleOption;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class SpellDragonFireballEntity extends AbstractHurtingProjectile {
    public SpellDragonFireballEntity(EntityType<? extends SpellDragonFireballEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (hitResult.getType() != HitResult.Type.ENTITY || !this.ownedBy(((EntityHitResult)hitResult).getEntity())) {
            if (!this.level().isClientSide()) {
                AreaEffectCloud areaEffectCloudEntity = createDragonBreath();
                List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class,
                        this.getBoundingBox().inflate(4.0, 2.0, 4.0));
                if (!list.isEmpty()) {
                    for (LivingEntity livingEntity : list) {
                        double d = this.distanceToSqr(livingEntity);
                        if (d < 16.0) {
                            areaEffectCloudEntity.setPos(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                            break;
                        }
                    }
                }

                this.level().addFreshEntity(areaEffectCloudEntity);
                this.discard();
            }
            if (!this.isSilent()) {
                this.level().playLocalSound(blockPosition(),
                        SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.HOSTILE,
                        1.0F, random.nextFloat() * 0.1F + 0.9F, false);
            }
        }
    }

    private AreaEffectCloud createDragonBreath() {
        AreaEffectCloud areaEffectCloudEntity = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity) {
            areaEffectCloudEntity.setOwner((LivingEntity)entity);
        }

        areaEffectCloudEntity.setCustomParticle(PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1.0F));
        areaEffectCloudEntity.setRadius(2.4F);
        areaEffectCloudEntity.setDuration(72);
        areaEffectCloudEntity.setRadiusPerTick(-0.004F);
        areaEffectCloudEntity.addEffect(new MobEffectInstance(MobEffects.INSTANT_DAMAGE, 1, 1));
        return areaEffectCloudEntity;
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1.0F);
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }
}

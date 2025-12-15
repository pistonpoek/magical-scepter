package io.github.pistonpoek.magicalscepter.entity.spell;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.particle.DragonBreathParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import java.util.List;

public class SpellDragonFireballEntity extends ExplosiveProjectileEntity {
    public SpellDragonFireballEntity(EntityType<? extends SpellDragonFireballEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (hitResult.getType() != HitResult.Type.ENTITY || !this.isOwner(((EntityHitResult)hitResult).getEntity())) {
            if (!this.getEntityWorld().isClient()) {
                AreaEffectCloudEntity areaEffectCloudEntity = createDragonBreath();
                List<LivingEntity> list = this.getEntityWorld().getNonSpectatingEntities(LivingEntity.class,
                        this.getBoundingBox().expand(4.0, 2.0, 4.0));
                if (!list.isEmpty()) {
                    for (LivingEntity livingEntity : list) {
                        double d = this.squaredDistanceTo(livingEntity);
                        if (d < 16.0) {
                            areaEffectCloudEntity.setPosition(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                            break;
                        }
                    }
                }

                this.getEntityWorld().spawnEntity(areaEffectCloudEntity);
                this.discard();
            }
            if (!this.isSilent()) {
                this.getEntityWorld().playSoundAtBlockCenterClient(getBlockPos(),
                        SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.HOSTILE,
                        1.0F, random.nextFloat() * 0.1F + 0.9F, false);
            }
        }
    }

    private AreaEffectCloudEntity createDragonBreath() {
        AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(this.getEntityWorld(), this.getX(), this.getY(), this.getZ());
        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity) {
            areaEffectCloudEntity.setOwner((LivingEntity)entity);
        }

        areaEffectCloudEntity.setParticleType(DragonBreathParticleEffect.of(ParticleTypes.DRAGON_BREATH, 1.0F));
        areaEffectCloudEntity.setRadius(2.4F);
        areaEffectCloudEntity.setDuration(72);
        areaEffectCloudEntity.setRadiusGrowth(-0.004F);
        areaEffectCloudEntity.addEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 1));
        return areaEffectCloudEntity;
    }

    @Override
    protected ParticleEffect getParticleType() {
        return DragonBreathParticleEffect.of(ParticleTypes.DRAGON_BREATH, 1.0F);
    }

    @Override
    protected boolean isBurning() {
        return false;
    }
}

package io.github.pistonpoek.magicalscepter.entity.spell;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

/**
 * Wither skull entity that has modified behavior to be suitable as spell projectile.
 */
public class SpellWitherSkullEntity extends ExplosiveProjectileEntity {
    public SpellWitherSkullEntity(EntityType<? extends SpellWitherSkullEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (!(this.getEntityWorld() instanceof ServerWorld serverWorld)) {
            return;
        }
        Entity entity = entityHitResult.getEntity();
        boolean damaged;

        if (this.getOwner() instanceof LivingEntity livingEntity) {
            DamageSource damageSource = this.getDamageSources()
                    .create(DamageTypes.WITHER_SKULL, this, livingEntity);
            damaged = entity.damage(serverWorld, damageSource, 8.0F);
            if (!damaged) {
                return;
            }

            if (entity.isAlive()) {
                EnchantmentHelper.onTargetDamaged(serverWorld, entity, damageSource);
            } else {
                livingEntity.heal(5.0F);
            }
        } else {
            damaged = entity.damage(serverWorld, this.getDamageSources().magic(), 5.0F);
        }

        if (damaged && entity instanceof LivingEntity livingEntity) {
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 160, 1),
                    this.getEffectCause());
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getEntityWorld().isClient()) {
            this.discard();
        }
    }

    @Override
    protected boolean isBurning() {
        return false;
    }
}

package io.github.pistonpoek.magicalscepter.entity.spell;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Wither skull entity that has modified behavior to be suitable as spell projectile.
 */
public class SpellWitherSkullEntity extends AbstractHurtingProjectile {
    public SpellWitherSkullEntity(EntityType<? extends SpellWitherSkullEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        if (!(this.level() instanceof ServerLevel serverWorld)) {
            return;
        }
        Entity entity = entityHitResult.getEntity();
        boolean damaged;

        if (this.getOwner() instanceof LivingEntity livingEntity) {
            DamageSource damageSource = this.damageSources()
                    .source(DamageTypes.WITHER_SKULL, this, livingEntity);
            damaged = entity.hurtServer(serverWorld, damageSource, 8.0F);
            if (!damaged) {
                return;
            }

            if (entity.isAlive()) {
                EnchantmentHelper.doPostAttackEffects(serverWorld, entity, damageSource);
            } else {
                livingEntity.heal(5.0F);
            }
        } else {
            damaged = entity.hurtServer(serverWorld, this.damageSources().magic(), 5.0F);
        }

        if (damaged && entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 160, 1),
                    this.getEffectSource());
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide()) {
            this.discard();
        }
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }
}

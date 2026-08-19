package io.github.pistonpoek.magicalscepter.entity.spell;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.hurtingprojectile.Fireball;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Fire charge entity that has modified behavior to be suitable as spell projectile.
 */
public class SpellFireChargeEntity extends Fireball {
    public SpellFireChargeEntity(EntityType<? extends SpellFireChargeEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        if (this.level() instanceof ServerLevel serverWorld) {
            Entity entity = entityHitResult.getEntity();
            int fireTicks = entity.getRemainingFireTicks();
            entity.igniteForSeconds(5.0F);
            DamageSource damageSource = this.damageSources().fireball(this, this.getOwner());
            if (!entity.hurtServer(serverWorld, damageSource, 5.0F)) {
                entity.setRemainingFireTicks(fireTicks);
            } else {
                EnchantmentHelper.doPostAttackEffects(serverWorld, entity, damageSource);
            }
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide()) {
            this.discard();
        }
    }
}

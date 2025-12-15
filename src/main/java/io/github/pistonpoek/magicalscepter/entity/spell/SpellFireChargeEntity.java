package io.github.pistonpoek.magicalscepter.entity.spell;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

/**
 * Fire charge entity that has modified behavior to be suitable as spell projectile.
 */
public class SpellFireChargeEntity extends AbstractFireballEntity {
    public SpellFireChargeEntity(EntityType<? extends SpellFireChargeEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (this.getEntityWorld() instanceof ServerWorld serverWorld) {
            Entity entity = entityHitResult.getEntity();
            int fireTicks = entity.getFireTicks();
            entity.setOnFireFor(5.0F);
            DamageSource damageSource = this.getDamageSources().fireball(this, this.getOwner());
            if (!entity.damage(serverWorld, damageSource, 5.0F)) {
                entity.setFireTicks(fireTicks);
            } else {
                EnchantmentHelper.onTargetDamaged(serverWorld, entity, damageSource);
            }
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getEntityWorld().isClient()) {
            this.discard();
        }
    }
}

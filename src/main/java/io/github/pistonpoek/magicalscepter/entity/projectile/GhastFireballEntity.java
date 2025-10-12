package io.github.pistonpoek.magicalscepter.entity.projectile;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class GhastFireballEntity extends AbstractFireballEntity {
    public GhastFireballEntity(EntityType<? extends GhastFireballEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (this.getEntityWorld() instanceof ServerWorld serverWorld) {
            serverWorld.createExplosion(this, this.getX(), this.getY(), this.getZ(),
                    1.0F, false, World.ExplosionSourceType.NONE);
            this.discard();
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (this.getEntityWorld() instanceof ServerWorld serverWorld) {
            Entity entity = entityHitResult.getEntity();
            Entity owner = this.getOwner();
            DamageSource damageSource = this.getDamageSources().fireball(this, owner);
            entity.damage(serverWorld, damageSource, 6.0F);
            EnchantmentHelper.onTargetDamaged(serverWorld, entity, damageSource);
        }
    }
}

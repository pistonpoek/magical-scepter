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
 * Fireball entity that has modified behavior to be suitable as spell projectile.
 */
public class SpellFireballEntity extends Fireball {
    public SpellFireballEntity(EntityType<? extends SpellFireballEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (this.level() instanceof ServerLevel serverWorld) {
            serverWorld.explode(this, this.getX(), this.getY(), this.getZ(),
                    1.0F, false, Level.ExplosionInteraction.NONE);
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        if (this.level() instanceof ServerLevel serverWorld) {
            Entity entity = entityHitResult.getEntity();
            Entity owner = this.getOwner();
            DamageSource damageSource = this.damageSources().fireball(this, owner);
            entity.hurtServer(serverWorld, damageSource, 6.0F);
            EnchantmentHelper.doPostAttackEffects(serverWorld, entity, damageSource);
        }
    }
}

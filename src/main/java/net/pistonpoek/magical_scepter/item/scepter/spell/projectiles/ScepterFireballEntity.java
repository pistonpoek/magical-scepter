package net.pistonpoek.magical_scepter.item.scepter.spell.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ScepterFireballEntity extends AbstractFireballEntity {
    private final static float EXPLOSION_POWER = 1;

    public ScepterFireballEntity(EntityType<? extends FireballEntity> entityType, World world) {
        super(entityType, world);
    }

    public ScepterFireballEntity(World world, LivingEntity owner, Vec3d velocity) {
        super(EntityType.FIREBALL, owner, velocity, world);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            this.getWorld().createExplosion(this, null, new ScepterFireballExplosionBehavior(),
                    getX(), getY(), getZ(), EXPLOSION_POWER, false, World.ExplosionSourceType.NONE);
            this.discard();
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (this.getWorld().isClient) {
            return;
        }
        Entity entity = entityHitResult.getEntity();
        Entity owner = this.getOwner();
        entity.damage(this.getDamageSources().fireball(this, owner), 6.0f); // Fireball damage is 6.0f
        if (entity instanceof LivingEntity) {
            entity.setOnFireFor(5.0F);
            DamageSource damageSource = this.getDamageSources().fireball(this, owner);
            entity.damage(damageSource, 5.0F);
        }
    }
}

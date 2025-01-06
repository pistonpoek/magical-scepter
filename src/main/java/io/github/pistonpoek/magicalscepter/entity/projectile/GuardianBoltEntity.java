package io.github.pistonpoek.magicalscepter.entity.projectile;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class GuardianBoltEntity extends ExplosiveProjectileEntity {
    private static final TrackedData<Integer> AGE = DataTracker.registerData(GuardianBoltEntity.class,
            TrackedDataHandlerRegistry.INTEGER);
    private static final int DURATION = 12;

    public GuardianBoltEntity(EntityType<GuardianBoltEntity> entityType, World world) {
        super(entityType, world);
    }

    public int getAge() {
        return this.getDataTracker().get(AGE);
    }

    public void setAge(int age) {
        if (!this.getWorld().isClient) {
            this.getDataTracker().set(AGE, Math.max(age, 0));
        }
    }

    @Override
    public void tick() {
        setAge(this.age);
        if (!this.getWorld().isClient &&
                (getAge() > DURATION + 1 || this.getBlockY() > this.getWorld().getTopYInclusive() + 30)) {
            this.discard();
        } else {
            super.tick();
        }
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(AGE, 0);
    }

    @Override
    protected Box calculateDefaultBoundingBox(Vec3d pos) {
        float width = this.getType().getDimensions().width();
        float height = this.getType().getDimensions().height();
        return Box.of(pos, width, height, width);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.age = nbt.getInt("Age");
        setAge(this.age);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Age", this.age);
    }

    @Override
    public void copyFrom(Entity original) {
        super.copyFrom(original);
        if (original instanceof GuardianBoltEntity guardianBoltEntity) {
            setAge(guardianBoltEntity.getAge());
        }
    }

    @Override
    public boolean collidesWith(Entity other) {
        return !(other instanceof GuardianBoltEntity) && super.collidesWith(other);
    }

    @Override
    protected boolean canHit(Entity entity) {
        return !(entity instanceof GuardianBoltEntity) &&
                entity.getType() != EntityType.END_CRYSTAL && super.canHit(entity);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            LivingEntity owner = this.getOwner() instanceof LivingEntity livingEntity ? livingEntity : null;
            Entity entity = entityHitResult.getEntity();
            if (owner != null) {
                owner.onAttacking(entity);
            }

            float progress = getProgress(0.0F);
            float damageMultiplier = (progress * progress);
            float projectileDamage = 6.0F * damageMultiplier;
            float magicDamage = damageMultiplier > 1 ? 3.0F : damageMultiplier == 1 ? 1.0F : 0.0F;

            DamageSource projectileDamageSource = this.getDamageSources().mobProjectile(this, owner);
            DamageSource magicDamageSource = this.getDamageSources().indirectMagic(this, owner);
            if (entity.damage(serverWorld, projectileDamageSource, projectileDamage) && entity instanceof LivingEntity attacked) {
                EnchantmentHelper.onTargetDamaged(serverWorld, attacked, projectileDamageSource);
            }
            if (entity.damage(serverWorld, magicDamageSource, magicDamage) && entity instanceof LivingEntity attacked) {
                EnchantmentHelper.onTargetDamaged(serverWorld, attacked, magicDamageSource);
            }

        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (!this.getWorld().isClient) {
            this.discard();
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            this.discard();
        }
    }

    @Override
    protected boolean isBurning() {
        return false;
    }

    @Override
    protected float getDrag() {
        return 1.0F;
    }

    @Override
    protected float getDragInWater() {
        return getDrag();
    }

    @Nullable
    @Override
    protected ParticleEffect getParticleType() {
        return null;
    }

    public float getProgress(float tickDelta) {
        return (this.getAge() + tickDelta) / DURATION;
    }

    @Override
    public SoundCategory getSoundCategory() {
        if (getOwner() != null) {
            return getOwner().getSoundCategory();
        }
        return super.getSoundCategory();
    }
}

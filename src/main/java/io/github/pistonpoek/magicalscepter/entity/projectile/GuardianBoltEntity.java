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
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Explosive projectile entity that relates to the beam attack of a guardian entity.
 */
public class GuardianBoltEntity extends ExplosiveProjectileEntity {
    private static final TrackedData<Integer> AGE = DataTracker.registerData(GuardianBoltEntity.class,
            TrackedDataHandlerRegistry.INTEGER);
    private static final int DURATION = 12;

    /**
     * Construct the guardian bolt entity in the specified world.
     *
     * @param entityType Entity type that is being created.
     * @param world      World to create the entity in.
     */
    public GuardianBoltEntity(EntityType<? extends GuardianBoltEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Get the current age value for the entity.
     *
     * @return Age value of the entity.
     */
    public int getAge() {
        return this.getDataTracker().get(AGE);
    }

    /**
     * Set the age value for the entity.
     *
     * @param age Age to set for the entity.
     */
    public void setAge(int age) {
        if (!this.getEntityWorld().isClient()) {
            this.getDataTracker().set(AGE, Math.max(age, 0));
        }
    }

    @Override
    public void tick() {
        setAge(this.age);
        if (!this.getEntityWorld().isClient() &&
                (getAge() > DURATION + 1 || this.getBlockY() > this.getEntityWorld().getTopYInclusive() + 30)) {
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
    public void readCustomData(ReadView view) {
        super.readCustomData(view);
        this.age = view.getInt("Age", 0);
        setAge(this.age);
    }

    @Override
    public void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        view.putInt("Age", this.age);
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
        if (this.getEntityWorld() instanceof ServerWorld serverWorld) {
            LivingEntity owner = this.getOwner() instanceof LivingEntity livingEntity ? livingEntity : null;
            Entity entity = entityHitResult.getEntity();
            if (owner != null) {
                owner.onAttacking(entity);
            }

            if (entity instanceof LivingEntity attacked) {
                float progress = getProgress(0.0F);
                float magicDamage = progress >= 1 ? 3.0F : 0.0F;
                float damageMultiplier = Math.clamp(progress * progress, 0.0F, 1.0F);
                float attackDamage = 7.0F * damageMultiplier;

                DamageSource magicDamageSource = this.getDamageSources().indirectMagic(this, owner);
                DamageSource attackDamageSource = this.getDamageSources().mobAttack(owner);

                magicDamage += EnchantmentHelper.getDamage(serverWorld, ItemStack.EMPTY,
                        attacked, attackDamageSource, attackDamage);
                attacked.damage(serverWorld, magicDamageSource, magicDamage);
                EnchantmentHelper.onTargetDamaged(serverWorld, attacked, magicDamageSource);
            }

        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (!this.getEntityWorld().isClient()) {
            this.discard();
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

    /**
     * TODO
     *
     * @param tickDelta
     * @return
     */
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

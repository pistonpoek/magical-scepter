package io.github.pistonpoek.magicalscepter.entity.spell;

import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SpellGuardianBeamEntity extends Entity implements Ownable {
    private static final TrackedData<Integer> OWNER_ID = DataTracker.registerData(SpellGuardianBeamEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TARGET_ID = DataTracker.registerData(SpellGuardianBeamEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> WARMUP_TIME = DataTracker.registerData(SpellGuardianBeamEntity.class, TrackedDataHandlerRegistry.INTEGER);
    @Nullable
    protected LazyEntityReference<LivingEntity> owner;
    @Nullable
    protected LazyEntityReference<Entity> target;
    @Nullable
    private LivingEntity cachedOwner;
    @Nullable
    private Entity cachedTarget;
    private static final int DEFAULT_WARMUP_TIME = 30;
    public static final float MAX_DISTANCE = 15.0F;

    public SpellGuardianBeamEntity(EntityType<? extends SpellGuardianBeamEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(OWNER_ID, 0);
        builder.add(TARGET_ID, 0);
        builder.add(WARMUP_TIME, DEFAULT_WARMUP_TIME);
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity owner = getOwner();
        Entity target = getTarget();
        if (owner == null || target == null) {
            if (!getEntityWorld().isClient()) {
                discard();
            }
            return;
        }

        setPosition(owner.getX(), owner.getEyeY(), owner.getZ());

        if (getEntityWorld().isClient()) {
            clientTick(owner, target);
            return;
        }
        serverTick(owner, target);
    }

    private void clientTick(LivingEntity owner, Entity target) {
        addParticles(owner, target);
    }

    private void addParticles(LivingEntity owner, Entity target) {
        double progress = getProgress(0.0F);
        double deltaX = target.getX() - owner.getX();
        double deltaY = target.getBodyY(0.5) - owner.getEyeY();
        double deltaZ = target.getZ() - owner.getZ();
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
        deltaX /= distance;
        deltaY /= distance;
        deltaZ /= distance;
        double random = this.random.nextDouble();

        while (random < distance) {
            random += 1.8 - progress + this.random.nextDouble() * (1.7 - progress);
            this.getEntityWorld().addParticleClient(ParticleTypes.BUBBLE,
                    owner.getX() + deltaX * random,
                    owner.getEyeY() + deltaY * random,
                    owner.getZ() + deltaZ * random,
                    0.0, 0.0, 0.0);
        }
    }

    private void serverTick(LivingEntity owner, Entity target) {
        if (!owner.canSee(target) || owner.distanceTo(target) > MAX_DISTANCE) {
            discard();
            return;
        }

        if (age > getWarmupTime()) {
            if (getEntityWorld() instanceof ServerWorld serverWorld) {
                target.damage(serverWorld, this.getDamageSources().indirectMagic(owner, owner), 6);
            }
            discard();
        }

        setSilent(owner.isSilent());
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        if (OWNER_ID.equals(data)) {
            this.cachedOwner = null;
        }
        if (TARGET_ID.equals(data)) {
            this.cachedTarget = null;
        }
    }

    @Override
    public void copyFrom(Entity original) {
        super.copyFrom(original);
        if (original instanceof SpellGuardianBeamEntity guardianBeamEntity) {
            setOwner(guardianBeamEntity.getOwner());
            setTarget(guardianBeamEntity.getTarget());
            setWarmupTime(guardianBeamEntity.getWarmupTime());
        }
    }

    @Override
    protected void readCustomData(ReadView view) {
        this.setOwner(LazyEntityReference.fromData(view, "owner"));
        this.setTarget(LazyEntityReference.fromData(view, "target"));
        this.setWarmupTime(view.getInt("warmup_time", DEFAULT_WARMUP_TIME));
    }

    @Override
    protected void writeCustomData(WriteView view) {
        LazyEntityReference.writeData(this.owner, view, "owner");
        LazyEntityReference.writeData(this.target, view, "target");
        view.putInt("warmup_time", this.getWarmupTime());
    }

    public float getProgress(float tickProgress) {
        return (age + tickProgress) / getWarmupTime();
    }

    public int getWarmupTime() {
        return this.dataTracker.get(WARMUP_TIME);
    }

    public void setWarmupTime(int warmupTime) {
        this.dataTracker.set(WARMUP_TIME, warmupTime);
    }

    public boolean hasOwner() {
        return this.dataTracker.get(OWNER_ID) != 0;
    }

    @Nullable
    public LivingEntity getOwner() {
        if (!this.hasOwner()) {
            return null;
        } else if (this.getEntityWorld().isClient()) {
            if (this.cachedOwner != null) {
                return this.cachedOwner;
            } else {
                Entity entity = this.getEntityWorld().getEntityById(this.dataTracker.get(OWNER_ID));
                if (entity instanceof LivingEntity livingEntity) {
                    this.cachedOwner = livingEntity;
                    return this.cachedOwner;
                } else {
                    return null;
                }
            }
        } else {
            return LazyEntityReference.getLivingEntity(this.owner, this.getEntityWorld());
        }
    }

    protected void setOwner(@Nullable LazyEntityReference<LivingEntity> owner) {
        this.owner = owner;
    }

    void setOwnerId(int entityId) {
        this.dataTracker.set(OWNER_ID, entityId);
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.setOwner(LazyEntityReference.of(owner));
        if (owner != null) setOwnerId(owner.getId());
    }

    public boolean hasTarget() {
        return this.dataTracker.get(TARGET_ID) != 0;
    }

    @Nullable
    public Entity getTarget() {
        if (!this.hasTarget()) {
            return null;
        } else if (this.getEntityWorld().isClient()) {
            if (this.cachedTarget != null) {
                return this.cachedTarget;
            } else {
                Entity entity = this.getEntityWorld().getEntityById(this.dataTracker.get(TARGET_ID));
                if (entity != null) {
                    this.cachedTarget = entity;
                    return this.cachedTarget;
                } else {
                    return null;
                }
            }
        } else {
            return LazyEntityReference.getEntity(this.target, this.getEntityWorld());
        }
    }

    protected void setTarget(@Nullable LazyEntityReference<Entity> target) {
        this.target = target;
    }

    void setTargetId(int entityId) {
        this.dataTracker.set(TARGET_ID, entityId);
    }

    public void setTarget(@Nullable Entity target) {
        this.setTarget(LazyEntityReference.of(target));
        if (target != null) setTargetId(target.getId());
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }

    @Override
    public PistonBehavior getPistonBehavior() {
        return PistonBehavior.IGNORE;
    }
}

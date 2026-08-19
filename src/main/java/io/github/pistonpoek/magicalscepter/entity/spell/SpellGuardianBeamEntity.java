package io.github.pistonpoek.magicalscepter.entity.spell;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class SpellGuardianBeamEntity extends Entity implements TraceableEntity {
    private static final EntityDataAccessor<Integer> OWNER_ID = SynchedEntityData.defineId(SpellGuardianBeamEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TARGET_ID = SynchedEntityData.defineId(SpellGuardianBeamEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> WARMUP_TIME = SynchedEntityData.defineId(SpellGuardianBeamEntity.class, EntityDataSerializers.INT);
    @Nullable
    protected EntityReference<LivingEntity> owner;
    @Nullable
    protected EntityReference<Entity> target;
    @Nullable
    private LivingEntity cachedOwner;
    @Nullable
    private Entity cachedTarget;
    private static final int DEFAULT_WARMUP_TIME = 24;
    public static final float MAX_DISTANCE = 15.0F;

    public SpellGuardianBeamEntity(EntityType<? extends SpellGuardianBeamEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(OWNER_ID, 0);
        builder.define(TARGET_ID, 0);
        builder.define(WARMUP_TIME, DEFAULT_WARMUP_TIME);
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity owner = getOwner();
        Entity target = getTarget();
        if (owner == null || target == null) {
            if (!level().isClientSide()) {
                discard();
            }
            return;
        }

        setPos(owner.getX(), owner.getEyeY(), owner.getZ());

        if (level().isClientSide()) {
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
        double deltaY = target.getY(0.5) - owner.getEyeY();
        double deltaZ = target.getZ() - owner.getZ();
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
        deltaX /= distance;
        deltaY /= distance;
        deltaZ /= distance;
        double random = this.random.nextDouble();

        while (random < distance) {
            random += 1.8 - progress + this.random.nextDouble() * (1.7 - progress);
            this.level().addParticle(ParticleTypes.BUBBLE,
                    owner.getX() + deltaX * random,
                    owner.getEyeY() + deltaY * random,
                    owner.getZ() + deltaZ * random,
                    0.0, 0.0, 0.0);
        }
    }

    private void serverTick(LivingEntity owner, Entity target) {
        if (!owner.hasLineOfSight(target) || owner.distanceTo(target) > MAX_DISTANCE) {
            discard();
            return;
        }

        if (tickCount > getWarmupTime()) {
            if (level() instanceof ServerLevel serverWorld) {
                target.hurtServer(serverWorld, this.damageSources().indirectMagic(owner, owner), 6);
            }
            discard();
        }

        setSilent(owner.isSilent());
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        super.onSyncedDataUpdated(data);
        if (OWNER_ID.equals(data)) {
            this.cachedOwner = null;
        }
        if (TARGET_ID.equals(data)) {
            this.cachedTarget = null;
        }
    }

    @Override
    public void restoreFrom(Entity original) {
        super.restoreFrom(original);
        if (original instanceof SpellGuardianBeamEntity guardianBeamEntity) {
            setOwner(guardianBeamEntity.getOwner());
            setTarget(guardianBeamEntity.getTarget());
            setWarmupTime(guardianBeamEntity.getWarmupTime());
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput view) {
        this.setOwner(EntityReference.read(view, "owner"));
        this.setTarget(EntityReference.read(view, "target"));
        this.setWarmupTime(view.getIntOr("warmup_time", DEFAULT_WARMUP_TIME));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput view) {
        EntityReference.store(this.owner, view, "owner");
        EntityReference.store(this.target, view, "target");
        view.putInt("warmup_time", this.getWarmupTime());
    }

    public float getProgress(float tickProgress) {
        return (tickCount + tickProgress) / getWarmupTime();
    }

    public int getWarmupTime() {
        return this.entityData.get(WARMUP_TIME);
    }

    public void setWarmupTime(int warmupTime) {
        this.entityData.set(WARMUP_TIME, warmupTime);
    }

    public boolean hasOwner() {
        return this.entityData.get(OWNER_ID) != 0;
    }

    @Nullable
    public LivingEntity getOwner() {
        if (!this.hasOwner()) {
            return null;
        } else if (this.level().isClientSide()) {
            if (this.cachedOwner != null) {
                return this.cachedOwner;
            } else {
                Entity entity = this.level().getEntity(this.entityData.get(OWNER_ID));
                if (entity instanceof LivingEntity livingEntity) {
                    this.cachedOwner = livingEntity;
                    return this.cachedOwner;
                } else {
                    return null;
                }
            }
        } else {
            return EntityReference.getLivingEntity(this.owner, this.level());
        }
    }

    protected void setOwner(@Nullable EntityReference<LivingEntity> owner) {
        this.owner = owner;
    }

    void setOwnerId(int entityId) {
        this.entityData.set(OWNER_ID, entityId);
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.setOwner(EntityReference.of(owner));
        if (owner != null) setOwnerId(owner.getId());
    }

    public boolean hasTarget() {
        return this.entityData.get(TARGET_ID) != 0;
    }

    @Nullable
    public Entity getTarget() {
        if (!this.hasTarget()) {
            return null;
        } else if (this.level().isClientSide()) {
            if (this.cachedTarget != null) {
                return this.cachedTarget;
            } else {
                Entity entity = this.level().getEntity(this.entityData.get(TARGET_ID));
                if (entity != null) {
                    this.cachedTarget = entity;
                    return this.cachedTarget;
                } else {
                    return null;
                }
            }
        } else {
            return EntityReference.getEntity(this.target, this.level());
        }
    }

    protected void setTarget(@Nullable EntityReference<Entity> target) {
        this.target = target;
    }

    void setTargetId(int entityId) {
        this.entityData.set(TARGET_ID, entityId);
    }

    public void setTarget(@Nullable Entity target) {
        this.setTarget(EntityReference.of(target));
        if (target != null) setTargetId(target.getId());
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean hurtServer(ServerLevel world, DamageSource source, float amount) {
        return false;
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }
}

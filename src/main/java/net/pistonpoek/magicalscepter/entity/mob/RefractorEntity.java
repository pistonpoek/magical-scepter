package net.pistonpoek.magicalscepter.entity.mob;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.pistonpoek.magicalscepter.entity.ai.goal.RefillScepterGoal;
import net.pistonpoek.magicalscepter.entity.ai.goal.ScepterAttackGoal;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.pistonpoek.magicalscepter.scepter.Scepter;
import net.pistonpoek.magicalscepter.scepter.ScepterHelper;
import net.pistonpoek.magicalscepter.scepter.Scepters;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class RefractorEntity extends IllagerEntity {

    public RefractorEntity(EntityType<? extends RefractorEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 5;
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(4, new RefillScepterGoal<>(this));
        this.goalSelector.add(5, new ScepterAttackGoal<>(this, 0.5, 50, 15.0F));
        this.goalSelector.add(8, new WanderAroundGoal(this, 0.6));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.add(1, new RevengeGoal(this, RaiderEntity.class).setGroupRevenge());
        this.targetSelector.add(2, new ActiveTargetGoal<PlayerEntity>(this, PlayerEntity.class, true).setMaxTimeWithoutVisibility(300));
        this.targetSelector.add(3, new ActiveTargetGoal<MerchantEntity>(this, MerchantEntity.class, false).setMaxTimeWithoutVisibility(300));
        this.targetSelector.add(3, new ActiveTargetGoal<IronGolemEntity>(this, IronGolemEntity.class, false).setMaxTimeWithoutVisibility(300));
    }

    public static DefaultAttributeContainer.Builder createRefractorAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 18.0)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 32.0);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty,
                                 SpawnReason spawnReason, @Nullable EntityData entityData) {
        RegistryEntry<Scepter> defaultScepter = world.getRegistryManager().get(ModRegistryKeys.SCEPTER)
                .getEntry(Scepters.DEFAULT_KEY).orElseThrow();
        this.equipStack(EquipmentSlot.MAINHAND, ScepterHelper.createScepter(defaultScepter));
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
    }

    @Override
    public SoundEvent getCelebratingSound() {
        return SoundEvents.ENTITY_ILLUSIONER_AMBIENT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ILLUSIONER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ILLUSIONER_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_ILLUSIONER_HURT;
    }

    @Override
    public void addBonusForWave(ServerWorld world, int wave, boolean unused) {
    }

    @Override
    public RefractorEntity.State getState() {
        return this.isAttacking() ? IllagerEntity.State.NEUTRAL : IllagerEntity.State.CROSSED;
    }

    @Override
    public boolean canLead() {
        return false;
    }

    protected class LookAtTargetGoal extends Goal {
        public LookAtTargetGoal() {
            this.setControls(EnumSet.of(Goal.Control.LOOK));
        }

        @Override
        public boolean canStart() {
            return RefractorEntity.this.isAttacking();
        }

        @Override
        public void start() {
            super.start();
        }

        @Override
        public void stop() {
            super.stop();
        }

        @Override
        public void tick() {
            if (RefractorEntity.this.getTarget() != null) {
                RefractorEntity.this.getLookControl()
                        .lookAt(
                                RefractorEntity.this.getTarget(),
                                (float)RefractorEntity.this.getMaxHeadRotation(),
                                (float)RefractorEntity.this.getMaxLookPitchChange()
                        );
            }
        }
    }
}

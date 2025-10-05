package io.github.pistonpoek.magicalscepter.entity.mob;

import io.github.pistonpoek.magicalscepter.entity.ai.goal.ScepterAttackGoal;
import io.github.pistonpoek.magicalscepter.entity.ai.goal.ScepterRefillGoal;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.registry.tag.ModItemTags;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import io.github.pistonpoek.magicalscepter.sound.ModSoundEvents;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreakingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Illager entity that wields a magical scepter.
 */
public class SorcererEntity extends IllagerEntity {
    /**
     * Construct the sorcerer entity in the specified world.
     *
     * @param entityType Entity type that is being created.
     * @param world      World to create the entity in.
     */
    public SorcererEntity(EntityType<? extends SorcererEntity> entityType, World world) {
        super(entityType, world);
        this.setCanPickUpLoot(true);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(3, new FleeEntityGoal<>(this, CreakingEntity.class, 8.0F, 1.0, 1.2));
        this.goalSelector.add(4, new ScepterRefillGoal<>(this));
        this.goalSelector.add(6, new ScepterAttackGoal<>(this, 0.5, 40, 8.0F));
        this.goalSelector.add(8, new WanderAroundGoal(this, 0.6));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.add(1, new RevengeGoal(this, RaiderEntity.class).setGroupRevenge());
        this.targetSelector.add(2, new ActiveTargetGoal<PlayerEntity>(this, PlayerEntity.class, true).setMaxTimeWithoutVisibility(300));
        this.targetSelector.add(3, new ActiveTargetGoal<MerchantEntity>(this, MerchantEntity.class, false).setMaxTimeWithoutVisibility(300));
        this.targetSelector.add(3, new ActiveTargetGoal<IronGolemEntity>(this, IronGolemEntity.class, false).setMaxTimeWithoutVisibility(300));
    }

    /**
     * Create the default attribute container builder for the sorcerer entity.
     *
     * @return Attribute builder for the sorcerer entity with base values.
     */
    public static DefaultAttributeContainer.Builder createSorcererAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.MOVEMENT_SPEED, 0.5)
                .add(EntityAttributes.FOLLOW_RANGE, 16.0)
                .add(EntityAttributes.MAX_HEALTH, 24.0);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty,
                                 SpawnReason spawnReason, @Nullable EntityData entityData) {
        Random random = world.getRandom();
        this.initEquipment(random, difficulty);
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected void initEquipment(Random random, LocalDifficulty localDifficulty) {
        Registry<Scepter> scepterRegistry = this.getRegistryManager().getOrThrow(ModRegistryKeys.SCEPTER);
        RegistryEntry<Scepter> magicalScepter = scepterRegistry.getOrThrow(Scepters.MAGICAL_KEY);
        this.equipStack(EquipmentSlot.MAINHAND, ScepterHelper.createMagicalScepter(magicalScepter));
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
    }

    @Override
    public SoundEvent getCelebratingSound() {
        return ModSoundEvents.ENTITY_SORCERER_CELEBRATE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.ENTITY_SORCERER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.ENTITY_SORCERER_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSoundEvents.ENTITY_SORCERER_HURT;
    }

    @Override
    public void addBonusForWave(ServerWorld world, int wave, boolean unused) {
    }

    @Override
    public TagKey<Item> getPreferredWeapons() {
        return ModItemTags.SORCERER_PREFERRED_WEAPONS;
    }

    @Override
    public SorcererEntity.State getState() {
        return this.isAttacking() ? IllagerEntity.State.NEUTRAL : IllagerEntity.State.CROSSED;
    }
}

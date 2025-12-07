package io.github.pistonpoek.magicalscepter.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.pistonpoek.magicalscepter.entity.ModEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * TODO
 */
@Mixin(Raid.class)
public abstract class RaidMixin {
    @Final
    @Shadow
    private Random random;

    @Shadow
    protected abstract boolean isSpawningExtraWave();

    @Unique
    private ServerWorld magicalscepter$world;
    @Unique
    private int magicalscepter$wave = 0;
    @Unique
    private int magicalscepter$count = 0;
    @Unique
    private RaiderEntity magicalscepter$raiderEntity = null;

    /**
     * Capture local variables at the entity creation during the spawning of a next wave.
     *
     * @param world        Server world to create entity in.
     * @param pos          Block position to spawn entity at.
     * @param callbackInfo Callback info to return values to the entity creation.
     * @param wave         Integer that is the current wave indicator.
     * @param count        Integer count of the amount of entities to spawn for the current type.
     */
    @Inject(
            method = "spawnNextWave",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/world/World;Lnet/minecraft/entity/SpawnReason;)Lnet/minecraft/entity/Entity;",
                    ordinal = 0
            )
    )
    private void captureLocalVariables(ServerWorld world,
                                       BlockPos pos, CallbackInfo callbackInfo,
                                       @Local(ordinal = 0) int wave, @Local(ordinal = 5) int count) {
        this.magicalscepter$world = world;
        this.magicalscepter$wave = wave;
        this.magicalscepter$count = count;
    }

    /**
     * Create a raider entity based on the current raider entity being created.
     *
     * @param instance Entity type of the raider entity currently being created.
     * @param world World to create the raider entity in.
     * @param reason Spawn reason for the raider entity.
     * @param <T> Type of raider entity currently being created.
     * @return Entity created based on the current raider entity being created.
     */
    @Redirect(
            method = "spawnNextWave",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/world/World;Lnet/minecraft/entity/SpawnReason;)Lnet/minecraft/entity/Entity;",
                    ordinal = 0
            )
    )
    private <T extends Entity> T createRaiderEntity(EntityType<T> instance, World world, SpawnReason reason) {
        int wave = this.magicalscepter$wave;
        int count = this.magicalscepter$count;
        if (instance.equals(EntityType.PILLAGER)) {
            if (wave == 4 && !this.isSpawningExtraWave() && count == 0) {
                magicalscepter$setOptionalSorcererRaiderEntity(world, reason);
                return null;
            } else if (wave >= 5 && count == 0) {
                magicalscepter$setSorcererRaiderEntity(world, reason);
                return null;
            }
        }
        if (instance.equals(EntityType.VINDICATOR) && wave >= 5 && count == 0) {
            magicalscepter$setOptionalSorcererRaiderEntity(world, reason);
            return null;
        }
        this.magicalscepter$raiderEntity = null;
        return instance.create(world, reason);
    }

    /**
     * Create a sorcerer raider entity with a 50% chance.
     *
     * @param world World to create raider entity in.
     * @param reason Spawn reason to create raider entity with.
     */
    @Unique
    private void magicalscepter$setOptionalSorcererRaiderEntity(World world, SpawnReason reason) {
        if (this.random.nextBoolean()) {
            magicalscepter$setSorcererRaiderEntity(world, reason);
        }
    }

    /**
     * Create a sorcerer raider entity.
     *
     * @param world World to create raider entity in.
     * @param reason Spawn reason to create raider entity with.
     */
    @Unique
    private void magicalscepter$setSorcererRaiderEntity(World world, SpawnReason reason) {
        this.magicalscepter$raiderEntity = ModEntityType.SORCERER.create(world, reason);
    }

    /**
     * Replace the current raider entity with the raider entity of this class, if it exists.
     *
     * @param raiderEntity Raider entity to update with created raider entity.
     * @return Raider entity that is updated with a new raider entity, if it exists.
     */
    @ModifyVariable(
            method = "spawnNextWave",
            at = @At(
                    value = "STORE"
            ),
            ordinal = 0
    )
    private RaiderEntity createSorcererRaiderEntity(RaiderEntity raiderEntity) {
        RaiderEntity newRaiderEntity = this.magicalscepter$raiderEntity;
        if (newRaiderEntity != null) {
            this.magicalscepter$raiderEntity = null;
            return newRaiderEntity;
        }
        return raiderEntity;
    }

    /**
     * Get the ravager passenger based on the current raider entity that is set to be passenger.
     *
     * @param raiderEntity Raider entity set to be the ravager passenger.
     * @return Raider entity to be the ravager passenger.
     */
    @ModifyVariable(
            method = "spawnNextWave",
            at = @At(
                    value = "STORE"
            ),
            ordinal = 1
    )
    private RaiderEntity getRavagerPassenger(RaiderEntity raiderEntity) {
        int wave = this.magicalscepter$wave;
        int count = this.magicalscepter$count;
        ServerWorld world = magicalscepter$world;

        if (wave <= 5) {
            return EntityType.PILLAGER.create(world, SpawnReason.EVENT);
        } else if (wave == 6) {
            if (count == 0) {
                return ModEntityType.SORCERER.create(world, SpawnReason.EVENT);
            } else {
                return null;
            }
        } else {
            return switch (count) {
                case 0 -> ModEntityType.SORCERER.create(world, SpawnReason.EVENT);
                case 1 -> EntityType.EVOKER.create(world, SpawnReason.EVENT);
                default -> EntityType.VINDICATOR.create(world, SpawnReason.EVENT);
            };
        }
    }

    /**
     * Prevent ravager passenger assignment by making get max waves return the value it is being compared against.
     *
     * @param maxWaves Current max waves value to override.
     * @return Integer value being compared against to prevent further ravager assignment.
     */
    @ModifyExpressionValue(
            method = "spawnNextWave",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/village/raid/Raid;getMaxWaves(Lnet/minecraft/world/Difficulty;)I"
            )
    )
    private int preventRavagerPassengerAssignment(int maxWaves) {
        // Prevents if and if else from being true, so raider entity does not get reassigned.
        return this.magicalscepter$wave + 1;
    }
}

package io.github.pistonpoek.magicalscepter.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.pistonpoek.magicalscepter.entity.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Raid.class)
public abstract class RaidMixin {
    @Final
    @Shadow
    private RandomSource random;

    @Shadow
    protected abstract boolean shouldSpawnBonusGroup();

    @Unique
    private ServerLevel magicalscepter$world;
    @Unique
    private int magicalscepter$wave = 0;
    @Unique
    private int magicalscepter$count = 0;
    @Unique
    private Raider magicalscepter$raiderEntity = null;

    /**
     * Capture local variables at the entity creation during the spawning of a next wave.
     *
     * @param level        Server world to create entity in.
     * @param pos          Block position to spawn entity at.
     * @param callbackInfo Callback info to return values to the entity creation.
     * @param groupNumber         Integer that is the current wave indicator.
     * @param i        Integer count of the amount of entities to spawn for the current type.
     */
    @Inject(
            method = "spawnGroup",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/EntityType;create(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/EntitySpawnReason;)Lnet/minecraft/world/entity/Entity;",
                    ordinal = 0
            )
    )
    private void captureLocalVariables(ServerLevel level,
                                       BlockPos pos, CallbackInfo callbackInfo,
                                       @Local(name = "groupNumber") int groupNumber, @Local(name = "i") int i) {
        this.magicalscepter$world = level;
        this.magicalscepter$wave = groupNumber;
        this.magicalscepter$count = i;
    }

    /**
     * Create a raider entity based on the current raider entity being created.
     *
     * @param instance Entity type of the raider entity currently being created.
     * @param level World to create the raider entity in.
     * @param reason Spawn reason for the raider entity.
     * @param <T> Type of raider entity currently being created.
     * @return Entity created based on the current raider entity being created.
     */
    @Redirect(
            method = "spawnGroup",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/EntityType;create(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/EntitySpawnReason;)Lnet/minecraft/world/entity/Entity;",
                    ordinal = 0
            )
    )
    private <T extends Entity> T createRaiderEntity(EntityType<T> instance, Level level, EntitySpawnReason reason) {
        int wave = this.magicalscepter$wave;
        int count = this.magicalscepter$count;
        if (instance.equals(EntityTypes.PILLAGER)) {
            if (wave == 4 && !this.shouldSpawnBonusGroup() && count == 0) {
                magicalscepter$setOptionalSorcererRaiderEntity(level, reason);
                return null;
            } else if (wave >= 5 && count == 0) {
                magicalscepter$setSorcererRaiderEntity(level, reason);
                return null;
            }
        }
        if (instance.equals(EntityTypes.VINDICATOR) && wave >= 5 && count == 0) {
            magicalscepter$setOptionalSorcererRaiderEntity(level, reason);
            return null;
        }
        this.magicalscepter$raiderEntity = null;
        return instance.create(level, reason);
    }

    /**
     * Create a sorcerer raider entity with a 50% chance.
     *
     * @param world World to create raider entity in.
     * @param reason Spawn reason to create raider entity with.
     */
    @Unique
    private void magicalscepter$setOptionalSorcererRaiderEntity(Level world, EntitySpawnReason reason) {
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
    private void magicalscepter$setSorcererRaiderEntity(Level world, EntitySpawnReason reason) {
        this.magicalscepter$raiderEntity = ModEntityTypes.SORCERER.create(world, reason);
    }

    /**
     * Replace the current raider entity with the raider entity of this class, if it exists.
     *
     * @param raider Raider entity to update with created raider entity.
     * @return Raider entity that is updated with a new raider entity, if it exists.
     */
    @ModifyVariable(
            method = "spawnGroup",
            at = @At(
                    value = "STORE"
            ),
            ordinal = 0
    )
    private Raider createSorcererRaiderEntity(Raider raider) {
        Raider newRaiderEntity = this.magicalscepter$raiderEntity;
        if (newRaiderEntity != null) {
            this.magicalscepter$raiderEntity = null;
            return newRaiderEntity;
        }
        return raider;
    }

    /**
     * Get the ravager passenger based on the current raider entity that is set to be passenger.
     *
     * @param ridingRaider Raider entity set to be the ravager passenger.
     * @return Raider entity to be the ravager passenger.
     */
    @ModifyVariable(
            method = "spawnGroup",
            at = @At(
                    value = "STORE"
            ),
            ordinal = 1
    )
    private Raider getRavagerPassenger(Raider ridingRaider) {
        int wave = this.magicalscepter$wave;
        int count = this.magicalscepter$count;
        ServerLevel world = magicalscepter$world;

        if (wave <= 5) {
            return EntityTypes.PILLAGER.create(world, EntitySpawnReason.EVENT);
        } else if (wave == 6) {
            if (count == 0) {
                return ModEntityTypes.SORCERER.create(world, EntitySpawnReason.EVENT);
            } else {
                return null;
            }
        } else {
            return switch (count) {
                case 0 -> ModEntityTypes.SORCERER.create(world, EntitySpawnReason.EVENT);
                case 1 -> EntityTypes.EVOKER.create(world, EntitySpawnReason.EVENT);
                default -> EntityTypes.VINDICATOR.create(world, EntitySpawnReason.EVENT);
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
            method = "spawnGroup",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/raid/Raid;getNumGroups(Lnet/minecraft/world/Difficulty;)I"
            )
    )
    private int preventRavagerPassengerAssignment(int maxWaves) {
        // Prevents if and if else from being true, so raider entity does not get reassigned.
        return this.magicalscepter$wave + 1;
    }
}

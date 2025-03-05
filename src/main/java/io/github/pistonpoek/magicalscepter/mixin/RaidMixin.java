package io.github.pistonpoek.magicalscepter.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.pistonpoek.magicalscepter.MagicalScepter;
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
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Raid.class)
public abstract class RaidMixin {
    @Final
    @Shadow
    private ServerWorld world;

    @Final
    @Shadow
    private Random random;

    @Shadow protected abstract boolean isSpawningExtraWave();

    @Unique
    private int magicalscepter$wave = 0;
    @Unique
    private int magicalscepter$count = 0;
    @Unique
    private RaiderEntity magicalscepter$raiderEntity = null;

    @Inject(
            method = "spawnNextWave(Lnet/minecraft/util/math/BlockPos;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/world/World;Lnet/minecraft/entity/SpawnReason;)Lnet/minecraft/entity/Entity;",
                    ordinal = 0
            )
    )
    private void captureLocalVariables(BlockPos pos, CallbackInfo callbackInfo,
                                       @Local(ordinal = 0) int wave, @Local(ordinal = 5) int count) {
        this.magicalscepter$wave = wave;
        this.magicalscepter$count = count;
    }


    @Redirect(
            method = "spawnNextWave(Lnet/minecraft/util/math/BlockPos;)V",
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
                magicalscepter$setOptionalRefractorRaiderEntity(world, reason);
                return null;
            } else if (wave >= 5 && count == 0) {
                magicalscepter$setRefractorRaiderEntity(world, reason);
                return null;
            }
        }
        if (instance.equals(EntityType.VINDICATOR) && wave >= 5 && count == 0) {
            magicalscepter$setOptionalRefractorRaiderEntity(world, reason);
            return null;
        }
        this.magicalscepter$raiderEntity = null;
        return instance.create(world, reason);
    }

    @Unique
    private void magicalscepter$setOptionalRefractorRaiderEntity(World world, SpawnReason reason) {
        if (this.random.nextBoolean()) {
            magicalscepter$setRefractorRaiderEntity(world, reason);
        }
    }

    @Unique
    private void magicalscepter$setRefractorRaiderEntity(World world, SpawnReason reason) {
        this.magicalscepter$raiderEntity = ModEntityType.REFRACTOR.create(world, reason);
    }

    @ModifyVariable(
            method = "spawnNextWave(Lnet/minecraft/util/math/BlockPos;)V",
            at = @At(
                    value = "STORE"
            ),
            ordinal = 0
    )
    private RaiderEntity createRefractorRaiderEntity(RaiderEntity raiderEntity) {
        RaiderEntity newRaiderEntity = this.magicalscepter$raiderEntity;
        if (newRaiderEntity != null) {
            this.magicalscepter$raiderEntity = null;
            return newRaiderEntity;
        }
        return raiderEntity;
    }

    @ModifyVariable(
            method = "spawnNextWave(Lnet/minecraft/util/math/BlockPos;)V",
            at = @At(
                    value = "STORE"
            ),
            ordinal = 1
    )
    private RaiderEntity getRavagerPassenger(RaiderEntity raiderEntity) {
        int wave = this.magicalscepter$wave;
        int count = this.magicalscepter$count;

        if (wave <= 5) {
            return EntityType.PILLAGER.create(this.world, SpawnReason.EVENT);
        } else if (wave == 6) {
            if (count == 0) {
                return ModEntityType.REFRACTOR.create(this.world, SpawnReason.EVENT);
            } else {
                return null;
            }
        } else {
            return switch (count) {
                case 0 -> ModEntityType.REFRACTOR.create(this.world, SpawnReason.EVENT);
                case 1 -> EntityType.EVOKER.create(this.world, SpawnReason.EVENT);
                default -> EntityType.VINDICATOR.create(this.world, SpawnReason.EVENT);
            };
        }
    }

    @ModifyExpressionValue(
            method = "spawnNextWave(Lnet/minecraft/util/math/BlockPos;)V",
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

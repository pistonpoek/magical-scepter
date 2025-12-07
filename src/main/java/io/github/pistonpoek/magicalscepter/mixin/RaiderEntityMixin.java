package io.github.pistonpoek.magicalscepter.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.pistonpoek.magicalscepter.entity.ModEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * TODO
 */
@Mixin(RaiderEntity.class)
public abstract class RaiderEntityMixin extends PatrolEntity {
    /**
     * Constructs a raider entity mixin to match the patrol entity constructor.
     *
     * @param type Entity type to create the raider entity mixin with.
     * @param world World to create the raider entity mixin in.
     */
    protected RaiderEntityMixin(EntityType<? extends PatrolEntity> type, World world) {
        super(type, world);
    }

    /**
     * Modify the able to join raid value to exclude naturally spawning sorcerers.
     *
     * @param ableToJoinRaid Truth assignment, if originally the raider entity is able to join a raid.
     * @param spawnReason Spawn reason for the raider entity.
     * @return Truth assignment, if originally able to join the raid excluding naturally spawned sorcerers.
     */
    @ModifyArg(method = "initialize",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/raid/RaiderEntity;setAbleToJoinRaid(Z)V"))
    public boolean modifyAbleToJoinRaid(boolean ableToJoinRaid, @Local(argsOnly = true) SpawnReason spawnReason) {
        return ableToJoinRaid && (this.getType() != ModEntityType.SORCERER && spawnReason != SpawnReason.NATURAL);
    }
}

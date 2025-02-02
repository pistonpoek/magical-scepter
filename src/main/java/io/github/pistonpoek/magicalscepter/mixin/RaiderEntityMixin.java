package io.github.pistonpoek.magicalscepter.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(RaiderEntity.class)
public abstract class RaiderEntityMixin extends PatrolEntity {
    protected RaiderEntityMixin(EntityType<? extends PatrolEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyArg(method = "initialize",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/raid/RaiderEntity;setAbleToJoinRaid(Z)V"))
    public boolean modifyAbleToJoinRaid(boolean ableToJoinRaid, @Local(argsOnly = true) SpawnReason spawnReason) {
        return ableToJoinRaid && spawnReason != SpawnReason.STRUCTURE && spawnReason != SpawnReason.NATURAL;
    }
}

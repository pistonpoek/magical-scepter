package io.github.pistonpoek.magicalscepter.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.pistonpoek.magicalscepter.entity.ModEntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Raider.class)
public abstract class RaiderEntityMixin extends PatrollingMonster {
    /**
     * Constructs a raider entity mixin to match the patrol entity constructor.
     *
     * @param type Entity type to create the raider entity mixin with.
     * @param world World to create the raider entity mixin in.
     */
    protected RaiderEntityMixin(EntityType<? extends PatrollingMonster> type, Level world) {
        super(type, world);
    }

    /**
     * Modify the able to join raid value to exclude naturally spawning sorcerers.
     *
     * @param ableToJoinRaid Truth assignment, if originally the raider entity is able to join a raid.
     * @param spawnReason Spawn reason for the raider entity.
     * @return Truth assignment, if originally able to join the raid excluding naturally spawned sorcerers.
     */
    @ModifyArg(method = "finalizeSpawn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/raid/Raider;setCanJoinRaid(Z)V"))
    public boolean modifyAbleToJoinRaid(boolean ableToJoinRaid, @Local(argsOnly = true) EntitySpawnReason spawnReason) {
        return ableToJoinRaid && (this.getType() != ModEntityType.SORCERER && spawnReason != EntitySpawnReason.NATURAL);
    }
}

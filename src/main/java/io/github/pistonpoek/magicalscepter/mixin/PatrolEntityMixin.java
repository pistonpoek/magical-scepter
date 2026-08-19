package io.github.pistonpoek.magicalscepter.mixin;

import io.github.pistonpoek.magicalscepter.entity.ModEntityTypes;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PatrollingMonster.class)
public abstract class PatrolEntityMixin extends Monster {
    @Shadow
    private boolean patrolLeader;

    /**
     * Constructs a patrol entity mixin to match the hostile entity constructor.
     *
     * @param type Entity type to create the patrol entity mixin with.
     * @param world World to create the patrol entity mixin in.
     */
    protected PatrolEntityMixin(EntityType<? extends Monster> type, Level world) {
        super(type, world);
    }

    /**
     * Modify the patrol leader value at the initialize method
     * to be false when a sorcerer spawns naturally in the world.
     *
     * @param world World the patrol entity is being initialized in.
     * @param difficulty Local difficulty at the initialization place.
     * @param spawnReason Spawn reason to initialize with.
     * @param entityData Entity data to initialize with.
     * @param callbackInfoReturnable Callback into returnable to return a different value of the initialize method.
     */
    @Inject(method = "finalizeSpawn", at = @At(value = "FIELD",
            target = "Lnet/minecraft/world/entity/monster/PatrollingMonster;patrolLeader:Z",
            opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    private void modifyPatrolLeader(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason,
                                    SpawnGroupData entityData, CallbackInfoReturnable<SpawnGroupData> callbackInfoReturnable) {
        this.patrolLeader = this.patrolLeader && (this.getType() != (ModEntityTypes.SORCERER) ||
                spawnReason != EntitySpawnReason.NATURAL);
    }
}

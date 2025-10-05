package io.github.pistonpoek.magicalscepter.mixin;

import io.github.pistonpoek.magicalscepter.entity.ModEntityType;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PatrolEntity.class)
public abstract class PatrolEntityMixin extends HostileEntity {
    @Shadow
    private boolean patrolLeader;

    protected PatrolEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initialize", at = @At(value = "FIELD",
            target = "Lnet/minecraft/entity/mob/PatrolEntity;patrolLeader:Z",
            opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    private void modifyPatrolLeader(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
                                    EntityData entityData, CallbackInfoReturnable<EntityData> callbackInfoReturnable) {
        this.patrolLeader = this.patrolLeader && (this.getType() != (ModEntityType.SORCERER) ||
                spawnReason != SpawnReason.NATURAL);
    }
}

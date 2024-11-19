package net.pistonpoek.magicalscepter.mixin;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.pistonpoek.magicalscepter.item.SwingHandLivingEntity;
import net.pistonpoek.magicalscepter.item.SwingType;
import net.pistonpoek.magicalscepter.network.packet.SwingHandPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements SwingHandLivingEntity {
    @Shadow public boolean handSwinging;
    @Shadow public int handSwingTicks;
    @Shadow abstract int getHandSwingDuration();
    @Shadow public Hand preferredHand;
    public SwingType swingType = SwingType.HIT;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method="swingHand(Lnet/minecraft/util/Hand;Z)V", at=@At("HEAD"))
    public void swingHand(Hand hand, boolean fromServerPlayer, CallbackInfo callbackInfo) {
        if (this.handSwinging && this.handSwingTicks < this.getHandSwingDuration() / 2 && this.handSwingTicks >= 0) {
            return;
        }
        setSwingType(SwingType.HIT);
    }

    @Override
    public void swingHand(Hand hand, SwingType swingType) {
        if (this.handSwinging && this.handSwingTicks < this.getHandSwingDuration() / 2 && this.handSwingTicks >= 0) {
            return;
        }
        this.handSwingTicks = -1;
        this.handSwinging = true;
        this.preferredHand = hand;
        this.swingType = swingType;

        if (this.getWorld() instanceof ServerWorld) {
            SwingHandPayload swingHandPayload = new SwingHandPayload(this.getId(), hand, swingType);
            for (ServerPlayerEntity player : PlayerLookup.tracking(this)) {
                ServerPlayNetworking.send(player, swingHandPayload);
            }
        }
    }

    @Override
    public SwingType getSwingType() {
        return swingType;
    }

    @Override
    public void setSwingType(SwingType swingType) {
        this.swingType = swingType;
    }
}

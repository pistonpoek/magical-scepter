package io.github.pistonpoek.magicalscepter.mixin;

import io.github.pistonpoek.magicalscepter.item.SwingHandLivingEntity;
import io.github.pistonpoek.magicalscepter.item.SwingType;
import io.github.pistonpoek.magicalscepter.network.packet.SwingHandPayload;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements SwingHandLivingEntity {
    @Shadow
    public boolean swinging;
    @Shadow
    public int swingTime;

    @Shadow
    protected abstract int getCurrentSwingDuration();

    @Shadow
    public InteractionHand swingingArm;

    @Unique
    public SwingType magicalscepter$swingType = SwingType.HIT;

    /**
     * Constructs a living entity mixin to match the entity constructor.
     *
     * @param type Entity type to create the living entity mixin with.
     * @param world World to create the living entity mixin in.
     */
    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    /**
     * Update the current swing type to a basic hit when the swing hand method is called.
     *
     * @param hand Hand that is triggered to swing.
     * @param fromServerPlayer Truth assignment, if the player is server side.
     * @param callbackInfo Callback info to return values back to the swing hand method.
     */
    @Inject(method = "swing(Lnet/minecraft/world/InteractionHand;Z)V", at = @At("HEAD"))
    public void updateSwingType(InteractionHand hand, boolean fromServerPlayer, CallbackInfo callbackInfo) {
        if (this.swinging && this.swingTime < this.getCurrentSwingDuration() / 2 && this.swingTime >= 0) {
            return;
        }
        magical_scepter$setSwingType(SwingType.HIT);
    }

    @Override
    public void magical_scepter$swingHand(InteractionHand hand, SwingType swingType) {
        if (this.swinging && this.swingTime < this.getCurrentSwingDuration() / 2 && this.swingTime >= 0) {
            return;
        }
        this.swingTime = -1;
        this.swinging = true;
        this.swingingArm = hand;
        magical_scepter$setSwingType(swingType);

        if (this.level() instanceof ServerLevel) {
            SwingHandPayload swingHandPayload = new SwingHandPayload(this.getId(), hand, swingType);
            for (ServerPlayer player : PlayerLookup.tracking(this)) {
                ServerPlayNetworking.send(player, swingHandPayload);
            }
        }
    }

    @Override
    public SwingType magical_scepter$getSwingType() {
        return magicalscepter$swingType;
    }

    @Override
    public void magical_scepter$setSwingType(SwingType swingType) {
        this.magicalscepter$swingType = swingType;
    }
}

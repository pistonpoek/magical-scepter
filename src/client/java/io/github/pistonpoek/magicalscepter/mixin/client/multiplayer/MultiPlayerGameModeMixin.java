package io.github.pistonpoek.magicalscepter.mixin.client.multiplayer;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.InteractionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {
    @ModifyExpressionValue(
            method = "performUseItemOn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;useWithoutItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;")
    )
    public InteractionResult updateResultWithEmptyItemContext(InteractionResult original) {
        if (original instanceof InteractionResult.Success success) {
            return success.withoutItem();
        }
        return original;
    }
}

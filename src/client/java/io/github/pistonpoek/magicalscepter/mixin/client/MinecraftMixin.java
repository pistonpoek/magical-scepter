package io.github.pistonpoek.magicalscepter.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SwingAnimation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @WrapOperation(
        method = "startUseItem",
        at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/player/LocalPlayer;swing(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/component/SwingAnimation;Z)Z"
        )
    )
    private boolean useEmptyHandSwingAnimation(
            LocalPlayer instance,
            InteractionHand interactionHand,
            SwingAnimation swingAnimation,
            boolean sendToSwingingEntity,
            Operation<Boolean> original,
            @Local(name = "success") InteractionResult.Success success
    ) {
        if (!success.itemContext().wasItemInteraction()) {
            swingAnimation = ItemStack.EMPTY.getInteractAnimation();
        }

        return original.call(instance, interactionHand, swingAnimation, sendToSwingingEntity);
    }
}

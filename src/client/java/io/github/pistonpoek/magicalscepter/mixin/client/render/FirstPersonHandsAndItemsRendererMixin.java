package io.github.pistonpoek.magicalscepter.mixin.client.render;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.pistonpoek.magicalscepter.model.effects.ScepterAnimations;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwingAnimationType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public class FirstPersonHandsAndItemsRendererMixin {
    @Inject(
            method = "submitArmWithItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/component/SwingAnimation;type()Lnet/minecraft/world/item/SwingAnimationType;"
            )
    )
    private void addSwingAnimationTypeToSwitch(
            PlayerRenderState playerState,
            FirstPersonHandsAndItemsRenderState state,
            float partialTicks,
            float xRot,
            InteractionHand hand,
            float attack,
            ItemStack itemStack,
            float inverseArmHeight,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int lightCoords,
            CallbackInfo callbackInfo,
            @Local(name = "arm") HumanoidArm arm,
            @Local(name = "invert") int invert,
            @Local(name = "currentSwing") LivingEntity.SwingDescription currentSwing
    ) {
        assert currentSwing != null && hand == currentSwing.hand();
        var animationType = currentSwing.animation().type();

        if (animationType == SwingAnimationType.MAGICALSCEPTER_SWIRL) {
            ScepterAnimations.firstPersonSwing(
                    attack,
                    poseStack,
                    invert,
                    arm
            );
        }
    }
}

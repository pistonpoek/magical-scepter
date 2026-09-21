package io.github.pistonpoek.magicalscepter.mixin.client.render.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.pistonpoek.magicalscepter.model.effects.ScepterAnimations;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwingAnimationType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public class ItemInHandLayerMixin<S extends ArmedEntityRenderState, M extends EntityModel<S> & ArmedModel<S>>  {
    @Inject(
            method = "submitArmWithItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/component/SwingAnimation;type()Lnet/minecraft/world/item/SwingAnimationType;"
            )
    )
    private void addSwingAnimationType(
            S state,
            ItemStackRenderState item,
            ItemStack itemStack,
            HumanoidArm arm, PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int lightCoords,
            CallbackInfo callbackInfo
    ) {
        assert state.currentSwing != null && state.currentSwing.hand().asArm(state.mainArm) == arm;
        SwingAnimationType animationType = state.currentSwing.animation().type();

        if (animationType == SwingAnimationType.MAGICALSCEPTER_SWIRL) {
            ScepterAnimations.thirdPersonItemSwing(state, poseStack);
        }
    }
}

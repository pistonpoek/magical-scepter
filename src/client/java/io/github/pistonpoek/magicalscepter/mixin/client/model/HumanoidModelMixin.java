package io.github.pistonpoek.magicalscepter.mixin.client.model;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.pistonpoek.magicalscepter.model.effects.ScepterAnimations;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.SwingAnimationType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin<T extends HumanoidRenderState> {
    @Inject(
            method = "setupAttackAnimation",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/component/SwingAnimation;type()Lnet/minecraft/world/item/SwingAnimationType;"
            )
    )
    private void addSwingAnimationTypeToSwitch(
            final T state,
            CallbackInfo callbackInfo,
            @Local(name = "swingAnimation") float swingAnimation,
            @Local(name = "swing") LivingEntity.SwingDescription swing,
            @Local(name = "swingArm") HumanoidArm swingArm
    ) {
        assert !(swingAnimation <= 0.0F) && swing != null;
        var animationType = swing.animation().type();

        if (animationType == SwingAnimationType.MAGICALSCEPTER_SWIRL) {
            ScepterAnimations.thirdPersonHandSwing(
                    (HumanoidModel<? extends HumanoidRenderState>)(Object) this,
                    swingAnimation,
                    swingArm
            );
        }
    }
}

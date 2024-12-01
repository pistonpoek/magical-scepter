package io.github.pistonpoek.magicalscepter.mixin.client.render.entity.model;

import io.github.pistonpoek.magicalscepter.item.SwingHandLivingEntity;
import io.github.pistonpoek.magicalscepter.render.entity.model.ArmSwingingEntityRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ArmedEntityRenderState.class)
public class ArmedEntityRenderStateMixin {
    @Inject(method="updateRenderState", at=@At("TAIL"))
    private static void updateRenderState(LivingEntity entity, ArmedEntityRenderState state,
                                          ItemModelManager itemModelManager, CallbackInfo callbackInfo) {
        if (state instanceof ArmSwingingEntityRenderState armSwingingEntityRenderState) {
            magical_scepter$updateRenderState(entity, armSwingingEntityRenderState);
        }
    }

    @Unique
    private static void magical_scepter$updateRenderState(LivingEntity entity, ArmSwingingEntityRenderState state) {
        state.magical_scepter$setSwingType(((SwingHandLivingEntity) entity).magical_scepter$getSwingType());
    }
}

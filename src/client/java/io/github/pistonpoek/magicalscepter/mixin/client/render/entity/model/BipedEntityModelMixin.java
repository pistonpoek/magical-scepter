package io.github.pistonpoek.magicalscepter.mixin.client.render.entity.model;

import io.github.pistonpoek.magicalscepter.item.SwingType;
import io.github.pistonpoek.magicalscepter.render.entity.model.ArmSwingingEntityModel;
import io.github.pistonpoek.magicalscepter.render.entity.model.ArmSwingingEntityRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends BipedEntityRenderState & ArmSwingingEntityRenderState>
        extends EntityModel<T> implements ArmSwingingEntityModel<T> {
    @Final
    @Shadow public ModelPart body;

    public BipedEntityModelMixin(ModelPart modelPart) {
        super(modelPart);
    }

    @Shadow protected abstract ModelPart getArm(Arm arm);

    @Override
    public ModelPart magical_scepter$getArm(Arm arm) {
        return getArm(arm);
    }

    @Override
    public ModelPart magical_scepter$getBody() {
        return this.body;
    }

    @Inject(at = @At("HEAD"), method = "animateArms", cancellable = true)
    protected void animateArms(T renderState, float animationProgress, CallbackInfo callbackInfo) {
        if (renderState.magical_scepter$getSwingType() == SwingType.HIT) {
            return; // Allow vanilla hand swing code to perform.
        }

        // Swing the arm for the current render state.
        this.magical_scepter$swingArm(renderState);

        // Cancel the vanilla hand swing attack animation.
        callbackInfo.cancel();
    }

}

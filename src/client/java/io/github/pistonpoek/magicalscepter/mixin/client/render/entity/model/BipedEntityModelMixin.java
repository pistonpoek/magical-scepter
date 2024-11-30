package io.github.pistonpoek.magicalscepter.mixin.client.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import io.github.pistonpoek.magicalscepter.MagicalScepter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends BipedEntityRenderState> extends EntityModel<T> {
    @Final
    @Shadow public ModelPart head;
    @Final
    @Shadow public ModelPart body;
    @Final
    @Shadow public ModelPart rightArm;
    @Final
    @Shadow public ModelPart leftArm;

    public BipedEntityModelMixin(ModelPart modelPart) {
        super(modelPart);
    }

    @Shadow protected abstract ModelPart getArm(Arm arm);

    @Inject(at = @At("HEAD"), method = "animateArms", cancellable = true)
    
    protected void animateArms(T renderState, float animationProgress, CallbackInfo callbackInfo) {
//        if (renderState.getSwingType() == SwingType.HIT) { // TODO add mixin to add swing type to render renderState.
//            return;
//        }
//        if (!livingEntity.handSwinging && renderState.handSwingProgress == 0.0F) {
//            ((SwingHandLivingEntity) livingEntity).setSwingType(SwingType.HIT);
//            return;
//        }
        // TODO create method that uses ArmedEntityRenderState, with handSwingProgress parameter, and the modelparts?
        if (renderState.handSwingProgress > 0.0F) {
            Arm arm = renderState.preferredArm;
            ModelPart armModel = this.getArm(arm);
            boolean moveBody = true;
            float yawMovement = MathHelper.sin(MathHelper.sqrt(renderState.handSwingProgress) * MathHelper.TAU) * 0.2F;
            if (arm == Arm.LEFT) {
                yawMovement *= -1.0F;
            }
            if (moveBody) {
                this.body.yaw = yawMovement;
                this.rightArm.pivotZ = MathHelper.sin(this.body.yaw) * 5.0F;
                this.rightArm.pivotX = -MathHelper.cos(this.body.yaw) * 5.0F;
                this.leftArm.pivotZ = -MathHelper.sin(this.body.yaw) * 5.0F;
                this.leftArm.pivotX = MathHelper.cos(this.body.yaw) * 5.0F;
                this.rightArm.yaw += this.body.yaw;
                this.leftArm.yaw += this.body.yaw;
                this.leftArm.pitch += this.body.yaw;
            }

            float f = 1.0F - 2.0F * renderState.handSwingProgress;
            f *= f;
            f *= f;
            f = 1.0F - f;
            float g = MathHelper.sin(f * MathHelper.PI);
            float h = MathHelper.sin(renderState.handSwingProgress * MathHelper.PI) * -(this.head.pitch - 0.7F) * 0.75F;
//            armModel.pitch -= g * 2.0F + h; // Forward backwards
//            armModel.yaw += (MathHelper.sin(MathHelper.sqrt(this.handSwingProgress) * MathHelper.TAU) + 1.0F) * -0.6F; // To from body
//            armModel.roll -= MathHelper.sin(this.handSwingProgress * MathHelper.PI) * 0.8F; // Around arm
            armModel.pitch -= MathHelper.sin(5.0F/3.0F*MathHelper.PI*renderState.handSwingProgress-1.0F/3.0F*MathHelper.PI)-MathHelper.sin(-1.0F/3.0F*MathHelper.PI);
            //armModel.pitch -= MathHelper.PI/2 - MathHelper.PI/10;
            armModel.yaw -= (MathHelper.sin(g) + 0.9F) * 0.7F;
//            armModel.yaw -= MathHelper.PI * 1/4;
            //armModel.roll += MathHelper.sin(1.0F-(float)Math.pow(1.1F-2.2F*this.handSwingProgress, 4.0F) *MathHelper.PI)+0.84147F;
            MagicalScepter.LOGGER.info("Pitch %f, Yaw %f, Roll %f".formatted(
                        armModel.pitch * MathHelper.DEGREES_PER_RADIAN,
                        armModel.yaw * MathHelper.DEGREES_PER_RADIAN,
                        armModel.roll * MathHelper.DEGREES_PER_RADIAN));
        }
        // Cancel the vanilla swing animation.
        callbackInfo.cancel();
    }

}

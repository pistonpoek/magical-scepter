package io.github.pistonpoek.magicalscepter.mixin.client.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.item.SwingHandLivingEntity;
import io.github.pistonpoek.magicalscepter.item.SwingType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity> extends AnimalModel<T> {
    @Shadow public ModelPart head;
    @Shadow public ModelPart body;
    @Shadow public ModelPart rightArm;
    @Shadow public ModelPart leftArm;

    @Shadow protected abstract ModelPart getArm(Arm arm);
    @Shadow abstract Arm getPreferredArm(T entity);

    @Inject(at = @At("HEAD"), method = "animateArms", cancellable = true)
    protected void animateArms(T livingEntity, float animationProgress, CallbackInfo callbackInfo) {
        if (((SwingHandLivingEntity)livingEntity).getSwingType() == SwingType.HIT) {
            return;
        }
        if (!livingEntity.handSwinging && this.handSwingProgress == 0.0F) {
            ((SwingHandLivingEntity) livingEntity).setSwingType(SwingType.HIT);
            return;
        }
        if (this.handSwingProgress > 0.0F) {
            Arm arm = this.getPreferredArm(livingEntity);
            ModelPart armModel = this.getArm(arm);
            boolean moveBody = true;
            float yawMovement = MathHelper.sin(MathHelper.sqrt(this.handSwingProgress) * MathHelper.TAU) * 0.2F;
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

            float f = 1.0F - 2.0F * this.handSwingProgress;
            f *= f;
            f *= f;
            f = 1.0F - f;
            float g = MathHelper.sin(f * MathHelper.PI);
            float h = MathHelper.sin(this.handSwingProgress * MathHelper.PI) * -(this.head.pitch - 0.7F) * 0.75F;
//            armModel.pitch -= g * 2.0F + h; // Forward backwards
//            armModel.yaw += (MathHelper.sin(MathHelper.sqrt(this.handSwingProgress) * MathHelper.TAU) + 1.0F) * -0.6F; // To from body
//            armModel.roll -= MathHelper.sin(this.handSwingProgress * MathHelper.PI) * 0.8F; // Around arm
            armModel.pitch -= MathHelper.sin(5.0F/3.0F*MathHelper.PI*this.handSwingProgress-1.0F/3.0F*MathHelper.PI)-MathHelper.sin(-1.0F/3.0F*MathHelper.PI);
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

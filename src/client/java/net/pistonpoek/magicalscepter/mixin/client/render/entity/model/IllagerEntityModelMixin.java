package net.pistonpoek.magicalscepter.mixin.client.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.pistonpoek.magicalscepter.MagicalScepter;
import net.pistonpoek.magicalscepter.item.SwingHandLivingEntity;
import net.pistonpoek.magicalscepter.item.SwingType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(IllagerEntityModel.class)
public abstract class IllagerEntityModelMixin<T extends IllagerEntity> extends SinglePartEntityModel<T> {
    @Shadow public ModelPart head;
    @Shadow public ModelPart rightArm;
    @Shadow public ModelPart leftArm;
    @Shadow public ModelPart root;
    public ModelPart body;

    @Inject(method="<init>(Lnet/minecraft/client/model/ModelPart;)V", at = @At("TAIL"))
    public void IllagerEntityModel(ModelPart root, CallbackInfo callbackInfo) {
        this.body = this.root.getChild(EntityModelPartNames.BODY);
    }

    private Arm getPreferredArm(T entity) {
        Arm arm = entity.getMainArm();
        return entity.preferredHand == Hand.MAIN_HAND ? arm : arm.getOpposite();
    }

    @Shadow abstract ModelPart getAttackingArm(Arm arm);

    @Inject(method = "setAngles", at = @At("TAIL"))
    public void setAngles(T livingEntity, float f, float g, float animationProgress, float i, float j, CallbackInfo info) {
        if (this.handSwingProgress <= 0.0F) {
            return;
        }

        if (((SwingHandLivingEntity)livingEntity).getSwingType() == SwingType.HIT) {
            swingHandAttack(livingEntity);
        } else {
            swingHandProtect(livingEntity);
        }
    }

    // TODO combine Illager and BipedEntityModel to use the same method.
    public void swingHandProtect(T livingEntity) {
        Arm arm = this.getPreferredArm(livingEntity);
        ModelPart armModel = getAttackingArm(arm);
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

    public void swingHandAttack(T livingEntity) {
        Arm arm = this.getPreferredArm(livingEntity);
        ModelPart armModel = getAttackingArm(arm);
        this.body.yaw = MathHelper.sin(MathHelper.sqrt(this.handSwingProgress) * MathHelper.TAU) * 0.2F;

        if (arm == Arm.LEFT) {
            this.body.yaw *= -1.0F;
        }

        this.rightArm.pivotZ = MathHelper.sin(this.body.yaw) * 5.0F;
        this.rightArm.pivotX = -MathHelper.cos(this.body.yaw) * 5.0F;
        this.leftArm.pivotZ = -MathHelper.sin(this.body.yaw) * 5.0F;
        this.leftArm.pivotX = MathHelper.cos(this.body.yaw) * 5.0F;
        this.rightArm.yaw += this.body.yaw;
        this.leftArm.yaw += this.body.yaw;
        this.leftArm.pitch += this.body.yaw;
        float f = 1.0F - this.handSwingProgress;
        f *= f;
        f *= f;
        f = 1.0F - f;
        float g = MathHelper.sin(f * MathHelper.PI);
        float h = MathHelper.sin(this.handSwingProgress * MathHelper.PI) * -(this.head.pitch - 0.7F) * 0.75F;
        armModel.pitch -= g * 1.2F + h;
        armModel.yaw += this.body.yaw * 2.0F;
        armModel.roll += MathHelper.sin(this.handSwingProgress * MathHelper.PI) * -0.4F;
        MagicalScepter.LOGGER.info("Pitch %f, Yaw %f, Roll %f".formatted(
                armModel.pitch * MathHelper.DEGREES_PER_RADIAN,
                armModel.yaw * MathHelper.DEGREES_PER_RADIAN,
                armModel.roll * MathHelper.DEGREES_PER_RADIAN));
    }
}

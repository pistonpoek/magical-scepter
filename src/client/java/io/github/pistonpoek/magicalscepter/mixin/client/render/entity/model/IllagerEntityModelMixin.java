package io.github.pistonpoek.magicalscepter.mixin.client.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.client.render.entity.state.IllagerEntityRenderState;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import io.github.pistonpoek.magicalscepter.MagicalScepter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(IllagerEntityModel.class)
public abstract class IllagerEntityModelMixin<T extends IllagerEntityRenderState> extends EntityModel<T> {
    @Final
    @Shadow
    private ModelPart head;
    @Final
    @Shadow
    private ModelPart rightArm;
    @Final
    @Shadow
    private ModelPart leftArm;
    @Unique
    private ModelPart  magical_scepter$body;

    protected IllagerEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method="<init>(Lnet/minecraft/client/model/ModelPart;)V", at = @At("TAIL"))
    public void IllagerEntityModel(ModelPart root, CallbackInfo callbackInfo) {
        this.magical_scepter$body = root.getChild(EntityModelPartNames.BODY);
    }

    @Shadow abstract ModelPart getAttackingArm(Arm arm);

    @Inject(method = "setAngles", at = @At("TAIL"))
    public void setAngles(T renderState, CallbackInfo info) {
        if (renderState.handSwingProgress <= 0.0F) {
            return;
        }

//        if (((SwingHandLivingEntity)livingEntity).getSwingType() == SwingType.HIT) {
//            swingHandAttack(livingEntity);
//        } else {
//            swingHandProtect(livingEntity);
//        }
    }

    // TODO combine Illager and BipedEntityModel to use the same method.
    public void swingHandProtect(T renderState) {
        Arm arm = renderState.illagerMainArm;
        ModelPart armModel = getAttackingArm(arm);
        boolean moveBody = true;
        float yawMovement = MathHelper.sin(MathHelper.sqrt(renderState.handSwingProgress) * MathHelper.TAU) * 0.2F;
        if (arm == Arm.LEFT) {
            yawMovement *= -1.0F;
        }
        if (moveBody) {
            this.magical_scepter$body.yaw = yawMovement;
            this.rightArm.pivotZ = MathHelper.sin(this.magical_scepter$body.yaw) * 5.0F;
            this.rightArm.pivotX = -MathHelper.cos(this.magical_scepter$body.yaw) * 5.0F;
            this.leftArm.pivotZ = -MathHelper.sin(this.magical_scepter$body.yaw) * 5.0F;
            this.leftArm.pivotX = MathHelper.cos(this.magical_scepter$body.yaw) * 5.0F;
            this.rightArm.yaw += this.magical_scepter$body.yaw;
            this.leftArm.yaw += this.magical_scepter$body.yaw;
            this.leftArm.pitch += this.magical_scepter$body.yaw;
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
        // TODO fix yaw moving wrong direction for left hand.
        armModel.yaw -= (MathHelper.sin(g) + 0.9F) * 0.7F;
//            armModel.yaw -= MathHelper.PI * 1/4;
        //armModel.roll += MathHelper.sin(1.0F-(float)Math.pow(1.1F-2.2F*this.handSwingProgress, 4.0F) *MathHelper.PI)+0.84147F;
        MagicalScepter.LOGGER.info("Pitch %f, Yaw %f, Roll %f".formatted(
                armModel.pitch * MathHelper.DEGREES_PER_RADIAN,
                armModel.yaw * MathHelper.DEGREES_PER_RADIAN,
                armModel.roll * MathHelper.DEGREES_PER_RADIAN));
    }

    public void swingHandAttack(T renderState) {
        Arm arm = renderState.illagerMainArm;
        ModelPart armModel = getAttackingArm(arm);
        this.magical_scepter$body.yaw = MathHelper.sin(MathHelper.sqrt(renderState.handSwingProgress) * MathHelper.TAU) * 0.2F;

        if (arm == Arm.LEFT) {
            this.magical_scepter$body.yaw *= -1.0F;
        }

        this.rightArm.pivotZ = MathHelper.sin(this.magical_scepter$body.yaw) * 5.0F;
        this.rightArm.pivotX = -MathHelper.cos(this.magical_scepter$body.yaw) * 5.0F;
        this.leftArm.pivotZ = -MathHelper.sin(this.magical_scepter$body.yaw) * 5.0F;
        this.leftArm.pivotX = MathHelper.cos(this.magical_scepter$body.yaw) * 5.0F;
        this.rightArm.yaw += this.magical_scepter$body.yaw;
        this.leftArm.yaw += this.magical_scepter$body.yaw;
        this.leftArm.pitch += this.magical_scepter$body.yaw;
        float f = 1.0F - renderState.handSwingProgress;
        f *= f;
        f *= f;
        f = 1.0F - f;
        float g = MathHelper.sin(f * MathHelper.PI);
        float h = MathHelper.sin(renderState.handSwingProgress * MathHelper.PI) * -(this.head.pitch - 0.7F) * 0.75F;
        armModel.pitch -= g * 1.2F + h;
        armModel.yaw += this.magical_scepter$body.yaw * 2.0F;
        armModel.roll += MathHelper.sin(renderState.handSwingProgress * MathHelper.PI) * -0.4F;
        MagicalScepter.LOGGER.info("Pitch %f, Yaw %f, Roll %f".formatted(
                armModel.pitch * MathHelper.DEGREES_PER_RADIAN,
                armModel.yaw * MathHelper.DEGREES_PER_RADIAN,
                armModel.roll * MathHelper.DEGREES_PER_RADIAN));
    }
}

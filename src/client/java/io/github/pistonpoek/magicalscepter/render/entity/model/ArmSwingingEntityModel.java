package io.github.pistonpoek.magicalscepter.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
public interface ArmSwingingEntityModel<T extends ArmedEntityRenderState & ArmSwingingEntityRenderState> 
        extends ModelWithHead {
    /**
     * Get the model part for the specified arm.
     *
     * @param arm Arm to get model part for.
     * @return Model part of the arm.
     */
    @Unique ModelPart magical_scepter$getArm(Arm arm);

    /**
     * Get the model part for the body of the entity model.
     *
     * @return Model part of the body.
     */
    @Unique ModelPart magical_scepter$getBody();

    /**
     * Swing the arm of the entity model for the specified render state.
     * 
     * @param renderState Render state used to swing arm for.
     */
    default void magical_scepter$swingArm(T renderState, Arm arm) {
        if (renderState.magical_scepter$getHandSwingProgress() <= 0.0F) {
            return;
        }

        switch(renderState.magical_scepter$getSwingType()) {
            case HIT -> magical_scepter$swingHandAttack(renderState, arm);
            case PROTECT -> magical_scepter$swingHandProtect(renderState, arm);
        };
    }

    /**
     * Animate the specified arm to swing in an attack motion.
     *
     * @param renderState Render state to use for animation.
     * @param arm Arm to swing.
     */
    @Unique default void magical_scepter$swingHandAttack(T renderState, Arm arm) {
        ModelPart armModel = magical_scepter$getArm(arm);
        ModelPart bodyModel = magical_scepter$getBody();
        ModelPart headModel = getHead();
        ModelPart leftArmModel = magical_scepter$getArm(Arm.LEFT);
        ModelPart rightArmModel = magical_scepter$getArm(Arm.RIGHT);
        float handSwingProgress = renderState.magical_scepter$getHandSwingProgress();

        bodyModel.yaw = MathHelper.sin(MathHelper.sqrt(handSwingProgress) * MathHelper.TAU) * 0.2F;

        if (arm == Arm.LEFT) {
            bodyModel.yaw *= -1.0F;
        }

        // Move the body and arm pivots with it
        rightArmModel.originZ = MathHelper.sin(bodyModel.yaw) * 5.0F;
        rightArmModel.originX = -MathHelper.cos(bodyModel.yaw) * 5.0F;
        leftArmModel.originZ = -MathHelper.sin(bodyModel.yaw) * 5.0F;
        leftArmModel.originX = MathHelper.cos(bodyModel.yaw) * 5.0F;
        rightArmModel.yaw += bodyModel.yaw;
        leftArmModel.yaw += bodyModel.yaw;
        leftArmModel.pitch += bodyModel.yaw;

        float f = 1.0F - handSwingProgress;
        f *= f;
        f *= f;
        f = 1.0F - f;
        float g = MathHelper.sin(f * MathHelper.PI);
        float h = MathHelper.sin(handSwingProgress * MathHelper.PI) * -(headModel.pitch - 0.7F) * 0.75F;
        armModel.pitch -= g * 1.2F + h;
        armModel.yaw += bodyModel.yaw * 2.0F;
        armModel.roll += MathHelper.sin(handSwingProgress * MathHelper.PI) * -0.4F;
    }

    /**
     * Animate the specified arm to swing in a protect motion.
     *
     * @param renderState Render state to use for animation.
     * @param arm Arm to swing.
     */
    @Unique default void magical_scepter$swingHandProtect(T renderState, Arm arm) {
        ModelPart armModel = magical_scepter$getArm(arm);
        ModelPart bodyModel = magical_scepter$getBody();
        ModelPart headModel = getHead();
        ModelPart leftArmModel = magical_scepter$getArm(Arm.LEFT);
        ModelPart rightArmModel = magical_scepter$getArm(Arm.RIGHT);
        float handSwingProgress = renderState.magical_scepter$getHandSwingProgress();
        float leftMirror = arm == Arm.LEFT ? -1.0F : 1.0F;

        // Move the body and arm pivots with it
        bodyModel.yaw = leftMirror * MathHelper.sin(MathHelper.sqrt(handSwingProgress) * MathHelper.TAU) * 0.2F;
        rightArmModel.originZ = MathHelper.sin(bodyModel.yaw) * 5.0F;
        rightArmModel.originX = -MathHelper.cos(bodyModel.yaw) * 5.0F;
        leftArmModel.originZ = -MathHelper.sin(bodyModel.yaw) * 5.0F;
        leftArmModel.originX = MathHelper.cos(bodyModel.yaw) * 5.0F;
        rightArmModel.yaw += bodyModel.yaw;
        leftArmModel.yaw += bodyModel.yaw;
        leftArmModel.pitch += bodyModel.yaw;

        // TODO update swing animation, variable naming and code structuring here.
        float f = 1.0F - 2.0F * handSwingProgress;
        f *= f;
        f *= f;
        f = 1.0F - f;
        float g = MathHelper.sin(f * MathHelper.PI);
        float h = MathHelper.sin(handSwingProgress * MathHelper.PI) * -(headModel.pitch - 0.7F) * 0.75F;
//            armModel.pitch -= g * 2.0F + h; // Forward backwards
//            armModel.yaw += (MathHelper.sin(MathHelper.sqrt(this.handSwingProgress) * MathHelper.TAU) + 1.0F) * -0.6F; // To from body
//            armModel.roll -= MathHelper.sin(this.handSwingProgress * MathHelper.PI) * 0.8F; // Around arm
        armModel.pitch -= MathHelper.sin(5.0F/3.0F*MathHelper.PI*handSwingProgress-1.0F/3.0F*MathHelper.PI)-MathHelper.sin(-1.0F/3.0F*MathHelper.PI);
        //armModel.pitch -= MathHelper.PI/2 - MathHelper.PI/10;
        armModel.yaw -= leftMirror * (MathHelper.sin(g) + 0.9F) * 0.7F;
//            armModel.yaw -= MathHelper.PI * 1/4;
        //armModel.roll += MathHelper.sin(1.0F-(float)Math.pow(1.1F-2.2F*this.handSwingProgress, 4.0F) *MathHelper.PI)+0.84147F;
    }
}

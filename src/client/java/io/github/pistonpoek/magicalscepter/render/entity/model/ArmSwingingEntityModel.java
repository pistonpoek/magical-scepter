package io.github.pistonpoek.magicalscepter.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
public interface ArmSwingingEntityModel<T extends ArmedEntityRenderState & ArmSwingingEntityRenderState>
        extends HeadedModel {
    /**
     * Get the model part for the specified arm.
     *
     * @param arm Arm to get model part for.
     * @return Model part of the arm.
     */
    @Unique
    ModelPart magical_scepter$getArm(HumanoidArm arm);

    /**
     * Get the model part for the body of the entity model.
     *
     * @return Model part of the body.
     */
    @Unique
    ModelPart magical_scepter$getBody();

    /**
     * Swing the arm of the entity model for the specified render state.
     *
     * @param renderState Render state used to swing arm for.
     */
    default void magical_scepter$swingArm(T renderState, HumanoidArm arm) {
        if (renderState.magical_scepter$getHandSwingProgress() <= 0.0F) {
            return;
        }

        switch (renderState.magical_scepter$getSwingType()) {
            case HIT -> magical_scepter$swingHandAttack(renderState, arm);
            case PROTECT -> magical_scepter$swingHandProtect(renderState, arm);
        }
    }

    /**
     * Animate the specified arm to swing in an attack motion.
     *
     * @param renderState Render state to use for animation.
     * @param arm         Arm to swing.
     */
    @Unique
    default void magical_scepter$swingHandAttack(T renderState, HumanoidArm arm) {
        ModelPart armModel = magical_scepter$getArm(arm);
        ModelPart bodyModel = magical_scepter$getBody();
        ModelPart headModel = getHead();
        ModelPart leftArmModel = magical_scepter$getArm(HumanoidArm.LEFT);
        ModelPart rightArmModel = magical_scepter$getArm(HumanoidArm.RIGHT);
        float handSwingProgress = renderState.magical_scepter$getHandSwingProgress();

        bodyModel.yRot = Mth.sin(Mth.sqrt(handSwingProgress) * Mth.TWO_PI) * 0.2F;

        if (arm == HumanoidArm.LEFT) {
            bodyModel.yRot *= -1.0F;
        }

        // Move the body and arm pivots with it
        rightArmModel.z = Mth.sin(bodyModel.yRot) * 5.0F;
        rightArmModel.x = -Mth.cos(bodyModel.yRot) * 5.0F;
        leftArmModel.z = -Mth.sin(bodyModel.yRot) * 5.0F;
        leftArmModel.x = Mth.cos(bodyModel.yRot) * 5.0F;
        rightArmModel.yRot += bodyModel.yRot;
        leftArmModel.yRot += bodyModel.yRot;
        leftArmModel.xRot += bodyModel.yRot;

        armModel.xRot -= Mth.sin((1.0F - (float) Math.pow(1.0F - handSwingProgress, 4)) * Mth.PI)
                * 1.2F + Mth.sin(handSwingProgress * Mth.PI) * -(headModel.xRot - 0.7F) * 0.75F;
        armModel.yRot += bodyModel.yRot * 2.0F;
        armModel.zRot += Mth.sin(handSwingProgress * Mth.PI) * -0.4F;
    }

    /**
     * Animate the specified arm to swing in a protect motion.
     *
     * @param renderState Render state to use for animation.
     * @param arm         Arm to swing.
     */
    @Unique
    default void magical_scepter$swingHandProtect(T renderState, HumanoidArm arm) {
        ModelPart armModel = magical_scepter$getArm(arm);
        ModelPart bodyModel = magical_scepter$getBody();
        ModelPart leftArmModel = magical_scepter$getArm(HumanoidArm.LEFT);
        ModelPart rightArmModel = magical_scepter$getArm(HumanoidArm.RIGHT);
        float handSwingProgress = renderState.magical_scepter$getHandSwingProgress();
        float leftMirror = arm == HumanoidArm.LEFT ? -1.0F : 1.0F;

        // Move the body and arm pivots with it
        bodyModel.yRot = leftMirror * Mth.sin(Mth.sqrt(handSwingProgress) * Mth.TWO_PI) * 0.2F;
        rightArmModel.z = Mth.sin(bodyModel.yRot) * 5.0F;
        rightArmModel.x = -Mth.cos(bodyModel.yRot) * 5.0F;
        leftArmModel.z = -Mth.sin(bodyModel.yRot) * 5.0F;
        leftArmModel.x = Mth.cos(bodyModel.yRot) * 5.0F;
        rightArmModel.yRot += bodyModel.yRot;
        leftArmModel.yRot += bodyModel.yRot;
        leftArmModel.xRot += bodyModel.yRot;

        armModel.xRot -= Mth.sin(5.0F / 3.0F * Mth.PI * handSwingProgress
                - 1.0F / 3.0F * Mth.PI) - Mth.sin(-1.0F / 3.0F * Mth.PI);
        armModel.yRot -= leftMirror * (Mth.sin(Mth.sin(
                (1.0F - (float) Math.pow(1.0F - 2.0F * handSwingProgress, 4)) * Mth.PI)) + 0.9F) * 0.7F;
    }
}

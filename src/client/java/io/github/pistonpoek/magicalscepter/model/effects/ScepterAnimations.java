package io.github.pistonpoek.magicalscepter.model.effects;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Ease;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

/**
 * Animations for the usage of the magical scepter item.
 *
 * @see net.minecraft.client.model.effects.SpearAnimations
 */
public class ScepterAnimations {
    private static float progress(final float time, final float start, final float end) {
        return Mth.clamp(Mth.inverseLerp(time, start, end), 0.0F, 1.0F);
    }

    /**
     * Animate the third person hand for the swing.
     * 
     * @param model     Humanoid model to animate.
     * @param animation Animation time to update the model to.
     * @param arm       Humanoid arm to update in the model.
     * @param <T>       Type of humanoid render state to use.
     *     
     * @see net.minecraft.client.model.effects.SpearAnimations#thirdPersonAttackHand(HumanoidModel, float, HumanoidArm) 
     */
    public static <T extends HumanoidRenderState> void thirdPersonHandSwing(
            final HumanoidModel<T> model,
            final float animation,
            final HumanoidArm arm
    ) {
        // WHACK
//        float aa = Mth.sin(Ease.outQuart(animation) * (float) Math.PI);
//        float bb = Mth.sin(animation * (float) Math.PI) * -(model.head.xRot - 0.7F) * 0.75F;
//        ModelPart attackArm = model.getArm(arm);
//        attackArm.xRot -= aa * 1.2F + bb;
//        attackArm.yRot = attackArm.yRot + model.body.yRot * 2.0F;
//        attackArm.zRot = attackArm.zRot + Mth.sin(animation * (float) Math.PI) * -0.4F;

        // STAB
//        model.rightArm.yRot = model.rightArm.yRot - model.body.yRot;
//        model.leftArm.yRot = model.leftArm.yRot - model.body.yRot;
//        float prepare = Ease.inOutSine(progress(animation, 0.0F, 0.05F));
//        float attack = Ease.inQuad(progress(animation, 0.05F, 0.2F));
//        float retract = Ease.inOutExpo(progress(animation, 0.4F, 1.0F));
//        model.getArm(arm).xRot += (90.0F * prepare - 120.0F * attack + 30.0F * retract) * Mth.DEG_TO_RAD;

        // SWIRL
        ModelPart swingArm = model.getArm(arm);

        float upDown = Mth.sin(animation * Mth.PI);
        float middleOutBack = Mth.sin(animation * 2.0F * Mth.PI);

        swingArm.xRot += (upDown * -90.0F) * Mth.DEG_TO_RAD;
        swingArm.yRot += (middleOutBack * -40.0F ) * Mth.DEG_TO_RAD;
    }

    /**
     * Animate the third person item for the swing.
     * 
     * @param state     Armed entity render state to use.
     * @param poseStack Item stack to animate.
     * @param <S>       Type of armed entity render state.
     * 
     * @see net.minecraft.client.model.effects.SpearAnimations#thirdPersonAttackItem(ArmedEntityRenderState, PoseStack) 
     */
    public static <S extends ArmedEntityRenderState> void thirdPersonItemSwing(
            final S state,
            final PoseStack poseStack
    ) {
        // STAB
//        float animation = state.swingAnimation;
//        if (!(animation <= 0.0F)) {
//            KineticWeapon kineticWeapon = (KineticWeapon)state.getMainHandItemStack().get(DataComponents.KINETIC_WEAPON);
//            float jetForward = kineticWeapon != null ? kineticWeapon.forwardMovement() : 0.0F;
//            float itemInHandDepth = 0.125F;
//            float attack = Ease.inQuad(progress(animation, 0.05F, 0.2F));
//            float retract = Ease.inOutExpo(progress(animation, 0.4F, 1.0F));
//            poseStack.rotateAround(Axis.XN.rotationDegrees(70.0F * (attack - retract)), 0.0F, -0.125F, 0.125F);
//            poseStack.translate(0.0F, jetForward * (attack - retract), 0.0F);
//        }
    }

    /**
     * Animate the first person item for the swing.
     *
     * @param animation Animation time to update the pose stack to.
     * @param poseStack Pose stack to update for the animation.
     * @param invert    Value to use for mirroring animation for other arm. (Either 1 or -1)
     * @param arm       Humanoid arm to animate for.
     *
     * @see net.minecraft.client.model.effects.SpearAnimations#firstPersonAttack(float, PoseStack, int, HumanoidArm)
     */
    public static void firstPersonSwing(final float animation, final PoseStack poseStack, final int invert, final HumanoidArm arm) {
        float startingAmount = Ease.inOutSine(progress(animation, 0.0F, 0.05F));
        float middleAmount = Ease.outBack(progress(animation, 0.05F, 0.2F));
        float endingAmount = Ease.inOutExpo(progress(animation, 0.4F, 1.0F));
        poseStack.translate(invert * 0.1F * (startingAmount - middleAmount), -0.075F * (startingAmount - endingAmount), 0.65F * (startingAmount - middleAmount));
        poseStack.rotate(Axis.XP.rotationDegrees(-70.0F * (startingAmount - endingAmount)));
        poseStack.translate(0.0, 0.0, -0.25 * (endingAmount - middleAmount));
    }
}

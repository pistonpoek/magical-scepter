package io.github.pistonpoek.magicalscepter.mixin.client.render.entity.model;

import io.github.pistonpoek.magicalscepter.render.entity.model.ArmSwingingEntityModel;
import io.github.pistonpoek.magicalscepter.render.entity.model.ArmSwingingEntityRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.client.render.entity.state.IllagerEntityRenderState;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(IllagerEntityModel.class)
public abstract class IllagerEntityModelMixin<T extends IllagerEntityRenderState & ArmSwingingEntityRenderState>
        extends EntityModel<T> implements ArmSwingingEntityModel<T> {
    @Unique
    private ModelPart magicalscepter$body;

    @Shadow
    protected abstract ModelPart getAttackingArm(Arm arm);

    protected IllagerEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Override
    public ModelPart magical_scepter$getArm(Arm arm) {
        return getAttackingArm(arm);
    }

    @Override
    public ModelPart magical_scepter$getBody() {
        return this.magicalscepter$body;
    }

    /**
     * Store the body model part of the illager entity model.
     *
     * @param root         Model root of the illager model.
     * @param callbackInfo Callback info of the method injection.
     */
    @Inject(method = "<init>(Lnet/minecraft/client/model/ModelPart;)V", at = @At("TAIL"))
    public void IllagerEntityModel(ModelPart root, CallbackInfo callbackInfo) {
        this.magicalscepter$body = root.getChild(EntityModelPartNames.BODY);
    }

    /**
     * Swing the main arm of the specified render state.
     *
     * @param renderState  Render state to get main arm from.
     * @param callbackInfo Callback info of the method injection.
     */
    @Inject(method = "setAngles*", at = @At("TAIL"))
    public void swingMainArm(T renderState, CallbackInfo callbackInfo) {
        this.magical_scepter$swingArm(renderState, renderState.illagerMainArm);
    }
}

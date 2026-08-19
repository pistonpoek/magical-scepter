package io.github.pistonpoek.magicalscepter.mixin.client.render.entity.model;

import io.github.pistonpoek.magicalscepter.render.entity.model.ArmSwingingEntityModel;
import io.github.pistonpoek.magicalscepter.render.entity.model.ArmSwingingEntityRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.monster.illager.IllagerModel;
import net.minecraft.client.renderer.entity.state.IllagerRenderState;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(IllagerModel.class)
public abstract class IllagerEntityModelMixin<T extends IllagerRenderState & ArmSwingingEntityRenderState>
        extends EntityModel<T> implements ArmSwingingEntityModel<T> {
    @Unique
    private ModelPart magicalscepter$body;

    @Shadow
    protected abstract ModelPart getArm(HumanoidArm arm);

    protected IllagerEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Override
    public ModelPart magical_scepter$getArm(HumanoidArm arm) {
        return getArm(arm);
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
    @Inject(method = "<init>(Lnet/minecraft/client/model/geom/ModelPart;)V", at = @At("TAIL"))
    public void IllagerEntityModel(ModelPart root, CallbackInfo callbackInfo) {
        this.magicalscepter$body = root.getChild(PartNames.BODY);
    }

    /**
     * Swing the main arm of the specified render state.
     *
     * @param renderState  Render state to get main arm from.
     * @param callbackInfo Callback info of the method injection.
     */
    @Inject(method = "setupAnim*", at = @At("TAIL"))
    public void swingMainArm(T renderState, CallbackInfo callbackInfo) {
        this.magical_scepter$swingArm(renderState, renderState.mainArm);
    }
}

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

    protected IllagerEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Shadow
    protected abstract ModelPart getAttackingArm(Arm arm);

    @Override
    public ModelPart magical_scepter$getArm(Arm arm) {
        return getAttackingArm(arm);
    }

    @Override
    public ModelPart magical_scepter$getBody() {
        return this.magicalscepter$body;
    }

    @Inject(method="<init>(Lnet/minecraft/client/model/ModelPart;)V", at = @At("TAIL"))
    public void IllagerEntityModel(ModelPart root, CallbackInfo callbackInfo) {
        this.magicalscepter$body = root.getChild(EntityModelPartNames.BODY);
    }

    @Inject(method = "setAngles*", at = @At("TAIL"))
    public void setAngles(T renderState, CallbackInfo info) {
        this.magical_scepter$swingArm(renderState, renderState.illagerMainArm);
    }
}

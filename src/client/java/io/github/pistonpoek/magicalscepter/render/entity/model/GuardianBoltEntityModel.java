package io.github.pistonpoek.magicalscepter.render.entity.model;

import io.github.pistonpoek.magicalscepter.render.entity.GuardianBoltEntityRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

import java.util.EnumSet;

@Environment(EnvType.CLIENT)
public class GuardianBoltEntityModel extends EntityModel<GuardianBoltEntityRenderState> {
    /**
     * The key of the bolt model part, whose value is {@value}.
     */
    private static final String BOLT_MODEL_KEY = "main";
    private final ModelPart bolt;

    public GuardianBoltEntityModel(ModelPart modelPart) {
        super(modelPart, RenderLayer::getEntityTranslucent);
        this.bolt = modelPart.getChild(BOLT_MODEL_KEY);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData boltModelPart = modelPartData.addChild(BOLT_MODEL_KEY);
        ModelPartBuilder modelPartBuilder = ModelPartBuilder.create().uv(0,0).cuboid(-4.0F, 0, -4.0F, 8.0F, 0, 8.0F, EnumSet.of(Direction.UP));
        boltModelPart.addChild("up", modelPartBuilder, ModelTransform.NONE);
        boltModelPart.addChild("front", modelPartBuilder, ModelTransform.rotation(-MathHelper.HALF_PI, 0.0F, MathHelper.HALF_PI));
        boltModelPart.addChild("left", modelPartBuilder, ModelTransform.rotation(MathHelper.HALF_PI, -MathHelper.HALF_PI, 0.0F));
        return TexturedModelData.of(modelData, 16, 16);
    }

    public void setAngles(GuardianBoltEntityRenderState state) {
        super.setAngles(state);
        this.bolt.yaw = state.yaw * (float) (Math.PI / 180.0);
        this.bolt.pitch = state.pitch * (float) (Math.PI / 180.0);
    }
}

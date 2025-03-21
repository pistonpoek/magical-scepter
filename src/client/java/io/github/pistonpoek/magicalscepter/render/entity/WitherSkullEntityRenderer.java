package io.github.pistonpoek.magicalscepter.render.entity;


import io.github.pistonpoek.magicalscepter.entity.projectile.WitherSkullEntity;
import io.github.pistonpoek.magicalscepter.render.entity.model.ModEntityModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class WitherSkullEntityRenderer extends EntityRenderer<WitherSkullEntity, WitherSkullEntityRenderState> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/wither/wither.png");
    private final SkullEntityModel model;

    public WitherSkullEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new SkullEntityModel(context.getPart(ModEntityModelLayers.WITHER_SKULL));
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(EntityModelPartNames.HEAD,
                ModelPartBuilder.create().uv(0, 35)
                        .cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
                ModelTransform.NONE);
        return TexturedModelData.of(modelData, 64, 64);
    }

    protected int getBlockLight(WitherSkullEntity witherSkullEntity, BlockPos blockPos) {
        return 15;
    }

    public void render(WitherSkullEntityRenderState witherSkullEntityRenderState,
                       MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(TEXTURE));
        this.model.setHeadRotation(0.0F, witherSkullEntityRenderState.yaw, witherSkullEntityRenderState.pitch);
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
        super.render(witherSkullEntityRenderState, matrixStack, vertexConsumerProvider, i);
    }

    public WitherSkullEntityRenderState createRenderState() {
        return new WitherSkullEntityRenderState();
    }

    public void updateRenderState(WitherSkullEntity witherSkullEntity,
                                  WitherSkullEntityRenderState witherSkullEntityRenderState, float f) {
        super.updateRenderState(witherSkullEntity, witherSkullEntityRenderState, f);
        witherSkullEntityRenderState.yaw = witherSkullEntity.getLerpedYaw(f);
        witherSkullEntityRenderState.pitch = witherSkullEntity.getLerpedPitch(f);
    }
}

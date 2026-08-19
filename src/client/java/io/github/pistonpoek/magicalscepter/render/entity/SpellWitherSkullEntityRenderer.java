package io.github.pistonpoek.magicalscepter.render.entity;


import com.mojang.blaze3d.vertex.PoseStack;
import io.github.pistonpoek.magicalscepter.entity.spell.SpellWitherSkullEntity;
import io.github.pistonpoek.magicalscepter.render.entity.model.ModEntityModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.object.skull.SkullModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class SpellWitherSkullEntityRenderer extends EntityRenderer<SpellWitherSkullEntity, SpellWitherSkullEntityRenderState> {
    private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/entity/wither/wither.png");
    private final SkullModel model;

    public SpellWitherSkullEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SkullModel(context.bakeLayer(ModEntityModelLayers.WITHER_SKULL));
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        modelPartData.addOrReplaceChild(PartNames.HEAD,
                CubeListBuilder.create().texOffs(0, 35)
                        .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
                PartPose.ZERO);
        return LayerDefinition.create(modelData, 64, 64);
    }

    protected int getBlockLight(SpellWitherSkullEntity entity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public void submit(SpellWitherSkullEntityRenderState renderState, PoseStack matrixStack,
                       SubmitNodeCollector orderedRenderCommandQueue, CameraRenderState cameraRenderState) {
        matrixStack.pushPose();
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        orderedRenderCommandQueue.submitModel(
                this.model,
                renderState.skullState,
                matrixStack,
                this.model.renderType(TEXTURE),
                renderState.lightCoords,
                OverlayTexture.NO_OVERLAY,
                renderState.outlineColor,
                null
        );
        matrixStack.popPose();
        super.submit(renderState, matrixStack, orderedRenderCommandQueue, cameraRenderState);
    }

    public SpellWitherSkullEntityRenderState createRenderState() {
        return new SpellWitherSkullEntityRenderState();
    }

    public void updateRenderState(SpellWitherSkullEntity entity,
                                  SpellWitherSkullEntityRenderState renderState, float f) {
        super.extractRenderState(entity, renderState, f);
        renderState.skullState.yRot = entity.getYRot(f);
        renderState.skullState.xRot = entity.getXRot(f);
    }
}

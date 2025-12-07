package io.github.pistonpoek.magicalscepter.render.entity;


import io.github.pistonpoek.magicalscepter.entity.projectile.SpellWitherSkullEntity;
import io.github.pistonpoek.magicalscepter.render.entity.model.ModEntityModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

/**
 * TODO
 */
@Environment(EnvType.CLIENT)
public class SpellWitherSkullEntityRenderer extends EntityRenderer<SpellWitherSkullEntity, SpellWitherSkullEntityRenderState> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/wither/wither.png");
    private final SkullEntityModel model;

    /**
     * TODO
     *
     * @param context
     */
    public SpellWitherSkullEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new SkullEntityModel(context.getPart(ModEntityModelLayers.WITHER_SKULL));
    }

    /**
     * TODO
     *
     * @return
     */
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(EntityModelPartNames.HEAD,
                ModelPartBuilder.create().uv(0, 35)
                        .cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
                ModelTransform.NONE);
        return TexturedModelData.of(modelData, 64, 64);
    }

    protected int getBlockLight(SpellWitherSkullEntity entity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public void render(SpellWitherSkullEntityRenderState renderState, MatrixStack matrixStack,
                       OrderedRenderCommandQueue orderedRenderCommandQueue, CameraRenderState cameraRenderState) {
        matrixStack.push();
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        orderedRenderCommandQueue.submitModel(
                this.model,
                renderState.skullState,
                matrixStack,
                this.model.getLayer(TEXTURE),
                renderState.light,
                OverlayTexture.DEFAULT_UV,
                renderState.outlineColor,
                null
        );
        matrixStack.pop();
        super.render(renderState, matrixStack, orderedRenderCommandQueue, cameraRenderState);
    }

    public SpellWitherSkullEntityRenderState createRenderState() {
        return new SpellWitherSkullEntityRenderState();
    }

    public void updateRenderState(SpellWitherSkullEntity entity,
                                  SpellWitherSkullEntityRenderState renderState, float f) {
        super.updateRenderState(entity, renderState, f);
        renderState.skullState.yaw = entity.getLerpedYaw(f);
        renderState.skullState.pitch = entity.getLerpedPitch(f);
    }
}

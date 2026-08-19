package io.github.pistonpoek.magicalscepter.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.pistonpoek.magicalscepter.entity.spell.SpellDragonFireballEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.CommonColors;

@Environment(EnvType.CLIENT)
public class SpellDragonFireballRenderer extends EntityRenderer<SpellDragonFireballEntity, EntityRenderState> {
        private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/entity/enderdragon/dragon_fireball.png");
        private static final RenderType LAYER = RenderTypes.entityCutoutNoCull(TEXTURE);

	public SpellDragonFireballRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

        protected int getBlockLight(SpellDragonFireballEntity dragonFireballEntity, BlockPos blockPos) {
        return 15;
    }

        @Override
        public void submit(EntityRenderState renderState, PoseStack matrices, SubmitNodeCollector
        queue, CameraRenderState cameraState) {
        matrices.pushPose();
        matrices.scale(2.0F, 2.0F, 2.0F);
        matrices.mulPose(cameraState.orientation);
        queue.submitCustomGeometry(matrices, LAYER, (entry, vertexConsumer) -> {
            produceVertex(vertexConsumer, entry, renderState.lightCoords, 0.0F, 0, 0, 1);
            produceVertex(vertexConsumer, entry, renderState.lightCoords, 1.0F, 0, 1, 1);
            produceVertex(vertexConsumer, entry, renderState.lightCoords, 1.0F, 1, 1, 0);
            produceVertex(vertexConsumer, entry, renderState.lightCoords, 0.0F, 1, 0, 0);
        });
        matrices.popPose();
        super.submit(renderState, matrices, queue, cameraState);
    }

        private static void produceVertex(VertexConsumer vertexConsumer, PoseStack.Pose matrix, int light, float x, int z, int textureU, int textureV) {
        vertexConsumer.addVertex(matrix, x - 0.5F, z - 0.25F, 0.0F)
                .setColor(CommonColors.WHITE)
                .setUv(textureU, textureV)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(matrix, 0.0F, 1.0F, 0.0F);
    }

        @Override
        public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}

package io.github.pistonpoek.magicalscepter.render.entity;

import io.github.pistonpoek.magicalscepter.entity.mob.RefractorEntity;
import io.github.pistonpoek.magicalscepter.render.entity.model.ModEntityModelLayers;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.IllagerEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.client.render.entity.state.IllagerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class RefractorEntityRenderer extends IllagerEntityRenderer<RefractorEntity, IllagerEntityRenderState> {
    private static final Identifier TEXTURE = ModIdentifier.of("textures/entity/illager/refractor.png");

    public RefractorEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new IllagerEntityModel<>(context.getPart(ModEntityModelLayers.REFRACTOR)), 0.5F);
        this.addFeature(new HeldItemFeatureRenderer<>(this) {
            public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i,
                               IllagerEntityRenderState renderState, float f, float g) {
                if (renderState.attacking) {
                    super.render(matrixStack, vertexConsumerProvider, i, renderState, f, g);
                }

            }
        });
    }

    @Override
    public void render(IllagerEntityRenderState renderState, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int light) {
        this.model.getHat().visible = !renderState.attacking;
        super.render(renderState, matrixStack, vertexConsumerProvider, light);
    }

    @Override
    public IllagerEntityRenderState createRenderState() {
        return new IllagerEntityRenderState();
    }

    @Override
    public Identifier getTexture(IllagerEntityRenderState state) {
        return TEXTURE;
    }
}

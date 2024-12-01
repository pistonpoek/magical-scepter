package io.github.pistonpoek.magicalscepter.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.IllagerEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import io.github.pistonpoek.magicalscepter.entity.mob.RefractorEntity;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;

@Environment(EnvType.CLIENT)
public class RefractorEntityRenderer extends IllagerEntityRenderer<RefractorEntity, RefractorEntityRenderState> {
    private static final Identifier TEXTURE = ModIdentifier.of("textures/entity/illager/refractor.png");

    public RefractorEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new IllagerEntityModel<>(context.getPart(ModEntityModelLayers.REFRACTOR)), 0.5F);
        this.addFeature(new HeldItemFeatureRenderer<>(this) {
            public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i,
                               RefractorEntityRenderState renderState, float f, float g) {
                this.getContextModel().getHat().visible = renderState.hatVisible;
                if (renderState.attacking) {
                    super.render(matrixStack, vertexConsumerProvider, i, renderState, f, g);
                }

            }
        });
    }

    @Override
    public RefractorEntityRenderState createRenderState() {
        return new RefractorEntityRenderState();
    }

    public void updateRenderState(RefractorEntity refractorEntity,
                                  RefractorEntityRenderState renderState,
                                  float tickDelta) {
        super.updateRenderState(refractorEntity, renderState, tickDelta);
        renderState.hatVisible = !refractorEntity.isAttacking();
    }

    @Override
    public Identifier getTexture(RefractorEntityRenderState state) {
        return TEXTURE;
    }
}

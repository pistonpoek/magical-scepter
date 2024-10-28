package net.pistonpoek.magicalscepter.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.IllagerEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.IllusionerEntity;
import net.minecraft.util.Identifier;
import net.pistonpoek.magicalscepter.entity.mob.RefractorEntity;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;

@Environment(EnvType.CLIENT)
public class RefractorEntityRenderer extends IllagerEntityRenderer<RefractorEntity> {
    private static final Identifier TEXTURE = ModIdentifier.of("textures/entity/illager/refractor.png");

    public RefractorEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new IllagerEntityModel<>(context.getPart(ModEntityModelLayers.REFRACTOR)), 0.5F);
        this.addFeature(
                new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()) {
                    public void render(
                            MatrixStack matrixStack,
                            VertexConsumerProvider vertexConsumerProvider,
                            int i,
                            RefractorEntity refractorEntity,
                            float f,
                            float g,
                            float h,
                            float j,
                            float k,
                            float l
                    ) {
                        if (refractorEntity.isAttacking()) {
                            super.render(matrixStack, vertexConsumerProvider, i, refractorEntity, f, g, h, j, k, l);
                        }
                    }
                }
        );
    }

    public Identifier getTexture(RefractorEntity refractorEntity) {
        return TEXTURE;
    }

    public void render(RefractorEntity refractorEntity, float f, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.model.getHat().visible = !refractorEntity.isAttacking();
        super.render(refractorEntity, f, tickDelta, matrixStack, vertexConsumerProvider, i);
    }
}

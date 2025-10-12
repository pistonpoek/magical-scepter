package io.github.pistonpoek.magicalscepter.render.entity;

import io.github.pistonpoek.magicalscepter.entity.mob.SorcererEntity;
import io.github.pistonpoek.magicalscepter.render.entity.model.ModEntityModelLayers;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.IllagerEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.client.render.entity.state.IllagerEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SorcererEntityRenderer extends IllagerEntityRenderer<SorcererEntity, IllagerEntityRenderState> {
    private static final Identifier TEXTURE = ModIdentifier.of("textures/entity/illager/sorcerer.png");

    public SorcererEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new IllagerEntityModel<>(context.getPart(ModEntityModelLayers.SORCERER)), 0.5F);
        this.addFeature(
                new HeldItemFeatureRenderer<>(this) {
                    public void render(
                            MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, int i,
                            IllagerEntityRenderState renderState, float f, float g
                    ) {
                        if (renderState.attacking) {
                            super.render(matrixStack, orderedRenderCommandQueue, i, renderState, f, g);
                        }
                    }
                }
        );
    }

    @Override
    public void render(IllagerEntityRenderState renderState, MatrixStack matrixStack,
            OrderedRenderCommandQueue orderedRenderCommandQueue, CameraRenderState cameraRenderState) {
        this.model.getHat().visible = !renderState.attacking;
        super.render(renderState, matrixStack, orderedRenderCommandQueue, cameraRenderState);
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

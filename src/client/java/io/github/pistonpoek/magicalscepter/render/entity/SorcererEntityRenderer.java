package io.github.pistonpoek.magicalscepter.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.pistonpoek.magicalscepter.entity.mob.SorcererEntity;
import io.github.pistonpoek.magicalscepter.render.entity.model.ModEntityModelLayers;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.monster.illager.IllagerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.IllagerRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class SorcererEntityRenderer extends IllagerRenderer<SorcererEntity, IllagerRenderState> {
    private static final Identifier TEXTURE = ModIdentifier.of("textures/entity/illager/sorcerer.png");

    public SorcererEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new IllagerModel<>(context.bakeLayer(ModEntityModelLayers.SORCERER)), 0.5F);
        this.addLayer(
                new ItemInHandLayer<>(this) {
                    public void submit(
                            PoseStack matrixStack, SubmitNodeCollector orderedRenderCommandQueue, int i,
                            IllagerRenderState renderState, float f, float g
                    ) {
                        if (renderState.isAggressive) {
                            super.submit(matrixStack, orderedRenderCommandQueue, i, renderState, f, g);
                        }
                    }
                }
        );
    }

    @Override
    public void submit(IllagerRenderState renderState, PoseStack matrixStack,
            SubmitNodeCollector orderedRenderCommandQueue, CameraRenderState cameraRenderState) {
        this.model.getHat().visible = !renderState.isAggressive;
        super.submit(renderState, matrixStack, orderedRenderCommandQueue, cameraRenderState);
    }

    @Override
    public IllagerRenderState createRenderState() {
        return new IllagerRenderState();
    }

    @Override
    public Identifier getTextureLocation(IllagerRenderState state) {
        return TEXTURE;
    }
}

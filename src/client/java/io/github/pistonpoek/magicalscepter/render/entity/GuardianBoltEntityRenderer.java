package io.github.pistonpoek.magicalscepter.render.entity;

import io.github.pistonpoek.magicalscepter.entity.projectile.GuardianBoltEntity;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;
import io.github.pistonpoek.magicalscepter.render.ModRenderLayer;
import io.github.pistonpoek.magicalscepter.render.entity.model.GuardianBoltEntityModel;
import io.github.pistonpoek.magicalscepter.render.entity.model.ModEntityModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class GuardianBoltEntityRenderer extends EntityRenderer<GuardianBoltEntity, GuardianBoltEntityRenderState> {
    private static final Identifier TEXTURE = ModIdentifier.of("textures/entity/projectiles/guardian_bolt.png");
    private final GuardianBoltEntityModel model;

    public GuardianBoltEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new GuardianBoltEntityModel(context.getPart(ModEntityModelLayers.GUARDIAN_BOLT));
    }

    @Override
    public void render(GuardianBoltEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(
                ModRenderLayer.getGuardianBolt(TEXTURE, 0.0F, state.age * 0.03F % 1)
        );
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.sin(state.age * 0.2F) * 180.0F));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.cos(state.age * 0.2F) * 180.0F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.sin(state.age * 0.3F) * 360.0F));
        this.model.setAngles(state);
        this.model.render(matrices, vertexConsumer, LightmapTextureManager.MAX_LIGHT_COORDINATE,
                OverlayTexture.DEFAULT_UV, state.color);
        matrices.pop();
        super.render(state, matrices, vertexConsumers, LightmapTextureManager.MAX_LIGHT_COORDINATE);
    }

    @Override
    public GuardianBoltEntityRenderState createRenderState() {
        return new GuardianBoltEntityRenderState();
    }

    public void updateRenderState(GuardianBoltEntity guardianBoltEntity, GuardianBoltEntityRenderState renderState, float tickDelta) {
        super.updateRenderState(guardianBoltEntity, renderState, tickDelta);
        renderState.yaw = guardianBoltEntity.getLerpedYaw(tickDelta);
        renderState.pitch = guardianBoltEntity.getLerpedPitch(tickDelta);
        renderState.color = getColor(guardianBoltEntity.getProgress(tickDelta));
    }

    private static int getColor(float progress) {
        float transition = progress * progress;
        int red = 64 + (int)(transition * 191.0F);
        int green = 32 + (int)(transition * 191.0F);
        int blue = 128 - (int)(transition * 64.0F);
        return ColorHelper.getArgb(red, green, blue);
    }
}

package io.github.pistonpoek.magicalscepter.render.entity;

import io.github.pistonpoek.magicalscepter.entity.spell.SpellGuardianBeamEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.*;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class SpellGuardianBeamRenderer extends EntityRenderer<SpellGuardianBeamEntity, SpellGuardianBeamRenderState> {
    private static final Identifier EXPLOSION_BEAM_TEXTURE = Identifier.ofVanilla("textures/entity/guardian_beam.png");
    private static final RenderLayer LAYER = RenderLayers.entityCutoutNoCull(EXPLOSION_BEAM_TEXTURE);

    public SpellGuardianBeamRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public boolean shouldRender(SpellGuardianBeamEntity entity, Frustum frustum, double d, double e, double f) {
        if (super.shouldRender(entity, frustum, d, e, f)) {
            return true;
        } else {
            Entity target = entity.getTarget();
            if (target != null) {
                Vec3d targetPos = this.getPosition(target, target.getHeight() * 0.5, 1.0F);
                Vec3d entityPos = this.getPosition(entity, 0.0F, 1.0F);
                return frustum.isVisible(new Box(
                        entityPos.x, entityPos.y, entityPos.z,
                        targetPos.x, targetPos.y, targetPos.z)
                );
            }
            return false;
        }
    }

    private Vec3d getPosition(Entity entity, double yOffset, float delta) {
        double d = MathHelper.lerp(delta, entity.lastRenderX, entity.getX());
        double e = MathHelper.lerp(delta, entity.lastRenderY, entity.getY()) + yOffset;
        double f = MathHelper.lerp(delta, entity.lastRenderZ, entity.getZ());
        return new Vec3d(d, e, f);
    }

    public void render(
            SpellGuardianBeamRenderState state,
            MatrixStack matrixStack,
            OrderedRenderCommandQueue orderedRenderCommandQueue,
            CameraRenderState cameraRenderState
    ) {
        super.render(state, matrixStack, orderedRenderCommandQueue, cameraRenderState);
        Vec3d targetPos = state.targetPos;
        if (targetPos != null) {
            float f = state.age * 0.5F % 1.0F;
            matrixStack.push();
            renderBeam(
                    matrixStack,
                    orderedRenderCommandQueue,
                    targetPos.subtract(state.x, state.y, state.z),
                    state.age,
                    state.progress,
                    f
            );
            matrixStack.pop();
        }
    }

    private static void renderBeam(MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue,
                                   Vec3d vec3d, float age, float progress, float g) {
        float h = (float)(vec3d.length() + 1.0);
        vec3d = vec3d.normalize();
        float i = (float)Math.acos(vec3d.y);
        float j = (float) (Math.PI / 2) - (float)Math.atan2(vec3d.z, vec3d.x);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j * (180.0F / (float)Math.PI)));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(i * (180.0F / (float)Math.PI)));
        float k = age * 0.05F * -1.5F;
        float l = progress * progress;
        int m = 64 + (int)(l * 191.0F);
        int n = 32 + (int)(l * 191.0F);
        int o = 128 - (int)(l * 64.0F);
        float p = 0.2F;
        float q = 0.282F;
        float r = MathHelper.cos(k + (float) (Math.PI * 3.0 / 4.0)) * 0.282F;
        float s = MathHelper.sin(k + (float) (Math.PI * 3.0 / 4.0)) * 0.282F;
        float t = MathHelper.cos(k + (float) (Math.PI / 4)) * 0.282F;
        float u = MathHelper.sin(k + (float) (Math.PI / 4)) * 0.282F;
        float v = MathHelper.cos(k + ((float) Math.PI * 5.0F / 4.0F)) * 0.282F;
        float w = MathHelper.sin(k + ((float) Math.PI * 5.0F / 4.0F)) * 0.282F;
        float x = MathHelper.cos(k + ((float) Math.PI * 7.0F / 4.0F)) * 0.282F;
        float y = MathHelper.sin(k + ((float) Math.PI * 7.0F / 4.0F)) * 0.282F;
        float z = MathHelper.cos(k + (float) Math.PI) * 0.2F;
        float aa = MathHelper.sin(k + (float) Math.PI) * 0.2F;
        float ab = MathHelper.cos(k + 0.0F) * 0.2F;
        float ac = MathHelper.sin(k + 0.0F) * 0.2F;
        float ad = MathHelper.cos(k + (float) (Math.PI / 2)) * 0.2F;
        float ae = MathHelper.sin(k + (float) (Math.PI / 2)) * 0.2F;
        float af = MathHelper.cos(k + (float) (Math.PI * 3.0 / 2.0)) * 0.2F;
        float ag = MathHelper.sin(k + (float) (Math.PI * 3.0 / 2.0)) * 0.2F;
        float ai = 0.0F;
        float aj = 0.4999F;
        float ak = -1.0F + g;
        float al = ak + h * 2.5F;
        orderedRenderCommandQueue.submitCustom(matrixStack, LAYER, (entry, vertexConsumer) -> {
            vertex(vertexConsumer, entry, z, h, aa, m, n, o, 0.4999F, al);
            vertex(vertexConsumer, entry, z, 0.0F, aa, m, n, o, 0.4999F, ak);
            vertex(vertexConsumer, entry, ab, 0.0F, ac, m, n, o, 0.0F, ak);
            vertex(vertexConsumer, entry, ab, h, ac, m, n, o, 0.0F, al);
            vertex(vertexConsumer, entry, ad, h, ae, m, n, o, 0.4999F, al);
            vertex(vertexConsumer, entry, ad, 0.0F, ae, m, n, o, 0.4999F, ak);
            vertex(vertexConsumer, entry, af, 0.0F, ag, m, n, o, 0.0F, ak);
            vertex(vertexConsumer, entry, af, h, ag, m, n, o, 0.0F, al);
            float acx = MathHelper.floor(age) % 2 == 0 ? 0.5F : 0.0F;
            vertex(vertexConsumer, entry, r, h, s, m, n, o, 0.5F, acx + 0.5F);
            vertex(vertexConsumer, entry, t, h, u, m, n, o, 1.0F, acx + 0.5F);
            vertex(vertexConsumer, entry, x, h, y, m, n, o, 1.0F, acx);
            vertex(vertexConsumer, entry, v, h, w, m, n, o, 0.5F, acx);
        });
    }

    private static void vertex(VertexConsumer vertexConsumer, MatrixStack.Entry matrix, float x, float y, float z, int red, int green, int blue, float u, float v) {
        vertexConsumer.vertex(matrix, x, y, z)
                .color(red, green, blue, 255)
                .texture(u, v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
                .normal(matrix, 0.0F, 1.0F, 0.0F);
    }

    public SpellGuardianBeamRenderState createRenderState() {
        return new SpellGuardianBeamRenderState();
    }

    public void updateRenderState(SpellGuardianBeamEntity spellGuardianBeamEntity, SpellGuardianBeamRenderState state, float tickProgress) {
        super.updateRenderState(spellGuardianBeamEntity, state, tickProgress);
        Entity target = spellGuardianBeamEntity.getTarget();
        if (target != null) {
            state.progress = spellGuardianBeamEntity.getProgress(tickProgress);
            state.targetPos = this.getPosition(target, target.getHeight() * 0.5, tickProgress);
        } else {
            state.targetPos = null;
        }
    }
}

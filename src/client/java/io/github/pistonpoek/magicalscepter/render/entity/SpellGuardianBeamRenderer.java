package io.github.pistonpoek.magicalscepter.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.pistonpoek.magicalscepter.entity.spell.SpellGuardianBeamEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class SpellGuardianBeamRenderer extends EntityRenderer<SpellGuardianBeamEntity, SpellGuardianBeamRenderState> {
    private static final Identifier EXPLOSION_BEAM_TEXTURE = Identifier.withDefaultNamespace("textures/entity/guardian_beam.png");
    private static final RenderType LAYER = RenderTypes.entityCutout(EXPLOSION_BEAM_TEXTURE);

    public SpellGuardianBeamRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public boolean shouldRender(SpellGuardianBeamEntity entity, Frustum frustum, double d, double e, double f) {
        if (super.shouldRender(entity, frustum, d, e, f)) {
            return true;
        } else {
            Entity target = entity.getTarget();
            if (target != null) {
                Vec3 targetPos = this.getPosition(target, target.getBbHeight() * 0.5, 1.0F);
                Vec3 entityPos = this.getPosition(entity, 0.0F, 1.0F);
                return frustum.isVisible(new AABB(
                        entityPos.x, entityPos.y, entityPos.z,
                        targetPos.x, targetPos.y, targetPos.z)
                );
            }
            return false;
        }
    }

    private Vec3 getPosition(Entity entity, double yOffset, float delta) {
        double d = Mth.lerp(delta, entity.xOld, entity.getX());
        double e = Mth.lerp(delta, entity.yOld, entity.getY()) + yOffset;
        double f = Mth.lerp(delta, entity.zOld, entity.getZ());
        return new Vec3(d, e, f);
    }

    public void render(
            SpellGuardianBeamRenderState state,
            PoseStack matrixStack,
            SubmitNodeCollector orderedRenderCommandQueue,
            CameraRenderState cameraRenderState
    ) {
        super.submit(state, matrixStack, orderedRenderCommandQueue, cameraRenderState);
        Vec3 targetPos = state.targetPos;
        if (targetPos != null) {
            float f = state.ageInTicks * 0.5F % 1.0F;
            matrixStack.pushPose();
            renderBeam(
                    matrixStack,
                    orderedRenderCommandQueue,
                    targetPos.subtract(state.x, state.y, state.z),
                    state.ageInTicks,
                    state.progress,
                    f
            );
            matrixStack.popPose();
        }
    }

    private static void renderBeam(PoseStack matrixStack, SubmitNodeCollector orderedRenderCommandQueue,
                                   Vec3 vec3d, float age, float progress, float g) {
        float h = (float)(vec3d.length() + 1.0);
        vec3d = vec3d.normalize();
        float i = (float)Math.acos(vec3d.y);
        float j = (float) (Math.PI / 2) - (float)Math.atan2(vec3d.z, vec3d.x);
        matrixStack.mulPose(Axis.YP.rotationDegrees(j * (180.0F / (float)Math.PI)));
        matrixStack.mulPose(Axis.XP.rotationDegrees(i * (180.0F / (float)Math.PI)));
        float k = age * 0.05F * -1.5F;
        float l = progress * progress;
        int m = 64 + (int)(l * 191.0F);
        int n = 32 + (int)(l * 191.0F);
        int o = 128 - (int)(l * 64.0F);
        float p = 0.2F;
        float q = 0.282F;
        float r = Mth.cos(k + (float) (Math.PI * 3.0 / 4.0)) * 0.282F;
        float s = Mth.sin(k + (float) (Math.PI * 3.0 / 4.0)) * 0.282F;
        float t = Mth.cos(k + (float) (Math.PI / 4)) * 0.282F;
        float u = Mth.sin(k + (float) (Math.PI / 4)) * 0.282F;
        float v = Mth.cos(k + ((float) Math.PI * 5.0F / 4.0F)) * 0.282F;
        float w = Mth.sin(k + ((float) Math.PI * 5.0F / 4.0F)) * 0.282F;
        float x = Mth.cos(k + ((float) Math.PI * 7.0F / 4.0F)) * 0.282F;
        float y = Mth.sin(k + ((float) Math.PI * 7.0F / 4.0F)) * 0.282F;
        float z = Mth.cos(k + (float) Math.PI) * 0.2F;
        float aa = Mth.sin(k + (float) Math.PI) * 0.2F;
        float ab = Mth.cos(k + 0.0F) * 0.2F;
        float ac = Mth.sin(k + 0.0F) * 0.2F;
        float ad = Mth.cos(k + (float) (Math.PI / 2)) * 0.2F;
        float ae = Mth.sin(k + (float) (Math.PI / 2)) * 0.2F;
        float af = Mth.cos(k + (float) (Math.PI * 3.0 / 2.0)) * 0.2F;
        float ag = Mth.sin(k + (float) (Math.PI * 3.0 / 2.0)) * 0.2F;
        float ai = 0.0F;
        float aj = 0.4999F;
        float ak = -1.0F + g;
        float al = ak + h * 2.5F;
        orderedRenderCommandQueue.submitCustomGeometry(matrixStack, LAYER, (entry, vertexConsumer) -> {
            vertex(vertexConsumer, entry, z, h, aa, m, n, o, 0.4999F, al);
            vertex(vertexConsumer, entry, z, 0.0F, aa, m, n, o, 0.4999F, ak);
            vertex(vertexConsumer, entry, ab, 0.0F, ac, m, n, o, 0.0F, ak);
            vertex(vertexConsumer, entry, ab, h, ac, m, n, o, 0.0F, al);
            vertex(vertexConsumer, entry, ad, h, ae, m, n, o, 0.4999F, al);
            vertex(vertexConsumer, entry, ad, 0.0F, ae, m, n, o, 0.4999F, ak);
            vertex(vertexConsumer, entry, af, 0.0F, ag, m, n, o, 0.0F, ak);
            vertex(vertexConsumer, entry, af, h, ag, m, n, o, 0.0F, al);
            float acx = Mth.floor(age) % 2 == 0 ? 0.5F : 0.0F;
            vertex(vertexConsumer, entry, r, h, s, m, n, o, 0.5F, acx + 0.5F);
            vertex(vertexConsumer, entry, t, h, u, m, n, o, 1.0F, acx + 0.5F);
            vertex(vertexConsumer, entry, x, h, y, m, n, o, 1.0F, acx);
            vertex(vertexConsumer, entry, v, h, w, m, n, o, 0.5F, acx);
        });
    }

    private static void vertex(VertexConsumer vertexConsumer, PoseStack.Pose matrix, float x, float y, float z, int red, int green, int blue, float u, float v) {
        vertexConsumer.addVertex(matrix, x, y, z)
                .setColor(red, green, blue, 255)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightCoordsUtil.FULL_BRIGHT)
                .setNormal(matrix, 0.0F, 1.0F, 0.0F);
    }

    public SpellGuardianBeamRenderState createRenderState() {
        return new SpellGuardianBeamRenderState();
    }

    public void updateRenderState(SpellGuardianBeamEntity spellGuardianBeamEntity, SpellGuardianBeamRenderState state, float tickProgress) {
        super.extractRenderState(spellGuardianBeamEntity, state, tickProgress);
        Entity target = spellGuardianBeamEntity.getTarget();
        if (target != null) {
            state.progress = spellGuardianBeamEntity.getProgress(tickProgress);
            state.targetPos = this.getPosition(target, target.getBbHeight() * 0.5, tickProgress);
        } else {
            state.targetPos = null;
        }
    }
}

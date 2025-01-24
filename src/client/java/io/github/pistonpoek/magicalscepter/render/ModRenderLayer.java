package io.github.pistonpoek.magicalscepter.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.TriState;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.client.render.RenderLayer
 */
@Environment(EnvType.CLIENT)
public abstract class ModRenderLayer {
    public static RenderLayer getGuardianBolt(Identifier texture, float x, float y) {
        return RenderLayer.of(
                "guardian_bolt",
                VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                VertexFormat.DrawMode.QUADS,
                1536,
                false,
                true,
                RenderLayer.MultiPhaseParameters.builder()
                        .program(RenderPhase.ENERGY_SWIRL_PROGRAM)
                        .texture(new RenderPhase.Texture(texture, TriState.FALSE, false))
                        .texturing(new RenderPhase.OffsetTexturing(x, y))
                        .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
                        .cull(RenderPhase.DISABLE_CULLING)
                        .lightmap(RenderPhase.ENABLE_LIGHTMAP)
                        .overlay(RenderPhase.ENABLE_OVERLAY_COLOR)
                        .build(false)
        );
    }
}

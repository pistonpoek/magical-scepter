package io.github.pistonpoek.magicalscepter.render;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.TriState;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.client.render.RenderLayer
 */
@Environment(EnvType.CLIENT)
public abstract class ModRenderLayer extends RenderLayer {
    public ModRenderLayer(String name, int size, boolean hasCrumbling, boolean translucent, Runnable begin, Runnable end) {
        super(name, size, hasCrumbling, translucent, begin, end);
    }

    public static RenderLayer getGuardianBolt(Identifier texture, float x, float y) {
        return RenderLayer.getEntityTranslucent(texture);
    }
}

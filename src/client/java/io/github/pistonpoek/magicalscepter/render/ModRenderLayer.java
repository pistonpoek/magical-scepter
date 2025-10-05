package io.github.pistonpoek.magicalscepter.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
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

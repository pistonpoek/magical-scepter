package io.github.pistonpoek.magicalscepter.render.entity.model;

import io.github.pistonpoek.magicalscepter.item.SwingType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface ArmSwingingEntityRenderState {
    /**
     * Get the hand swing progress.
     *
     * @return Hand swing progress.
     *
     * @see net.minecraft.entity.LivingEntity#getHandSwingProgress(float)
     */
    float magical_scepter$getHandSwingProgress();

    /**
     * Get the swing type.
     *
     * @return Swing type.
     */
    SwingType magical_scepter$getSwingType();

    /**
     * Set the swing type.
     *
     * @param swingType Swing type to set.
     */
    void magical_scepter$setSwingType(SwingType swingType);
}

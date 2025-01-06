package io.github.pistonpoek.magicalscepter.render.entity.model;

import io.github.pistonpoek.magicalscepter.item.SwingType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface ArmSwingingEntityRenderState {
    float magical_scepter$getHandSwingProgress();
    SwingType magical_scepter$getSwingType();
    void magical_scepter$setSwingType(SwingType swingType);
}

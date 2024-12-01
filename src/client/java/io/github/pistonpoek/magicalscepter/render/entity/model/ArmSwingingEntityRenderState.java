package io.github.pistonpoek.magicalscepter.render.entity.model;

import io.github.pistonpoek.magicalscepter.item.SwingType;

public interface ArmSwingingEntityRenderState {
    float magical_scepter$getHandSwingProgress();
    SwingType magical_scepter$getSwingType();
    void magical_scepter$setSwingType(SwingType swingType);
}

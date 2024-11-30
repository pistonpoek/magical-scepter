package io.github.pistonpoek.magicalscepter.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.IllagerEntityRenderState;

@Environment(EnvType.CLIENT)
public class RefractorEntityRenderState extends IllagerEntityRenderState {
    public boolean wearingHat;

    public RefractorEntityRenderState() {
    }
}
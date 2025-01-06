package io.github.pistonpoek.magicalscepter.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.IllagerEntityRenderState;

@Environment(EnvType.CLIENT)
public class RefractorEntityRenderState extends IllagerEntityRenderState {
    public boolean hatVisible;

    public RefractorEntityRenderState() {
    }
}
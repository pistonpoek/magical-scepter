package io.github.pistonpoek.magicalscepter.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.EntityRenderState;

@Environment(EnvType.CLIENT)
public class WitherSkullEntityRenderState extends EntityRenderState {
    public float pitch;
    public float yaw;
}

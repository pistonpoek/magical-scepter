package io.github.pistonpoek.magicalscepter.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class SpellGuardianBeamRenderState extends EntityRenderState {
    public Vec3d pos;
    public Vec3d targetPos;
    public float progress;
}

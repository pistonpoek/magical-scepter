package io.github.pistonpoek.magicalscepter.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class SpellGuardianBeamRenderState extends EntityRenderState {
    public Vec3 pos;
    public Vec3 targetPos;
    public float progress;
}

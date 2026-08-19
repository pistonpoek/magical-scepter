package io.github.pistonpoek.magicalscepter.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.object.skull.SkullModelBase;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

@Environment(EnvType.CLIENT)
public class SpellWitherSkullEntityRenderState extends EntityRenderState {
    public final SkullModelBase.State skullState = new SkullModelBase.State();
}

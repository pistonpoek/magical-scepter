package io.github.pistonpoek.magicalscepter.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;

/**
 * TODO
 */
@Environment(EnvType.CLIENT)
public class SpellWitherSkullEntityRenderState extends EntityRenderState {
    public final SkullBlockEntityModel.SkullModelState skullState = new SkullBlockEntityModel.SkullModelState();
}

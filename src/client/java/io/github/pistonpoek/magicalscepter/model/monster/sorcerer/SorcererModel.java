package io.github.pistonpoek.magicalscepter.model.monster.sorcerer;

import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.monster.illager.IllagerModel;
import net.minecraft.client.renderer.entity.state.IllagerRenderState;
import net.minecraft.world.entity.monster.illager.AbstractIllager;

public class SorcererModel<S extends IllagerRenderState> extends IllagerModel<S> {
    public HumanoidModel<S> humanoid;

    public SorcererModel(ModelPart root) {
        super(root);
        this.humanoid = new HumanoidModel<>(root);
    }

    public void setupAnim(final S state) {
        super.setupAnim(state);
        getHat().visible = !state.isAggressive;
    }
}

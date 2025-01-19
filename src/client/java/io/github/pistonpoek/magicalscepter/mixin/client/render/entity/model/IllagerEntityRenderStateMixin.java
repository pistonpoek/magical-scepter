package io.github.pistonpoek.magicalscepter.mixin.client.render.entity.model;

import io.github.pistonpoek.magicalscepter.item.SwingType;
import io.github.pistonpoek.magicalscepter.render.entity.model.ArmSwingingEntityRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.IllagerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(IllagerEntityRenderState.class)
public class IllagerEntityRenderStateMixin implements ArmSwingingEntityRenderState {
    @Shadow public float handSwingProgress;
    @Unique
    public SwingType magicalscepter$swingType;

    @Override
    public float magical_scepter$getHandSwingProgress() {
        return this.handSwingProgress;
    }

    @Override
    public SwingType magical_scepter$getSwingType() {
        return magicalscepter$swingType;
    }

    @Override
    public void magical_scepter$setSwingType(SwingType swingType) {
        this.magicalscepter$swingType = swingType;
    }
}

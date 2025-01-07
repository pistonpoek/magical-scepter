package io.github.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.MapCodec;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;

public record RotateSpellEffect() implements SpellEffect {
    public static final MapCodec<RotateSpellEffect> MAP_CODEC = MapCodec.unit(new RotateSpellEffect());

    @Override
    public void apply(SpellContext context) {
        context.target().setPitch(context.pitch());
        context.target().setYaw(context.yaw());
    }

    @Override
    public MapCodec<RotateSpellEffect> getCodec() {
        return MAP_CODEC;
    }
}

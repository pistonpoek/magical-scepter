package net.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.MapCodec;
import net.pistonpoek.magicalscepter.spell.cast.SpellContext;

public record RotateSpellEffect() implements SpellEffect {
    public static final MapCodec<RotateSpellEffect> CODEC = MapCodec.unit(new RotateSpellEffect());

    @Override
    public void apply(SpellContext context) {
        context.target().setPitch(context.pitch());
        context.target().setYaw(context.yaw());
    }

    @Override
    public MapCodec<? extends SpellEffect> getCodec() {
        return CODEC;
    }
}

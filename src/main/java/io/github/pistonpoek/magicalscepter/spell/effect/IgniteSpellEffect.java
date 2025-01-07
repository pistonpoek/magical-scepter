package io.github.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.util.math.intprovider.IntProvider;

public record IgniteSpellEffect(IntProvider duration) implements SpellEffect {
    public static final MapCodec<IgniteSpellEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            IntProvider.VALUE_CODEC.fieldOf("duration").forGetter(IgniteSpellEffect::duration)
                    )
                    .apply(instance, IgniteSpellEffect::new)
    );

    @Override
    public void apply(SpellContext context) {
        context.target().setOnFireFor(this.duration.get(context.getRandom()));
    }

    @Override
    public MapCodec<IgniteSpellEffect> getCodec() {
        return MAP_CODEC;
    }
}

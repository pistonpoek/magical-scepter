package io.github.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;

public record IgniteSpellEffect(FloatProvider duration) implements SpellEffect {
    public static final MapCodec<IgniteSpellEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            FloatProviders.CODEC.fieldOf("duration").forGetter(IgniteSpellEffect::duration)
                    )
                    .apply(instance, IgniteSpellEffect::new)
    );

    @Override
    public void apply(SpellContext context) {
        context.target().igniteForSeconds(this.duration.sample(context.getRandom()));
    }

    @Override
    public MapCodec<IgniteSpellEffect> getCodec() {
        return MAP_CODEC;
    }
}

package io.github.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import io.github.pistonpoek.magicalscepter.spell.cast.delay.DelayedSpellCasting;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public record DelayCastTransformer(int delay) implements CastTransformer {
    public static final MapCodec<DelayCastTransformer> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codecs.POSITIVE_INT.fieldOf("delay").forGetter(DelayCastTransformer::delay)
            ).apply(instance, DelayCastTransformer::new)
    );

    /**
     * Delay the invocation of the specified spell casting.
     *
     * @param casting Spell casting to delay.
     * @param delay Time to delay in ticks.
     * @return Spell casting that will have its invocation delayed.
     */
    public static SpellCasting delay(@NotNull SpellCasting casting, int delay) {
        if (delay <= 0) {
            return casting;
        }
        return new DelayedSpellCasting(casting, delay);
    }

    @Override
    public Collection<SpellCasting> transform(@NotNull SpellCasting casting) {
        return List.of(delay(casting, delay()));
    }

    @Override
    public MapCodec<DelayCastTransformer> getCodec() {
        return CODEC;
    }

    public static Builder builder(int delay) {
        return new Builder(delay);
    }

    public static class Builder {
        private final int delay;

        public Builder(int delay) {
            this.delay = delay;
        }

        public DelayCastTransformer build() {
            return new DelayCastTransformer(delay);
        }
    }
}

package io.github.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import io.github.pistonpoek.magicalscepter.spell.cast.delay.DelayedSpellCasting;
import net.minecraft.util.dynamic.Codecs;

import java.util.Collection;
import java.util.List;

/**
 * TODO
 *
 * @param delay
 */
public record DelayCastTransformer(int delay) implements CastTransformer {
    public static final MapCodec<DelayCastTransformer> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codecs.POSITIVE_INT.fieldOf("delay").forGetter(DelayCastTransformer::delay)
            ).apply(instance, DelayCastTransformer::new)
    );

    /**
     * Delay the invocation of the specified spell casting.
     *
     * @param casting Spell casting to delay.
     * @param delay   Time to delay in ticks.
     * @return Spell casting that will have its invocation delayed.
     */
    public static SpellCasting delay(SpellCasting casting, int delay) {
        if (delay <= 0) {
            return casting.clone();
        }
        return new DelayedSpellCasting(casting.clone(), delay);
    }

    @Override
    public Collection<SpellCasting> transform(SpellCasting casting) {
        return List.of(delay(casting, delay()));
    }

    @Override
    public MapCodec<DelayCastTransformer> getCodec() {
        return MAP_CODEC;
    }

    /**
     * TODO
     *
     * @param delay
     * @return
     */
    public static Builder builder(int delay) {
        return new Builder(delay);
    }

    /**
     * TODO
     */
    public static class Builder {
        private final int delay;

        /**
         * TODO
         *
         * @param delay
         */
        public Builder(int delay) {
            this.delay = delay;
        }

        /**
         * TODO
         *
         * @return
         */
        public DelayCastTransformer build() {
            return new DelayCastTransformer(delay);
        }
    }
}

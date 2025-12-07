package io.github.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import net.minecraft.util.dynamic.Codecs;

import java.util.ArrayList;
import java.util.Collection;

/**
 * TODO
 *
 * @param amount
 * @param stepDelay
 */
public record RepeatCastTransformer(int amount, float stepDelay) implements CastTransformer {
    public static final MapCodec<RepeatCastTransformer> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codecs.NON_NEGATIVE_INT.fieldOf("amount").forGetter(RepeatCastTransformer::amount),
                    Codecs.NON_NEGATIVE_FLOAT.optionalFieldOf("step_delay", 0.0F).forGetter(RepeatCastTransformer::stepDelay)
            ).apply(instance, RepeatCastTransformer::new)
    );

    @Override
    public Collection<SpellCasting> transform(SpellCasting casting) {
        Collection<SpellCasting> casts = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            casts.add(DelayCastTransformer.delay(casting, (int) (i * stepDelay)));
        }
        return casts;
    }

    @Override
    public MapCodec<RepeatCastTransformer> getCodec() {
        return MAP_CODEC;
    }

    /**
     * TODO
     *
     * @param amount
     * @return
     */
    public static Builder builder(int amount) {
        return new Builder(amount);
    }

    /**
     * TODO
     */
    public static class Builder {
        private final int amount;
        private float stepDelay;

        /**
         * TODO
         *
         * @param amount
         */
        public Builder(int amount) {
            this.amount = amount;
        }

        /**
         * TODO
         *
         * @param stepDelay
         * @return
         */
        public Builder stepDelay(float stepDelay) {
            this.stepDelay = stepDelay;
            return this;
        }

        /**
         * TODO
         *
         * @return
         */
        public RepeatCastTransformer build() {
            return new RepeatCastTransformer(amount, stepDelay);
        }
    }
}

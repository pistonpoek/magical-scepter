package io.github.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.ContextSourceList;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public record RepeatCastTransformer(int amount, int stepDelay) implements CastTransformer {
    public static final MapCodec<RepeatCastTransformer> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codecs.NON_NEGATIVE_INT.fieldOf("amount").forGetter(RepeatCastTransformer::amount),
                    Codecs.NON_NEGATIVE_INT.optionalFieldOf("step_delay", 0).forGetter(RepeatCastTransformer::stepDelay)
            ).apply(instance, RepeatCastTransformer::new)
    );

    @Override
    public Collection<SpellCasting> transform(@NotNull SpellCasting casting) {
        casting.addContext(new ContextSourceList(casting.getContext()));

        Collection<SpellCasting> casts = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            casts.add(DelayCastTransformer.delay(casting, i * stepDelay));
        }
        return casts;
    }

    @Override
    public MapCodec<RepeatCastTransformer> getCodec() {
        return CODEC;
    }

    public static Builder builder(int amount) {
        return new Builder(amount);
    }

    public static class Builder {
        private final int amount;
        private int stepDelay;

        public Builder(int amount) {
            this.amount = amount;
        }

        public Builder stepDelay(int stepDelay) {
            this.stepDelay = stepDelay;
            return this;
        }

        public RepeatCastTransformer build() {
            return new RepeatCastTransformer(amount, stepDelay);
        }
    }
}

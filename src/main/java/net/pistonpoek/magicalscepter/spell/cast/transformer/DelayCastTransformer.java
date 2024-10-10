package net.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.pistonpoek.magicalscepter.spell.cast.Cast;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public record DelayCastTransformer(int delay) implements CastTransformer {
    public static final MapCodec<DelayCastTransformer> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.INT.fieldOf("delay").forGetter(DelayCastTransformer::delay)
            ).apply(instance, DelayCastTransformer::new)
    );

    @Override
    public Collection<Cast> transform(@NotNull Cast cast) {
        return List.of(cast.setDelay(delay));
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

package io.github.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import io.github.pistonpoek.magicalscepter.spell.position.PositionSource;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public record MoveCastTransformer(PositionSource position) implements CastTransformer {
    public static final MapCodec<MoveCastTransformer> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    PositionSource.CODEC.fieldOf("position").forGetter(MoveCastTransformer::position)
            ).apply(instance, MoveCastTransformer::new)
    );

    @Override
    public Collection<SpellCasting> transform(@NotNull SpellCasting casting) {
        return List.of(casting.addContext(position));
    }

    @Override
    public MapCodec<MoveCastTransformer> getCodec() {
        return MAP_CODEC;
    }

    public static Builder builder(PositionSource position) {
        return new Builder(position);
    }

    public static class Builder {
        private final PositionSource position;

        public Builder(PositionSource position) {
            this.position = position;
        }

        public MoveCastTransformer build() {
            return new MoveCastTransformer(position);
        }
    }
}

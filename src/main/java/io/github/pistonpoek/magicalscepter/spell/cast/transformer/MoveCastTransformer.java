package io.github.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import io.github.pistonpoek.magicalscepter.spell.position.PositionSource;

import java.util.Collection;
import java.util.List;

/**
 * TODO
 *
 * @param position
 */
public record MoveCastTransformer(PositionSource position) implements CastTransformer {
    public static final MapCodec<MoveCastTransformer> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    PositionSource.CODEC.fieldOf("position").forGetter(MoveCastTransformer::position)
            ).apply(instance, MoveCastTransformer::new)
    );

    @Override
    public Collection<SpellCasting> transform(SpellCasting casting) {
        return List.of(casting.addContext(position));
    }

    @Override
    public MapCodec<MoveCastTransformer> getCodec() {
        return MAP_CODEC;
    }

    /**
     * TODO
     *
     * @param position
     * @return
     */
    public static Builder builder(PositionSource position) {
        return new Builder(position);
    }

    /**
     * TODO
     */
    public static class Builder {
        /**
         * TODO
         */
        private final PositionSource position;

        /**
         * TODO
         *
         * @param position
         */
        public Builder(PositionSource position) {
            this.position = position;
        }

        /**
         * TODO
         *
         * @return
         */
        public MoveCastTransformer build() {
            return new MoveCastTransformer(position);
        }
    }
}

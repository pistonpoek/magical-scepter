package io.github.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import io.github.pistonpoek.magicalscepter.spell.rotation.RotationSource;

import java.util.Collection;
import java.util.List;

/**
 * TODO
 *
 * @param rotation
 */
public record RotateCastTransformer(RotationSource rotation) implements CastTransformer {
    public static final MapCodec<RotateCastTransformer> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    RotationSource.CODEC.fieldOf("rotation").forGetter(RotateCastTransformer::rotation)
            ).apply(instance, RotateCastTransformer::new)
    );

    @Override
    public Collection<SpellCasting> transform(SpellCasting casting) {
        return List.of(casting.addContext(rotation));
    }

    @Override
    public MapCodec<RotateCastTransformer> getCodec() {
        return MAP_CODEC;
    }

    /**
     * TODO
     *
     * @param rotation
     * @return
     */
    public static Builder builder(RotationSource rotation) {
        return new Builder(rotation);
    }

    /**
     * TODO
     */
    public static class Builder {
        private final RotationSource rotation;

        /**
         * TODO
         *
         * @param rotation
         */
        public Builder(RotationSource rotation) {
            this.rotation = rotation;
        }

        /**
         * TODO
         *
         * @return
         */
        public RotateCastTransformer build() {
            return new RotateCastTransformer(rotation);
        }
    }
}

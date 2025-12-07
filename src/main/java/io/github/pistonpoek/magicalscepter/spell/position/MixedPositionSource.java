package io.github.pistonpoek.magicalscepter.spell.position;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

/**
 * TODO
 *
 * @param x
 * @param y
 * @param z
 */
public record MixedPositionSource(Optional<PositionSource> x,
                                  Optional<PositionSource> y,
                                  Optional<PositionSource> z) implements PositionSource {
    public static final MapCodec<MixedPositionSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    PositionSource.CODEC.optionalFieldOf("x").forGetter(MixedPositionSource::x),
                    PositionSource.CODEC.optionalFieldOf("y").forGetter(MixedPositionSource::y),
                    PositionSource.CODEC.optionalFieldOf("z").forGetter(MixedPositionSource::z)
            ).apply(instance, MixedPositionSource::new)
    );

    @Override
    public Vec3d getPosition(SpellContext context) {
        return new Vec3d(
                x.map(x -> x.getX(context)).orElse(context.getX()),
                y.map(y -> y.getY(context)).orElse(context.getY()),
                z.map(z -> z.getZ(context)).orElse(context.getZ()));
    }

    @Override
    public MapCodec<MixedPositionSource> getCodec() {
        return MAP_CODEC;
    }

    /**
     * TODO
     *
     * @return
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * TODO
     */
    public static class Builder {
        private PositionSource xPosition = null;
        private PositionSource yPosition = null;
        private PositionSource zPosition = null;

        /**
         * TODO
         *
         * @param position
         * @return
         */
        public Builder xPosition(PositionSource position) {
            this.xPosition = position;
            return this;
        }

        /**
         * TODO
         *
         * @param position
         * @return
         */
        public Builder yPosition(PositionSource position) {
            this.yPosition = position;
            return this;
        }

        /**
         * TODO
         *
         * @param position
         * @return
         */
        public Builder zPosition(PositionSource position) {
            this.zPosition = position;
            return this;
        }

        /**
         * TODO
         *
         * @return
         */
        public MixedPositionSource build() {
            return new MixedPositionSource(
                    Optional.ofNullable(xPosition),
                    Optional.ofNullable(yPosition),
                    Optional.ofNullable(zPosition)
            );
        }

    }

}

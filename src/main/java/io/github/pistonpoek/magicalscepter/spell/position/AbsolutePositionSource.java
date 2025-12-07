package io.github.pistonpoek.magicalscepter.spell.position;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.util.math.Vec3d;

/**
 * TODO
 *
 * @param x
 * @param y
 * @param z
 */
public record AbsolutePositionSource(double x, double y, double z) implements PositionSource {
    public static final MapCodec<AbsolutePositionSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.DOUBLE.fieldOf("x").forGetter(AbsolutePositionSource::x),
                    Codec.DOUBLE.fieldOf("y").forGetter(AbsolutePositionSource::y),
                    Codec.DOUBLE.fieldOf("z").forGetter(AbsolutePositionSource::z)
            ).apply(instance, AbsolutePositionSource::new)
    );

    /**
     * TODO
     *
     * @param position
     */
    public AbsolutePositionSource(Vec3d position) {
        this(position.x, position.y, position.z);
    }

    @Override
    public Vec3d getPosition(SpellContext context) {
        return new Vec3d(x, y, z);
    }

    @Override
    public MapCodec<AbsolutePositionSource> getCodec() {
        return MAP_CODEC;
    }

    /**
     * TODO
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public static Builder builder(double x, double y, double z) {
        return new Builder(x, y, z);
    }

    /**
     * TODO
     *
     * @param vector
     * @return
     */
    public static Builder builder(Vec3d vector) {
        return new Builder(vector.x, vector.y, vector.z);
    }

    /**
     * TODO
     */
    public static class Builder {
        private final double x;
        private final double y;
        private final double z;

        /**
         * TODO
         *
         * @param x
         * @param y
         * @param z
         */
        public Builder(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        /**
         * TODO
         *
         * @return
         */
        public AbsolutePositionSource build() {
            return new AbsolutePositionSource(x, y, z);
        }
    }

}

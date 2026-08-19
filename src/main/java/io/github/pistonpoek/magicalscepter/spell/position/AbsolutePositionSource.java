package io.github.pistonpoek.magicalscepter.spell.position;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record AbsolutePositionSource(double x, double y, double z) implements PositionSource {
    public static MapCodec<AbsolutePositionSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.DOUBLE.fieldOf("x").forGetter(AbsolutePositionSource::x),
                    Codec.DOUBLE.fieldOf("y").forGetter(AbsolutePositionSource::y),
                    Codec.DOUBLE.fieldOf("z").forGetter(AbsolutePositionSource::z)
            ).apply(instance, AbsolutePositionSource::new)
    );

    public AbsolutePositionSource(Vec3 position) {
        this(position.x, position.y, position.z);
    }

    @Override
    public Vec3 getPosition(@NotNull SpellContext context) {
        return new Vec3(x, y, z);
    }

    @Override
    public MapCodec<AbsolutePositionSource> getCodec() {
        return MAP_CODEC;
    }

    public static Builder builder(double x, double y, double z) {
        return new Builder(x, y, z);
    }

    public static Builder builder(Vec3 vector) {
        return new Builder(vector.x, vector.y, vector.z);
    }

    public static class Builder {
        private final double x;
        private final double y;
        private final double z;

        public Builder(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public AbsolutePositionSource build() {
            return new AbsolutePositionSource(x, y, z);
        }
    }

}

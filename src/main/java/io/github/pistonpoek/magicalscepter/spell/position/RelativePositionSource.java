package io.github.pistonpoek.magicalscepter.spell.position;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import io.github.pistonpoek.magicalscepter.spell.rotation.RotationSource;
import io.github.pistonpoek.magicalscepter.util.RotationVector;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record RelativePositionSource(double x, double y, double z,
                                     Optional<PositionSource> position,
                                     Optional<RotationSource> rotation) implements PositionSource {
    static MapCodec<RelativePositionSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.DOUBLE.fieldOf("x").forGetter(RelativePositionSource::x),
                    Codec.DOUBLE.fieldOf("y").forGetter(RelativePositionSource::y),
                    Codec.DOUBLE.fieldOf("z").forGetter(RelativePositionSource::z),
                    PositionSource.CODEC.optionalFieldOf("position").forGetter(RelativePositionSource::position),
                    RotationSource.CODEC.optionalFieldOf("rotation").forGetter(RelativePositionSource::rotation)
            ).apply(instance, RelativePositionSource::new)
    );

    @Override
    public Vec3d getPosition(@NotNull SpellContext context) {
        Vec3d vector = position.map(position -> position.getPosition(context)).orElse(context.position());
        float pitch = rotation.map(rotation -> rotation.getPitch(context)).orElse(context.pitch());
        float yaw = rotation.map(rotation -> rotation.getYaw(context)).orElse(context.yaw());
        return getRelativeVector(vector, pitch, yaw, x, y, z);
    }

    private Vec3d getRelativeVector(Vec3d base, float pitch, float yaw, double x, double y, double z) {
        return base
                .add(RotationVector.get(0, MathHelper.wrapDegrees(yaw - 90)).normalize().multiply(x))
                .add(RotationVector.get(MathHelper.wrapDegrees(pitch - 90), yaw).normalize().multiply(y))
                .add(RotationVector.get(pitch, yaw).normalize().multiply(z));
    }

    @Override
    public MapCodec<RelativePositionSource> getCodec() {
        return RelativePositionSource.MAP_CODEC;
    }

    public static Builder builder(double x, double y, double z) {
        return new Builder(x, y, z);
    }

    public static Builder builder(Vec3d vector) {
        return new Builder(vector.x, vector.y, vector.z);
    }

    public static class Builder {
        private final double x;
        private final double y;
        private final double z;
        private PositionSource position = null;
        private RotationSource rotation = null;

        public Builder(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Builder position(PositionSource position) {
            this.position = position;
            return this;
        }

        public Builder rotation(RotationSource rotation) {
            this.rotation = rotation;
            return this;
        }

        public RelativePositionSource build() {
            return new RelativePositionSource(
                    x, y, z,
                    Optional.ofNullable(position),
                    Optional.ofNullable(rotation)
            );
        }
    }
}

package io.github.pistonpoek.magicalscepter.spell.position;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import io.github.pistonpoek.magicalscepter.spell.rotation.RotationSource;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record RandomPositionSource(double dx, double dy, double dz,
                                   Optional<PositionSource> position,
                                   Optional<RotationSource> rotation) implements PositionSource {
    static MapCodec<RandomPositionSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.DOUBLE.fieldOf("dx").forGetter(RandomPositionSource::dx),
                    Codec.DOUBLE.fieldOf("dy").forGetter(RandomPositionSource::dy),
                    Codec.DOUBLE.fieldOf("dz").forGetter(RandomPositionSource::dz),
                    PositionSource.CODEC.optionalFieldOf("position").forGetter(RandomPositionSource::position),
                    RotationSource.CODEC.optionalFieldOf("rotation").forGetter(RandomPositionSource::rotation)
            ).apply(instance, RandomPositionSource::new)
    );

    @Override
    public Vec3d getPosition(@NotNull SpellContext context) {
        Random random = context.caster().getRandom();
        PositionSource position = getRandomPositionSource(random);
        return position.getPosition(context);
    }

    private PositionSource getRandomPositionSource(Random random) {
        RelativePositionSource.Builder builder =
                RelativePositionSource.builder(
                        random.nextDouble() * dx,
                        random.nextDouble() * dy,
                        random.nextDouble() * dz
                );
        position.ifPresent(builder::position);
        rotation.ifPresent(builder::rotation);
        return builder.build();
    }

    @Override
    public MapCodec<? extends PositionSource> getCodec() {
        return MAP_CODEC;
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

        public RandomPositionSource build() {
            return new RandomPositionSource(
                    x, y, z,
                    Optional.ofNullable(position),
                    Optional.ofNullable(rotation)
            );
        }
    }

}

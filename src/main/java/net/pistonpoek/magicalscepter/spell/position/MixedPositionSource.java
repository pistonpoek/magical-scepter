package net.pistonpoek.magicalscepter.spell.position;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.Vec3d;
import net.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import net.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record MixedPositionSource(Optional<PositionSource> x,
                                  Optional<PositionSource> y,
                                  Optional<PositionSource> z) implements PositionSource {
    static MapCodec<MixedPositionSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    PositionSource.CODEC.optionalFieldOf("x").forGetter(MixedPositionSource::x),
                    PositionSource.CODEC.optionalFieldOf("y").forGetter(MixedPositionSource::y),
                    PositionSource.CODEC.optionalFieldOf("z").forGetter(MixedPositionSource::z)
            ).apply(instance, MixedPositionSource::new)
    );

    @Override
    public Vec3d getPosition(@NotNull SpellContext context) {
        return new Vec3d(
                x.map(x -> x.getX(context)).orElse(context.getX()),
                y.map(y -> y.getY(context)).orElse(context.getY()),
                z.map(z -> z.getZ(context)).orElse(context.getZ()));
    }

    @Override
    public MapCodec<MixedPositionSource> getCodec() {
        return CODEC;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private PositionSource xPosition = null;
        private PositionSource yPosition = null;
        private PositionSource zPosition = null;

        public Builder xPosition(PositionSource position) {
            this.xPosition = position;
            return this;
        }

        public Builder yPosition(PositionSource position) {
            this.yPosition = position;
            return this;
        }

        public Builder zPosition(PositionSource position) {
            this.zPosition = position;
            return this;
        }

        public MixedPositionSource build() {
            return new MixedPositionSource(
                Optional.ofNullable(xPosition),
                Optional.ofNullable(yPosition),
                Optional.ofNullable(zPosition)
            );
        }

    }

}

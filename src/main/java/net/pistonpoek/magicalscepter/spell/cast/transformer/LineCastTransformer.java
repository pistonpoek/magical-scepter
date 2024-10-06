package net.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.Vec3d;
import net.pistonpoek.magicalscepter.spell.cast.Cast;
import net.pistonpoek.magicalscepter.spell.cast.PositionSource;
import net.pistonpoek.magicalscepter.spell.cast.ValuePositionSource;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public record LineCastTransformer(PositionSource startPosition,
                                  PositionSource endPosition,
                                  int amount,
                                  int stepDelay) implements CastTransformer {
    public static final MapCodec<LineCastTransformer> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    PositionSource.CODEC.fieldOf("start_position").forGetter(LineCastTransformer::startPosition),
                    PositionSource.CODEC.fieldOf("end_position").forGetter(LineCastTransformer::endPosition),
                    Codec.INT.fieldOf("amount").forGetter(LineCastTransformer::amount),
                    Codec.INT.optionalFieldOf("step_delay", 0).forGetter(LineCastTransformer::stepDelay)
            ).apply(instance, LineCastTransformer::new)
    );

    @Override
    public Collection<Cast> transform(@NotNull Cast cast) {
        Vec3d startPos = startPosition.getPosition(cast.getContext());
        Vec3d lineVector = endPosition.getPosition(cast.getContext()).subtract(startPos);
        Collection<Cast> casts = new ArrayList<>();
        for (int i = 0; i <= amount; i++) {
            Cast pointCast = cast.clone();
            pointCast.setDelay(cast.getDelay() + i * stepDelay);
            pointCast.setPosition(
                    new ValuePositionSource(startPos.add(lineVector.multiply(((double)i) / (amount - 1)))));
            casts.add(pointCast);
        }
        return casts;
    }

    public static LineCastTransformer.Builder builder(int amount,
                                                      PositionSource startPosition,
                                                      PositionSource endPosition) {
        return new LineCastTransformer.Builder(startPosition, endPosition, amount);
    }

    public static class Builder {
        private final PositionSource startPosition;
        private final PositionSource endPosition;
        private final int amount;
        private int stepDelay = 0;

        public Builder(PositionSource startPosition,
                       PositionSource endPosition,
                       int amount) {
            this.startPosition = startPosition;
            this.endPosition = endPosition;
            this.amount = amount;
        }

        public LineCastTransformer.Builder stepDelay(int stepDelay) {
            this.stepDelay = stepDelay;
            return this;
        }

        public LineCastTransformer build() {
            return new LineCastTransformer(startPosition, endPosition, amount, stepDelay);
        }
    }

    @Override
    public MapCodec<? extends CastTransformer> getCodec() {
        return CODEC;
    }
}


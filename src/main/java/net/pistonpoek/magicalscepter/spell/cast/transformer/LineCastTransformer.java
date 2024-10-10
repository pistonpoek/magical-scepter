package net.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.Vec3d;
import net.pistonpoek.magicalscepter.spell.cast.Cast;
import net.pistonpoek.magicalscepter.spell.position.PositionSource;
import net.pistonpoek.magicalscepter.spell.position.AbsolutePositionSource;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public record LineCastTransformer(PositionSource position,
                                  int amount,
                                  int stepDelay) implements CastTransformer {
    public static final MapCodec<LineCastTransformer> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    PositionSource.CODEC.fieldOf("position").forGetter(LineCastTransformer::position),
                    Codec.INT.fieldOf("amount").forGetter(LineCastTransformer::amount),
                    Codec.INT.optionalFieldOf("step_delay", 0).forGetter(LineCastTransformer::stepDelay)
            ).apply(instance, LineCastTransformer::new)
    );

    @Override
    public Collection<Cast> transform(@NotNull Cast cast) {
        Vec3d startPos = cast.getPositionSource().getPosition(cast.getContext());
        Vec3d lineVector = position.getPosition(cast.getContext()).subtract(startPos);
        Collection<Cast> casts = new ArrayList<>();
        for (int i = 0; i <= amount; i++) {
            Cast pointCast = cast.clone();
            pointCast.setDelay(cast.getDelay() + i * stepDelay);
            pointCast.setPosition(AbsolutePositionSource.builder(
                    startPos.add(lineVector.multiply(((double)i) / (amount - 1)))).build());
            casts.add(pointCast);
        }
        return casts;
    }

    public static Builder builder(int amount, PositionSource position) {
        return new Builder(position, amount);
    }

    public static class Builder {
        private final PositionSource position;
        private final int amount;
        private int stepDelay = 0;

        public Builder(PositionSource position,
                       int amount) {
            this.position = position;
            this.amount = amount;
        }

        public Builder stepDelay(int stepDelay) {
            this.stepDelay = stepDelay;
            return this;
        }

        public LineCastTransformer build() {
            return new LineCastTransformer(position, amount, stepDelay);
        }
    }

    @Override
    public MapCodec<LineCastTransformer> getCodec() {
        return CODEC;
    }
}


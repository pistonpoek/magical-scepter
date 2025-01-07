package io.github.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.Vec3d;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import io.github.pistonpoek.magicalscepter.spell.position.PositionSource;
import io.github.pistonpoek.magicalscepter.spell.position.AbsolutePositionSource;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public record LineCastTransformer(PositionSource position,
                                  int amount,
                                  int stepDelay) implements CastTransformer {
    public static final MapCodec<LineCastTransformer> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    PositionSource.CODEC.fieldOf("position").forGetter(LineCastTransformer::position),
                    Codecs.NON_NEGATIVE_INT.fieldOf("amount").forGetter(LineCastTransformer::amount),
                    Codecs.NON_NEGATIVE_INT.optionalFieldOf("step_delay", 0).forGetter(LineCastTransformer::stepDelay)
            ).apply(instance, LineCastTransformer::new)
    );

    @Override
    public Collection<SpellCasting> transform(@NotNull SpellCasting casting) {
        SpellContext context = casting.getContext();
        Vec3d startPos = context.position();
        Vec3d lineVector = position.getPosition(context).subtract(startPos);
        Collection<SpellCasting> casts = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            SpellCasting pointCast = DelayCastTransformer.delay(casting, i * stepDelay);
            pointCast.addContext(AbsolutePositionSource.builder(
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


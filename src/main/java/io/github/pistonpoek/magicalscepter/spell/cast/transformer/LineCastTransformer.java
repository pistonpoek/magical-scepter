package io.github.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import io.github.pistonpoek.magicalscepter.spell.position.AbsolutePositionSource;
import io.github.pistonpoek.magicalscepter.spell.position.PositionSource;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Collection;

/**
 * TODO
 *
 * @param position
 * @param amount
 * @param stepDelay
 */
public record LineCastTransformer(PositionSource position,
                                  int amount,
                                  float stepDelay) implements CastTransformer {
    public static final MapCodec<LineCastTransformer> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    PositionSource.CODEC.fieldOf("position").forGetter(LineCastTransformer::position),
                    Codecs.NON_NEGATIVE_INT.fieldOf("amount").forGetter(LineCastTransformer::amount),
                    Codecs.NON_NEGATIVE_FLOAT.optionalFieldOf("step_delay", 0.0F).forGetter(LineCastTransformer::stepDelay)
            ).apply(instance, LineCastTransformer::new)
    );

    @Override
    public Collection<SpellCasting> transform(SpellCasting casting) {
        SpellContext context = casting.getContext();
        Vec3d startPos = context.position();
        Vec3d lineVector = position.getPosition(context).subtract(startPos);
        Collection<SpellCasting> casts = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            SpellCasting pointCast = DelayCastTransformer.delay(casting, (int) (i * stepDelay));
            pointCast.addContext(AbsolutePositionSource.builder(
                    startPos.add(lineVector.multiply(((double) i) / (amount - 1)))).build());
            casts.add(pointCast);
        }
        return casts;
    }

    /**
     * TODO
     *
     * @param amount
     * @param position
     * @return
     */
    public static Builder builder(int amount, PositionSource position) {
        return new Builder(position, amount);
    }

    /**
     * TODO
     */
    public static class Builder {
        private final PositionSource position;
        private final int amount;
        private float stepDelay = 0.0F;

        /**
         * TODO
         *
         * @param position
         * @param amount
         */
        public Builder(PositionSource position,
                       int amount) {
            this.position = position;
            this.amount = amount;
        }

        /**
         * TODO
         *
         * @param stepDelay
         * @return
         */
        public Builder stepDelay(float stepDelay) {
            this.stepDelay = stepDelay;
            return this;
        }

        /**
         * TODO
         *
         * @return
         */
        public LineCastTransformer build() {
            return new LineCastTransformer(position, amount, stepDelay);
        }
    }

    @Override
    public MapCodec<LineCastTransformer> getCodec() {
        return MAP_CODEC;
    }
}


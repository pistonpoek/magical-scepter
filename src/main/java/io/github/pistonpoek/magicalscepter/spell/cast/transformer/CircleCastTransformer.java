package io.github.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import io.github.pistonpoek.magicalscepter.spell.position.AbsolutePositionSource;
import io.github.pistonpoek.magicalscepter.spell.position.PositionSource;
import io.github.pistonpoek.magicalscepter.spell.position.RelativePositionSource;
import io.github.pistonpoek.magicalscepter.spell.rotation.AbsoluteRotationSource;
import io.github.pistonpoek.magicalscepter.spell.rotation.FacingLocationRotationSource;
import io.github.pistonpoek.magicalscepter.spell.rotation.RotationSource;
import net.minecraft.util.Pair;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public record CircleCastTransformer(PositionSource position, float direction, float arc, int amount,
                                    float stepDelay) implements CastTransformer {
    public static final MapCodec<CircleCastTransformer> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    PositionSource.CODEC.fieldOf("position").forGetter(CircleCastTransformer::position),
                    Codec.floatRange(180.0F, -180.0F).optionalFieldOf("direction", 0.0F).forGetter(CircleCastTransformer::direction),
                    Codec.FLOAT.optionalFieldOf("arc", 360.0F).forGetter(CircleCastTransformer::arc),
                    Codecs.NON_NEGATIVE_INT.fieldOf("amount").forGetter(CircleCastTransformer::amount),
                    Codecs.NON_NEGATIVE_FLOAT.optionalFieldOf("step_delay", 0.0F).forGetter(CircleCastTransformer::stepDelay)
            ).apply(instance, CircleCastTransformer::new)
    );

    @Override
    public Collection<SpellCasting> transform(@NotNull SpellCasting casting) {
        SpellContext context = casting.getContext();
        RotationSource rotation = new FacingLocationRotationSource(position);
        Vec3d centerPos = context.position();
        double radius = position.getPosition(context).subtract(centerPos).length();
        double x = Math.cos(Math.toRadians(direction));
        double y = Math.sin(Math.toRadians(direction));

        Collection<SpellCasting> casts = new ArrayList<>();
        double radianStep = Math.toRadians(arc) / amount;
        for (int i = 0; i < amount; i++) {
            SpellCasting pointCast = DelayCastTransformer.delay(casting, (int) (i * stepDelay));
            double angle = radianStep * i;

            Vec3d relativePosition = new Vec3d(
                    x * Math.sin(angle),
                    y * Math.sin(angle),
                    Math.cos(angle)
            ).multiply(radius);

            PositionSource absolutePosition = AbsolutePositionSource.builder(
                    RelativePositionSource.builder(relativePosition)
                            .rotation(rotation).build().getPosition(context)).build();
            pointCast.addContext(absolutePosition);

            Pair<Float, Float> pointRotation = new FacingLocationRotationSource(absolutePosition).getRotation(context);
            RotationSource absoluteRotation = new AbsoluteRotationSource(pointRotation.getLeft(), pointRotation.getRight());
            pointCast.addContext(absoluteRotation);
            casts.add(pointCast);
        }
        return casts;
    }

    @Override
    public MapCodec<CircleCastTransformer> getCodec() {
        return MAP_CODEC;
    }

    public static Builder builder(PositionSource position, int amount) {
        return new Builder(position, amount);
    }

    public static class Builder {
        private final PositionSource position;
        private float direction = 0.0F;
        private float arc = 360.0F;
        private final int amount;
        private float stepDelay = 0.0F;

        public Builder(PositionSource position, int amount) {
            this.position = position;
            this.amount = amount;
        }

        public Builder direction(float direction) {
            this.direction = direction;
            return this;
        }

        public Builder arc(float arc) {
            this.arc = arc;
            return this;
        }

        public Builder stepDelay(float stepDelay) {
            this.stepDelay = stepDelay;
            return this;
        }

        public CircleCastTransformer build() {
            return new CircleCastTransformer(position, direction, arc, amount, stepDelay);
        }
    }

}

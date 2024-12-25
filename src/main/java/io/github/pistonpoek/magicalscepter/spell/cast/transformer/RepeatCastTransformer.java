package io.github.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.Vec3d;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import io.github.pistonpoek.magicalscepter.spell.position.AbsolutePositionSource;
import io.github.pistonpoek.magicalscepter.spell.rotation.AbsoluteRotationSource;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public record RepeatCastTransformer(int amount, int stepDelay) implements CastTransformer {
    public static final MapCodec<RepeatCastTransformer> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.INT.fieldOf("amount").forGetter(RepeatCastTransformer::amount),
                    Codec.INT.optionalFieldOf("step_delay", 0).forGetter(RepeatCastTransformer::stepDelay)
            ).apply(instance, RepeatCastTransformer::new)
    );

    @Override
    public Collection<SpellCasting> transform(@NotNull SpellCasting cast) {
        SpellContext context = cast.getContext();
        Vec3d position = context.position();
        cast.addContextSource(new AbsolutePositionSource(position.x, position.y, position.z));
        cast.addContextSource(new AbsoluteRotationSource(context.pitch(), context.yaw()));

        Collection<SpellCasting> casts = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            casts.add(cast.clone().setDelay(cast.getDelay() + i * stepDelay));
        }
        return casts;
    }

    @Override
    public MapCodec<RepeatCastTransformer> getCodec() {
        return CODEC;
    }

    public static Builder builder(int amount) {
        return new Builder(amount);
    }

    public static class Builder {
        private final int amount;
        private int stepDelay;

        public Builder(int amount) {
            this.amount = amount;
        }

        public Builder stepDelay(int stepDelay) {
            this.stepDelay = stepDelay;
            return this;
        }

        public RepeatCastTransformer build() {
            return new RepeatCastTransformer(amount, stepDelay);
        }
    }
}

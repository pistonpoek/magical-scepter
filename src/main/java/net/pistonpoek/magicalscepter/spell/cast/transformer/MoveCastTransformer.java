package net.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import net.pistonpoek.magicalscepter.spell.position.PositionSource;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public record MoveCastTransformer(PositionSource position) implements CastTransformer {
    public static final MapCodec<MoveCastTransformer> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    PositionSource.CODEC.fieldOf("position").forGetter(MoveCastTransformer::position)
            ).apply(instance, MoveCastTransformer::new)
    );

    @Override
    public Collection<SpellCasting> transform(@NotNull SpellCasting cast) {
        return List.of(cast.addContextSource(position));
    }

    @Override
    public MapCodec<MoveCastTransformer> getCodec() {
        return CODEC;
    }

    public static Builder builder(PositionSource position) {
        return new Builder(position);
    }

    public static class Builder {
        private final PositionSource position;

        public Builder(PositionSource position) {
            this.position = position;
        }

        public MoveCastTransformer build() {
            return new MoveCastTransformer(position);
        }
    }
}

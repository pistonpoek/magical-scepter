package net.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import net.pistonpoek.magicalscepter.spell.rotation.RotationSource;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public record RotateCastTransformer(RotationSource rotation) implements CastTransformer {
    public static final MapCodec<RotateCastTransformer> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    RotationSource.CODEC.fieldOf("rotation").forGetter(RotateCastTransformer::rotation)
            ).apply(instance, RotateCastTransformer::new)
    );

    @FunctionalInterface
    public interface CastRotator {
        RotationSource getSource(@NotNull SpellCasting cast);
    }

    @Override
    public Collection<SpellCasting> transform(@NotNull SpellCasting cast) {
        return List.of(cast.addContextSource(rotation));
    }

    @Override
    public MapCodec<RotateCastTransformer> getCodec() {
        return CODEC;
    }

    public static Builder builder(RotationSource rotation) {
        return new Builder(rotation);
    }

    public static class Builder {
        private final RotationSource rotation;

        public Builder(RotationSource rotation) {
            this.rotation = rotation;
        }

        public RotateCastTransformer build() {
            return new RotateCastTransformer(rotation);
        }
    }
}

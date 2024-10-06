package net.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringIdentifiable;
import net.pistonpoek.magicalscepter.spell.cast.*;
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
        RotationSource getSource(@NotNull Cast cast);
    }

    @Override
    public Collection<Cast> transform(@NotNull Cast cast) {
        return List.of(cast.setRotation(rotation.getSource(cast)));
    }

    @Override
    public MapCodec<? extends CastTransformer> getCodec() {
        return CODEC;
    }
}

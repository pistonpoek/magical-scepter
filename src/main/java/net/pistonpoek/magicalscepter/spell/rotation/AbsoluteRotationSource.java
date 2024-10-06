package net.pistonpoek.magicalscepter.spell.rotation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Pair;
import net.pistonpoek.magicalscepter.spell.cast.Cast;
import net.pistonpoek.magicalscepter.spell.cast.SpellContext;
import org.jetbrains.annotations.NotNull;

public record AbsoluteRotationSource(float pitch, float yaw) implements RotationSource {
    static MapCodec<AbsoluteRotationSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.FLOAT.optionalFieldOf("pitch", 0.0F).forGetter(AbsoluteRotationSource::pitch),
                    Codec.FLOAT.optionalFieldOf("yaw", 0.0F).forGetter(AbsoluteRotationSource::yaw)
            ).apply(instance, AbsoluteRotationSource::new)
    );

    @Override
    public RotationSource getSource(@NotNull Cast cast) {
        return new AbsoluteRotationSource(pitch, yaw);
    }

    @Override
    public Pair<Float, Float> getRotation(@NotNull SpellContext context) {
        return new Pair<>(pitch, yaw);
    }

    @Override
    public MapCodec<AbsoluteRotationSource> getCodec() {
        return CODEC;
    }

}

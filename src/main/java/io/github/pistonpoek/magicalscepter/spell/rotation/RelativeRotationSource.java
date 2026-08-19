package io.github.pistonpoek.magicalscepter.spell.rotation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import org.jetbrains.annotations.NotNull;

public record RelativeRotationSource(float pitch, float yaw) implements RotationSource {
    static MapCodec<RelativeRotationSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.FLOAT.optionalFieldOf("pitch", 0.0F).forGetter(RelativeRotationSource::pitch),
                    Codec.FLOAT.optionalFieldOf("yaw", 0.0F).forGetter(RelativeRotationSource::yaw)
            ).apply(instance, RelativeRotationSource::new)
    );

    public Tuple<Float, Float> getRotation(@NotNull SpellContext context) {
        return new Tuple<>(
                Mth.wrapDegrees(context.pitch() + pitch),
                Mth.wrapDegrees(context.yaw() + yaw));
    }

    @Override
    public MapCodec<RelativeRotationSource> getCodec() {
        return MAP_CODEC;
    }

}


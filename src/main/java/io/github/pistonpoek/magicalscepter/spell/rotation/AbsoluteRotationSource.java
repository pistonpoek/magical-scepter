package io.github.pistonpoek.magicalscepter.spell.rotation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.util.Pair;

/**
 * TODO
 *
 * @param pitch
 * @param yaw
 */
public record AbsoluteRotationSource(float pitch, float yaw) implements RotationSource {
    public static final MapCodec<AbsoluteRotationSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.FLOAT.optionalFieldOf("pitch", 0.0F).forGetter(AbsoluteRotationSource::pitch),
                    Codec.FLOAT.optionalFieldOf("yaw", 0.0F).forGetter(AbsoluteRotationSource::yaw)
            ).apply(instance, AbsoluteRotationSource::new)
    );

    @Override
    public Pair<Float, Float> getRotation(SpellContext context) {
        return new Pair<>(pitch, yaw);
    }

    @Override
    public MapCodec<AbsoluteRotationSource> getCodec() {
        return MAP_CODEC;
    }

}

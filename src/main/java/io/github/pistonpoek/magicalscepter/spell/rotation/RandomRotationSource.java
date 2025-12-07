package io.github.pistonpoek.magicalscepter.spell.rotation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.util.Pair;
import net.minecraft.util.math.random.Random;

/**
 * TODO
 *
 * @param pitch
 * @param yaw
 */
public record RandomRotationSource(float pitch, float yaw) implements RotationSource {
    public static final MapCodec<RandomRotationSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.FLOAT.optionalFieldOf("pitch", 180.0F).forGetter(RandomRotationSource::pitch),
                    Codec.FLOAT.optionalFieldOf("yaw", 360.0F).forGetter(RandomRotationSource::yaw)
            ).apply(instance, RandomRotationSource::new)
    );

    @Override
    public Pair<Float, Float> getRotation(SpellContext context) {
        Random random = context.caster().getRandom();
        RotationSource rotation = getRandomRotationSource(random);
        return rotation.getRotation(context);
    }

    /**
     * TODO
     *
     * @param random
     * @return
     */
    private RotationSource getRandomRotationSource(Random random) {
        return new RelativeRotationSource(
                random.nextFloat() * pitch,
                random.nextFloat() * yaw
        );
    }

    @Override
    public MapCodec<RandomRotationSource> getCodec() {
        return MAP_CODEC;
    }
}

package net.pistonpoek.magicalscepter.spell.rotation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Pair;
import net.minecraft.util.math.random.Random;
import net.pistonpoek.magicalscepter.spell.cast.Cast;
import net.pistonpoek.magicalscepter.spell.cast.SpellContext;
import org.jetbrains.annotations.NotNull;

public record RandomRotationSource(float pitch, float yaw) implements RotationSource {
    static MapCodec<RandomRotationSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.FLOAT.optionalFieldOf("pitch", 180.0F).forGetter(RandomRotationSource::pitch),
                    Codec.FLOAT.optionalFieldOf("yaw", 360.0F).forGetter(RandomRotationSource::yaw)
            ).apply(instance, RandomRotationSource::new)
    );

    @Override
    public RotationSource getSource(@NotNull Cast cast) {
        Random random = cast.getCaster().getRandom();
        return getRandomRotationSource(random);
    }

    @Override
    public Pair<Float, Float> getRotation(@NotNull SpellContext context) {
        Random random = context.caster().getRandom();
        RotationSource rotation = getRandomRotationSource(random);
        return rotation.getRotation(context);
    }

    private RotationSource getRandomRotationSource(Random random) {
        return new RelativeRotationSource(
                random.nextFloat() * pitch,
                random.nextFloat() * yaw
        );
    }

    @Override
    public MapCodec<? extends RotationSource> getCodec() {
        return CODEC;
    }
}

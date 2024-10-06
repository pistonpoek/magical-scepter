package net.pistonpoek.magicalscepter.spell.rotation;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import net.pistonpoek.magicalscepter.spell.cast.Cast;
import net.pistonpoek.magicalscepter.spell.cast.SpellContext;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record MixedRotationSource(Optional<RotationSource> pitch, Optional<RotationSource> yaw) implements RotationSource {
    static MapCodec<MixedRotationSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    RotationSource.CODEC.optionalFieldOf("pitch").forGetter(MixedRotationSource::pitch),
                    RotationSource.CODEC.optionalFieldOf("yaw").forGetter(MixedRotationSource::yaw)
            ).apply(instance, MixedRotationSource::new)
    );

    @Override
    public RotationSource getSource(@NotNull Cast cast) {
        return ListRotationSource.combine(cast.getRotationSource(), this);
    }

    @Override
    public Pair<Float, Float> getRotation(@NotNull SpellContext context) {
        return new Pair<>(
                MathHelper.wrapDegrees(pitch.map(pitch -> pitch.getPitch(context)).orElse(context.pitch())),
                MathHelper.wrapDegrees(yaw.map(yaw -> yaw.getYaw(context)).orElse(context.yaw())));
    }

    @Override
    public MapCodec<MixedRotationSource> getCodec() {
        return CODEC;
    }

}

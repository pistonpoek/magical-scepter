package net.pistonpoek.magicalscepter.spell.rotation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import net.pistonpoek.magicalscepter.spell.cast.Cast;
import net.pistonpoek.magicalscepter.spell.cast.SpellContext;
import org.jetbrains.annotations.NotNull;

public record RelativeRotationSource(float pitch, float yaw) implements RotationSource {
    static MapCodec<RelativeRotationSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.FLOAT.optionalFieldOf("pitch", 0.0F).forGetter(RelativeRotationSource::pitch),
                    Codec.FLOAT.optionalFieldOf("yaw", 0.0F).forGetter(RelativeRotationSource::yaw)
            ).apply(instance, RelativeRotationSource::new)
    );

    public RotationSource getSource(@NotNull Cast cast) {
        return ListRotationSource.combine(cast.getRotation(), this);
    }

    public Pair<Float, Float> getRotation(@NotNull SpellContext context) {
        return new Pair<>(
                    MathHelper.wrapDegrees(context.pitch() + pitch),
                    MathHelper.wrapDegrees(context.yaw() + yaw));
    }

    @Override
    public MapCodec<RelativeRotationSource> getCodec() {
        return RelativeRotationSource.CODEC;
    }

}


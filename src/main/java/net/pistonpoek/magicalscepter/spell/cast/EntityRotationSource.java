package net.pistonpoek.magicalscepter.spell.cast;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

public record EntityRotationSource(float pitch, float yaw) implements RotationSource {
    static MapCodec<EntityRotationSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.FLOAT.optionalFieldOf("pitch", 0.0F).forGetter(EntityRotationSource::pitch),
                    Codec.FLOAT.optionalFieldOf("yaw", 0.0F).forGetter(EntityRotationSource::yaw)
            ).apply(instance, EntityRotationSource::new)
    );

    @Override
    public RotationSource getSource(@NotNull Cast cast) {
        return new EntityRotationSource(pitch, yaw);
    }

    @Override
    public Pair<Float, Float> getRotation(@NotNull SpellContext context) {
        return new Pair<>(
                MathHelper.wrapDegrees(context.caster().getPitch()) + pitch,
                MathHelper.wrapDegrees(context.caster().getYaw() + yaw));
    }

    @Override
    public MapCodec<RelativeRotationSource> getCodec() {
        return RelativeRotationSource.CODEC;
    }

}



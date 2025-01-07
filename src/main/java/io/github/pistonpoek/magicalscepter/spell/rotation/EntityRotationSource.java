package io.github.pistonpoek.magicalscepter.spell.rotation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import org.jetbrains.annotations.NotNull;

public record EntityRotationSource(float pitch, float yaw) implements RotationSource {
    static MapCodec<EntityRotationSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.FLOAT.optionalFieldOf("pitch", 0.0F).forGetter(EntityRotationSource::pitch),
                    Codec.FLOAT.optionalFieldOf("yaw", 0.0F).forGetter(EntityRotationSource::yaw)
            ).apply(instance, EntityRotationSource::new)
    );

    @Override
    public Pair<Float, Float> getRotation(@NotNull SpellContext context) {
        return new Pair<>(
                MathHelper.wrapDegrees(context.target().getPitch() + pitch),
                MathHelper.wrapDegrees(context.target().getYaw() + yaw)
        );
    }

    @Override
    public MapCodec<EntityRotationSource> getCodec() {
        return MAP_CODEC;
    }

}



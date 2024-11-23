package io.github.pistonpoek.magicalscepter.spell.rotation;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
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
    public Pair<Float, Float> getRotation(@NotNull SpellContext context) {
        return new Pair<>(
                MathHelper.wrapDegrees(pitch.map(pitch -> pitch.getPitch(context)).orElse(context.pitch())),
                MathHelper.wrapDegrees(yaw.map(yaw -> yaw.getYaw(context)).orElse(context.yaw())));
    }

    @Override
    public MapCodec<MixedRotationSource> getCodec() {
        return CODEC;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private RotationSource pitchRotation = null;
        private RotationSource yawRotation = null;

        public Builder pitchRotation(RotationSource rotation) {
            this.pitchRotation = rotation;
            return this;
        }

        public Builder yawRotation(RotationSource rotation) {
            this.yawRotation = rotation;
            return this;
        }

        public MixedRotationSource build() {
            return new MixedRotationSource(Optional.ofNullable(pitchRotation), Optional.ofNullable(yawRotation));
        }
    }

}

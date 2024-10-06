package net.pistonpoek.magicalscepter.spell.position;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.pistonpoek.magicalscepter.spell.cast.Cast;
import net.pistonpoek.magicalscepter.spell.cast.SpellContext;
import net.pistonpoek.magicalscepter.spell.rotation.RotationSource;
import net.pistonpoek.magicalscepter.util.RotationVector;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record RelativePositionSource(Vec3d value, Optional<PositionSource> position, Optional<RotationSource> rotation)
        implements PositionSource {
    static MapCodec<RelativePositionSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Vec3d.CODEC.fieldOf("value").forGetter(RelativePositionSource::value),
                    PositionSource.CODEC.optionalFieldOf("position").forGetter(RelativePositionSource::position),
                    RotationSource.CODEC.optionalFieldOf("rotation").forGetter(RelativePositionSource::rotation)
            ).apply(instance, RelativePositionSource::new)
    );

    @Override
    public PositionSource getSource(@NotNull Cast cast) {
        return ListPositionSource.combine(cast.getPosition(), this);
    }

    @Override
    public Vec3d getPosition(@NotNull SpellContext context) {
        Vec3d vector = position.map(position -> position.getPosition(context)).orElse(context.position());
        float pitch = rotation.map(rotation -> rotation.getPitch(context)).orElse(context.pitch());
        float yaw = rotation.map(rotation -> rotation.getYaw(context)).orElse(context.yaw());
        return getRelativeVector(vector, pitch, yaw, value);
    }

    private Vec3d getRelativeVector(Vec3d base, float pitch, float yaw, Vec3d value) {
        return base
                .add(RotationVector.get(0, MathHelper.wrapDegrees(yaw - 90)).normalize().multiply(value.x))
                .add(RotationVector.get(MathHelper.wrapDegrees(pitch - 90), yaw).normalize().multiply(value.y))
                .add(RotationVector.get(pitch, yaw).normalize().multiply(value.z));
    }

    @Override
    public MapCodec<RelativePositionSource> getCodec() {
        return RelativePositionSource.CODEC;
    }

    // TODO add builder for this class and possibly other position and rotation sources.
}

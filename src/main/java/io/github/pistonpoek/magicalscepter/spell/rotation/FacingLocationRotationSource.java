package io.github.pistonpoek.magicalscepter.spell.rotation;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import io.github.pistonpoek.magicalscepter.spell.position.PositionSource;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec3d;

/**
 * TODO
 *
 * @param position
 */
public record FacingLocationRotationSource(PositionSource position) implements RotationSource {
    public final static MapCodec<FacingLocationRotationSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    PositionSource.CODEC.fieldOf("position").forGetter(FacingLocationRotationSource::position)
            ).apply(instance, FacingLocationRotationSource::new)
    );

    @Override
    public Pair<Float, Float> getRotation(SpellContext context) {
        Vec3d start = context.position();
        Vec3d end = position.getPosition(context);

        Vec3d facing = end.subtract(start).normalize();
        if (facing.length() == 0) {
            return new Pair<>(context.pitch(), context.yaw());
        }
        float pitch = (float) (-90 + 180 * Math.acos(facing.y) / Math.PI);

        Vec3d horizontal = new Vec3d(facing.x, 0, facing.z).normalize();
        if (horizontal.length() == 0) {
            return new Pair<>(pitch, context.yaw());
        }
        float yaw = (float) (180 * Math.acos(horizontal.z) / Math.PI);
        yaw = facing.x > 0 ? -yaw : yaw;

        return new Pair<>(pitch, yaw);
    }

    @Override
    public MapCodec<FacingLocationRotationSource> getCodec() {
        return MAP_CODEC;
    }
}

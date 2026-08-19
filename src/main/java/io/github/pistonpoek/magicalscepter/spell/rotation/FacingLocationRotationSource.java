package io.github.pistonpoek.magicalscepter.spell.rotation;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import io.github.pistonpoek.magicalscepter.spell.position.PositionSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record FacingLocationRotationSource(PositionSource position) implements RotationSource {
    static MapCodec<FacingLocationRotationSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    PositionSource.CODEC.fieldOf("position").forGetter(FacingLocationRotationSource::position)
            ).apply(instance, FacingLocationRotationSource::new)
    );

    @Override
    public Tuple<Float, Float> getRotation(@NotNull SpellContext context) {
        Vec3 start = context.position();
        Vec3 end = position.getPosition(context);

        Vec3 facing = end.subtract(start).normalize();
        if (facing.length() == 0) {
            return new Tuple<>(context.pitch(), context.yaw());
        }
        float pitch = (float) (-90 + 180 * Math.acos(facing.y) / Math.PI);

        Vec3 horizontal = new Vec3(facing.x, 0, facing.z).normalize();
        if (horizontal.length() == 0) {
            return new Tuple<>(pitch, context.yaw());
        }
        float yaw = (float) (180 * Math.acos(horizontal.z) / Math.PI);
        yaw = facing.x > 0 ? -yaw : yaw;

        return new Tuple<>(pitch, yaw);
    }

    @Override
    public MapCodec<FacingLocationRotationSource> getCodec() {
        return MAP_CODEC;
    }
}

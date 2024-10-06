package net.pistonpoek.magicalscepter.spell.cast;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public record ValuePositionSource(Vec3d value) implements PositionSource {
    static MapCodec<ValuePositionSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Vec3d.CODEC.optionalFieldOf("value", Vec3d.ZERO).forGetter(ValuePositionSource::value)
            ).apply(instance, ValuePositionSource::new)
    );

    @Override
    public PositionSource getSource(@NotNull Cast cast) {
        return this;
    }

    @Override
    public Vec3d getPosition(@NotNull SpellContext context) {
        return value;
    }

    @Override
    public MapCodec<ValuePositionSource> getCodec() {
        return ValuePositionSource.CODEC;
    }

}

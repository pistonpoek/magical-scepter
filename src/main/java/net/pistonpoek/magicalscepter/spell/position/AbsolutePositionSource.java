package net.pistonpoek.magicalscepter.spell.position;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.Vec3d;
import net.pistonpoek.magicalscepter.spell.cast.Cast;
import net.pistonpoek.magicalscepter.spell.cast.SpellContext;
import org.jetbrains.annotations.NotNull;

public record AbsolutePositionSource(Vec3d value) implements PositionSource {
    static MapCodec<AbsolutePositionSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Vec3d.CODEC.optionalFieldOf("value", Vec3d.ZERO).forGetter(AbsolutePositionSource::value)
            ).apply(instance, AbsolutePositionSource::new)
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
    public MapCodec<AbsolutePositionSource> getCodec() {
        return CODEC;
    }

}

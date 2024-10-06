package net.pistonpoek.magicalscepter.spell.position;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.Vec3d;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.registry.ModRegistries;
import net.pistonpoek.magicalscepter.spell.cast.SpellContext;
import net.pistonpoek.magicalscepter.spell.cast.transformer.MoveCastTransformer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface PositionSource extends MoveCastTransformer.CastMover {
    Codec<PositionSource> CODEC = ModRegistries.CAST_POSITION_SOURCE_TYPE.getCodec()
            .dispatch(PositionSource::getCodec, Function.identity());

    static void register(Registry<MapCodec<? extends PositionSource>> registry) {
        Registry.register(registry, ModIdentifier.of("absolute"), AbsolutePositionSource.CODEC);
        Registry.register(registry, ModIdentifier.of("entity"), EntityPositionSource.CODEC);
        Registry.register(registry, ModIdentifier.of("relative"), RelativePositionSource.CODEC);
        // TODO ENTITY PROJECTILE ANCHOR
        // TODO SURFACE POSITION
        // TODO RANDOM BOX CAST
    }

    Vec3d getPosition(@NotNull SpellContext context);

    default double getX(@NotNull SpellContext context) {
        return getPosition(context).getX();
    }

    default double getY(@NotNull SpellContext context) {
        return getPosition(context).getY();
    }

    default double getZ(@NotNull SpellContext context) {
        return getPosition(context).getZ();
    }

    MapCodec<? extends PositionSource> getCodec();
}

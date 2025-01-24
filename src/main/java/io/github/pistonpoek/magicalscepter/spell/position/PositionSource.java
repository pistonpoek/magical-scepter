package io.github.pistonpoek.magicalscepter.spell.position;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.Vec3d;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import io.github.pistonpoek.magicalscepter.registry.ModRegistries;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContextSource;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface PositionSource extends SpellContextSource {
    MapCodec<PositionSource> MAP_CODEC = ModRegistries.CAST_POSITION_SOURCE_TYPE.getCodec()
            .dispatchMap(PositionSource::getCodec, Function.identity());
    Codec<PositionSource> CODEC = MAP_CODEC.codec();

    static void register(Registry<MapCodec<? extends PositionSource>> registry) {
        Registry.register(registry, ModIdentifier.of("absolute"), AbsolutePositionSource.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("entity"), EntityPositionSource.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("relative"), RelativePositionSource.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("mixed"), MixedPositionSource.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("random"), RandomPositionSource.MAP_CODEC);
    }

    Vec3d getPosition(@NotNull SpellContext context);

    @Override
    default SpellContext getContext(@NotNull SpellContext spellContext) {
        return new SpellContext(spellContext, getPosition(spellContext));
    }

    default double getX(@NotNull SpellContext context) {
        return getPosition(context).getX();
    }

    default double getY(@NotNull SpellContext context) {
        return getPosition(context).getY();
    }

    default double getZ(@NotNull SpellContext context) {
        return getPosition(context).getZ();
    }

    @Override
    default MapCodec<PositionSource> getSourceCodec() {
        return MAP_CODEC;
    }

    MapCodec<? extends PositionSource> getCodec();
}

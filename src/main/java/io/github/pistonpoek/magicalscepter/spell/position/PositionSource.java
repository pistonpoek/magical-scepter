package io.github.pistonpoek.magicalscepter.spell.position;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.registry.ModRegistries;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContextSource;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public interface PositionSource extends SpellContextSource {
    MapCodec<PositionSource> MAP_CODEC = ModRegistries.CAST_POSITION_SOURCE_TYPE.byNameCodec()
            .dispatchMap(PositionSource::getCodec, Function.identity());
    Codec<PositionSource> CODEC = MAP_CODEC.codec();

    static void register(Registry<MapCodec<? extends PositionSource>> registry) {
        Registry.register(registry, ModIdentifier.of("absolute"), AbsolutePositionSource.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("entity"), EntityPositionSource.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("relative"), RelativePositionSource.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("mixed"), MixedPositionSource.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("random"), RandomPositionSource.MAP_CODEC);
    }

    Vec3 getPosition(@NotNull SpellContext context);

    @Override
    default SpellContext getContext(@NotNull SpellContext spellContext) {
        if (!Level.isInSpawnableBounds(BlockPos.containing(getPosition(spellContext)))) {
            MagicalScepter.LOGGER.debug("Spell position is not valid");
            return spellContext;
        }
        return new SpellContext(spellContext, getPosition(spellContext));
    }

    default double getX(@NotNull SpellContext context) {
        return getPosition(context).x();
    }

    default double getY(@NotNull SpellContext context) {
        return getPosition(context).y();
    }

    default double getZ(@NotNull SpellContext context) {
        return getPosition(context).z();
    }

    @Override
    default MapCodec<PositionSource> getSourceCodec() {
        return MAP_CODEC;
    }

    MapCodec<? extends PositionSource> getCodec();
}

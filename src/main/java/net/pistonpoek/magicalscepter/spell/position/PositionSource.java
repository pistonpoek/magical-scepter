package net.pistonpoek.magicalscepter.spell.position;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.Vec3d;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.registry.ModRegistries;
import net.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.pistonpoek.magicalscepter.spell.cast.context.SpellContextSource;
import net.pistonpoek.magicalscepter.spell.cast.transformer.MoveCastTransformer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface PositionSource extends SpellContextSource {
    MapCodec<PositionSource> MAP_CODEC = ModRegistries.CAST_POSITION_SOURCE_TYPE.getCodec()
            .dispatchMap(PositionSource::getCodec, Function.identity());
    Codec<PositionSource> CODEC = MAP_CODEC.codec();

    static void register(Registry<MapCodec<? extends PositionSource>> registry) {
        Registry.register(registry, ModIdentifier.of("absolute"), AbsolutePositionSource.CODEC);
        Registry.register(registry, ModIdentifier.of("entity"), EntityPositionSource.CODEC);
        Registry.register(registry, ModIdentifier.of("relative"), RelativePositionSource.CODEC);
        Registry.register(registry, ModIdentifier.of("mixed"), MixedPositionSource.CODEC);
        Registry.register(registry, ModIdentifier.of("random"), RandomPositionSource.CODEC);
        // TODO entity projectile anchor?
        // done? TODO random box cast (Allow scaled bounding box of entity?)
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

    MapCodec<? extends PositionSource> getCodec();
}

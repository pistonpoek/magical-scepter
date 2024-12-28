package io.github.pistonpoek.magicalscepter.spell.rotation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;
import io.github.pistonpoek.magicalscepter.registry.ModRegistries;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContextSource;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface RotationSource extends SpellContextSource {
    MapCodec<RotationSource> MAP_CODEC = ModRegistries.CAST_ROTATION_SOURCE_TYPE.getCodec()
            .dispatchMap(RotationSource::getCodec, Function.identity());
    Codec<RotationSource> CODEC = MAP_CODEC.codec();

    static void register(Registry<MapCodec<? extends RotationSource>> registry) {
        Registry.register(registry, ModIdentifier.of("absolute"), AbsoluteRotationSource.CODEC);
        Registry.register(registry, ModIdentifier.of("relative"), RelativeRotationSource.CODEC);
        Registry.register(registry, ModIdentifier.of("entity"), EntityRotationSource.CODEC);
        Registry.register(registry, ModIdentifier.of("mixed"), MixedRotationSource.CODEC);
        Registry.register(registry, ModIdentifier.of("random"), RandomRotationSource.CODEC);
        Registry.register(registry, ModIdentifier.of("facing"), FacingLocationRotationSource.CODEC);
    }

    Pair<Float, Float> getRotation(@NotNull SpellContext context);

    @Override
    default SpellContext getContext(@NotNull SpellContext context) {
        Pair<Float, Float> rotationPair = getRotation(context);
        return new SpellContext(context, rotationPair.getLeft(), rotationPair.getRight());
    }

    default float getPitch(@NotNull SpellContext context) {
        return getRotation(context).getLeft();
    }

    default float getYaw(@NotNull SpellContext context) {
        return getRotation(context).getRight();
    }

    static Direction getDirection(@NotNull SpellContext context) {
        return Direction.getFacing(context.getRotationVector());
    }

    @Override
    default MapCodec<RotationSource> getSourceCodec() {
        return MAP_CODEC;
    }

    MapCodec<? extends RotationSource> getCodec();
}

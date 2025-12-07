package io.github.pistonpoek.magicalscepter.spell.rotation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.pistonpoek.magicalscepter.registry.ModRegistries;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContextSource;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Direction;

import java.util.function.Function;

/**
 * TODO
 */
public interface RotationSource extends SpellContextSource {
    MapCodec<RotationSource> MAP_CODEC = ModRegistries.CAST_ROTATION_SOURCE_TYPE.getCodec()
            .dispatchMap(RotationSource::getCodec, Function.identity());
    Codec<RotationSource> CODEC = MAP_CODEC.codec();

    /**
     * TODO
     *
     * @param registry
     */
    static void register(Registry<MapCodec<? extends RotationSource>> registry) {
        Registry.register(registry, ModIdentifier.of("absolute"), AbsoluteRotationSource.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("relative"), RelativeRotationSource.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("entity"), EntityRotationSource.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("mixed"), MixedRotationSource.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("random"), RandomRotationSource.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("facing"), FacingLocationRotationSource.MAP_CODEC);
    }

    /**
     * TODO
     *
     * @param context
     * @return
     */
    Pair<Float, Float> getRotation(SpellContext context);

    @Override
    default SpellContext getContext(SpellContext context) {
        Pair<Float, Float> rotationPair = getRotation(context);
        return new SpellContext(context, rotationPair.getLeft(), rotationPair.getRight());
    }

    /**
     * TODO
     *
     * @param context
     * @return
     */
    default float getPitch(SpellContext context) {
        return getRotation(context).getLeft();
    }

    /**
     * TODO
     *
     * @param context
     * @return
     */
    default float getYaw(SpellContext context) {
        return getRotation(context).getRight();
    }

    /**
     * TODO
     *
     * @param context
     * @return
     */
    static Direction getDirection(SpellContext context) {
        return Direction.getFacing(context.getRotationVector());
    }

    @Override
    default MapCodec<RotationSource> getSourceCodec() {
        return MAP_CODEC;
    }

    /**
     * TODO
     *
     * @return
     */
    MapCodec<? extends RotationSource> getCodec();
}

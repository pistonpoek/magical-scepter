package net.pistonpoek.magicalscepter.spell.rotation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.registry.ModRegistries;
import net.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.pistonpoek.magicalscepter.spell.cast.context.SpellContextSource;
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
        // TODO Movement (entity.getMovement() form vector to pitch yaw??)
        // TODO facing target
    }

    Pair<Float, Float> getRotation(@NotNull SpellContext context);

    @Override
    default SpellContext getContext(@NotNull SpellContext spellContext) {
        Pair<Float, Float> rotationPair = getRotation(spellContext);
        return new SpellContext(spellContext, rotationPair.getLeft(), rotationPair.getRight());
    }

    default float getPitch(@NotNull SpellContext context) {
        return getRotation(context).getLeft();
    }

    default float getYaw(@NotNull SpellContext context) {
        return getRotation(context).getRight();
    }

    MapCodec<? extends RotationSource> getCodec();
}

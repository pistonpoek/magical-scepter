package net.pistonpoek.magicalscepter.spell.rotation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.registry.ModRegistries;
import net.pistonpoek.magicalscepter.spell.cast.SpellContext;
import net.pistonpoek.magicalscepter.spell.cast.transformer.RotateCastTransformer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface RotationSource extends RotateCastTransformer.CastRotator {
    Codec<RotationSource> CODEC = ModRegistries.CAST_ROTATION_SOURCE_TYPE.getCodec()
            .dispatch(RotationSource::getCodec, Function.identity());

    static void register(Registry<MapCodec<? extends RotationSource>> registry) {
        Registry.register(registry, ModIdentifier.of("absolute"), AbsoluteRotationSource.CODEC);
        Registry.register(registry, ModIdentifier.of("relative"), RelativeRotationSource.CODEC);
        Registry.register(registry, ModIdentifier.of("entity"), EntityRotationSource.CODEC);
        Registry.register(registry, ModIdentifier.of("list"), ListRotationSource.CODEC);
        Registry.register(registry, ModIdentifier.of("mixed"), MixedRotationSource.CODEC);
        Registry.register(registry, ModIdentifier.of("random"), RandomRotationSource.CODEC);
        // TODO Movement (entity.getMovement() form vector to pitch yaw??)
        // TODO facing location (position)
        // TODO facing target
    }

    Pair<Float, Float> getRotation(@NotNull SpellContext context);

    default float getPitch(@NotNull SpellContext context) {
        return getRotation(context).getLeft();
    }

    default float getYaw(@NotNull SpellContext context) {
        return getRotation(context).getRight();
    }

    MapCodec<? extends RotationSource> getCodec();
}

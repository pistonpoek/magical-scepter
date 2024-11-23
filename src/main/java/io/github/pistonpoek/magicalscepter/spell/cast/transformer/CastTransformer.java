package io.github.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;
import io.github.pistonpoek.magicalscepter.registry.ModRegistries;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;

public interface CastTransformer {
    Codec<CastTransformer> CODEC = ModRegistries.CAST_TRANSFORMER_TYPE.getCodec().dispatch(CastTransformer::getCodec, Function.identity());

    static void register(Registry<MapCodec<? extends CastTransformer>> registry) {
        Registry.register(registry, ModIdentifier.of("line"), LineCastTransformer.CODEC);
        Registry.register(registry, ModIdentifier.of("move"), MoveCastTransformer.CODEC);
        Registry.register(registry, ModIdentifier.of("delay"), DelayCastTransformer.CODEC);
        Registry.register(registry, ModIdentifier.of("rotate"), RotateCastTransformer.CODEC);
        Registry.register(registry, ModIdentifier.of("surface"), SurfaceCastTransformer.CODEC);
        Registry.register(registry, ModIdentifier.of("target"), TargetCastTransformer.CODEC);
        Registry.register(registry, ModIdentifier.of("circle"), CircleCastTransformer.CODEC);
        Registry.register(registry, ModIdentifier.of("repeat"), RepeatCastTransformer.CODEC);

        // TODO condition transformer??
    }

    Collection<SpellCasting> transform(@NotNull SpellCasting cast);

    MapCodec<? extends CastTransformer> getCodec();
}

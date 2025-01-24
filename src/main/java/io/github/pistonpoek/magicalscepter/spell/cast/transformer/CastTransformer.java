package io.github.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import io.github.pistonpoek.magicalscepter.registry.ModRegistries;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;

public interface CastTransformer {
    Codec<CastTransformer> CODEC = ModRegistries.CAST_TRANSFORMER_TYPE.getCodec().dispatch(CastTransformer::getCodec, Function.identity());

    static void register(Registry<MapCodec<? extends CastTransformer>> registry) {
        Registry.register(registry, ModIdentifier.of("anchor"), AnchorCastTransformer.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("line"), LineCastTransformer.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("circle"), CircleCastTransformer.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("surface"), SurfaceCastTransformer.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("move"), MoveCastTransformer.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("rotate"), RotateCastTransformer.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("delay"), DelayCastTransformer.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("repeat"), RepeatCastTransformer.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("ray"), RayCastTransformer.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("filter"), FilterCastTransformer.MAP_CODEC);
    }

    Collection<SpellCasting> transform(@NotNull SpellCasting casting);

    MapCodec<? extends CastTransformer> getCodec();
}

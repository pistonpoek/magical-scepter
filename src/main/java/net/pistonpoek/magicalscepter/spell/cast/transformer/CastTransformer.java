package net.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.registry.ModRegistries;
import net.pistonpoek.magicalscepter.spell.cast.Cast;
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
        // TODO Add circle transformer
        // TODO add target transformer (entity, block, liquid?)
        // TODO add surface transformer
    }

    Collection<Cast> transform(@NotNull Cast cast);

    MapCodec<? extends CastTransformer> getCodec();
}

package io.github.pistonpoek.magicalscepter.spell.cast.context;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.pistonpoek.magicalscepter.registry.ModRegistries;
import io.github.pistonpoek.magicalscepter.spell.position.PositionSource;
import io.github.pistonpoek.magicalscepter.spell.rotation.RotationSource;
import io.github.pistonpoek.magicalscepter.spell.target.TargetSource;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.registry.Registry;

import java.util.function.Function;

/**
 * TODO
 */
public interface SpellContextSource {
    Codec<SpellContextSource> CODEC = ModRegistries.CAST_CONTEXT_SOURCE_TYPE.getCodec()
            .dispatch("source", SpellContextSource::getSourceCodec, Function.identity());

    /**
     * TODO
     *
     * @param registry
     */
    static void register(Registry<MapCodec<? extends SpellContextSource>> registry) {
        Registry.register(registry, ModIdentifier.of("list"), ContextSourceList.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("position"), PositionSource.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("rotation"), RotationSource.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("target"), TargetSource.MAP_CODEC);
    }

    /**
     * TODO
     *
     * @param spellContext
     * @return
     */
    SpellContext getContext(SpellContext spellContext);

    /**
     * TODO
     *
     * @return
     */
    MapCodec<? extends SpellContextSource> getSourceCodec();
}

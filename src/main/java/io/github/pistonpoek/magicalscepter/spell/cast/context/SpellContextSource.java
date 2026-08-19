package io.github.pistonpoek.magicalscepter.spell.cast.context;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.pistonpoek.magicalscepter.registry.ModRegistries;
import io.github.pistonpoek.magicalscepter.spell.position.PositionSource;
import io.github.pistonpoek.magicalscepter.spell.rotation.RotationSource;
import io.github.pistonpoek.magicalscepter.spell.target.TargetSource;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import net.minecraft.core.Registry;

public interface SpellContextSource {
    Codec<SpellContextSource> CODEC = ModRegistries.CAST_CONTEXT_SOURCE_TYPE.byNameCodec()
            .dispatch("source", SpellContextSource::getSourceCodec, Function.identity());

    static void register(Registry<MapCodec<? extends SpellContextSource>> registry) {
        Registry.register(registry, ModIdentifier.of("list"), ContextSourceList.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("position"), PositionSource.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("rotation"), RotationSource.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("target"), TargetSource.MAP_CODEC);
    }

    SpellContext getContext(@NotNull SpellContext spellContext);

    MapCodec<? extends SpellContextSource> getSourceCodec();
}

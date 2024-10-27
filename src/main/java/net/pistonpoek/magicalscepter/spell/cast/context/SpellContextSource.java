package net.pistonpoek.magicalscepter.spell.cast.context;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.registry.ModRegistries;
import net.pistonpoek.magicalscepter.spell.position.PositionSource;
import net.pistonpoek.magicalscepter.spell.rotation.RotationSource;
import net.pistonpoek.magicalscepter.spell.target.TargetSource;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface SpellContextSource {
    Codec<SpellContextSource> CODEC = ModRegistries.CAST_CONTEXT_SOURCE_TYPE.getCodec()
                    .dispatch("source", SpellContextSource::getCodec, Function.identity());

    static void register(Registry<MapCodec<? extends SpellContextSource>> registry) {
        Registry.register(registry, ModIdentifier.of("position"), PositionSource.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("rotation"), RotationSource.MAP_CODEC);
        Registry.register(registry, ModIdentifier.of("target"), TargetSource.MAP_CODEC);
    }

    SpellContext getContext(@NotNull SpellContext spellContext);

    MapCodec<? extends SpellContextSource> getCodec();
}

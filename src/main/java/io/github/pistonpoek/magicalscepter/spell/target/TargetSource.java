package io.github.pistonpoek.magicalscepter.spell.target;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;
import io.github.pistonpoek.magicalscepter.registry.ModRegistries;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContextSource;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface TargetSource extends SpellContextSource {
    MapCodec<TargetSource> MAP_CODEC = ModRegistries.CAST_TARGET_SOURCE_TYPE.getCodec()
            .dispatchMap(TargetSource::getCodec, Function.identity());
    Codec<TargetSource> CODEC = MAP_CODEC.codec();

    static void register(Registry<MapCodec<? extends TargetSource>> registry) {
        Registry.register(registry, ModIdentifier.of("absolute"), AbsoluteTargetSource.MAP_CODEC);
    }

    Entity getTarget(@NotNull SpellContext spellContext);

    @Override
    default SpellContext getContext(@NotNull SpellContext spellContext) {
        return new SpellContext(spellContext, getTarget(spellContext));
    }

    @Override
    default MapCodec<TargetSource> getSourceCodec() {
        return MAP_CODEC;
    }

    MapCodec<? extends TargetSource> getCodec();
}

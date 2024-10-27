package net.pistonpoek.magicalscepter.spell.target;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.registry.ModRegistries;
import net.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.pistonpoek.magicalscepter.spell.cast.context.SpellContextSource;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface TargetSource extends SpellContextSource {
    MapCodec<TargetSource> MAP_CODEC = ModRegistries.CAST_TARGET_SOURCE_TYPE.getCodec()
            .dispatchMap(TargetSource::getCodec, Function.identity());
    Codec<TargetSource> CODEC = MAP_CODEC.codec();

    static void register(Registry<MapCodec<? extends TargetSource>> registry) {
        Registry.register(registry, ModIdentifier.of("absolute"), AbsoluteTargetSource.CODEC);
    }

    Entity getTarget(@NotNull SpellContext spellContext);

    @Override
    default SpellContext getContext(@NotNull SpellContext spellContext) {
        return new SpellContext(spellContext, getTarget(spellContext));
    }

    MapCodec<? extends TargetSource> getCodec();
}

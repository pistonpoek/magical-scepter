package io.github.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.MapCodec;
import io.github.pistonpoek.magicalscepter.spell.cast.context.ContextSourceList;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public record AnchorCastTransformer() implements CastTransformer {
    public static final MapCodec<AnchorCastTransformer> MAP_CODEC = MapCodec.unit(new AnchorCastTransformer());
    @Override
    public Collection<SpellCasting> transform(@NotNull SpellCasting casting) {
        return List.of(casting.addContext(new ContextSourceList(casting.getContext())));
    }

    @Override
    public MapCodec<AnchorCastTransformer> getCodec() {
        return MAP_CODEC;
    }
}

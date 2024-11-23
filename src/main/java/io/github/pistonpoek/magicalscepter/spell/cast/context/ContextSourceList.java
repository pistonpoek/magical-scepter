package io.github.pistonpoek.magicalscepter.spell.cast.context;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ContextSourceList(List<SpellContextSource> contextSources) implements SpellContextSource {
    static MapCodec<ContextSourceList> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    SpellContextSource.CODEC.listOf().fieldOf("contextSources")
                            .forGetter(ContextSourceList::contextSources)
            ).apply(instance, ContextSourceList::new)
    );

    @Override
    public SpellContext getContext(@NotNull SpellContext spellContext) {
        for (SpellContextSource contextSource: contextSources) {
            spellContext = contextSource.getContext(spellContext);
        }
        return spellContext;
    }

    public ContextSourceList append(SpellContextSource contextSource) {
        if (contextSource instanceof ContextSourceList) {
            for (SpellContextSource contextSourceElement: ((ContextSourceList)contextSource).contextSources) {
                this.append(contextSourceElement);
            }
            return this;
        }
        contextSources.add(contextSource);
        return this;
    }

    @Override
    public MapCodec<ContextSourceList> getCodec() {
        return CODEC;
    }
}

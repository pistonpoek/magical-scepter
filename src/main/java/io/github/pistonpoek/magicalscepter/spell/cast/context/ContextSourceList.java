package io.github.pistonpoek.magicalscepter.spell.cast.context;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.pistonpoek.magicalscepter.spell.position.AbsolutePositionSource;
import io.github.pistonpoek.magicalscepter.spell.rotation.AbsoluteRotationSource;
import io.github.pistonpoek.magicalscepter.spell.target.AbsoluteTargetSource;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ContextSourceList(List<SpellContextSource> sources) implements SpellContextSource {
    public static final MapCodec<ContextSourceList> MAP_CODEC =
            SpellContextSource.CODEC.listOf().xmap(
                    ContextSourceList::new,
                    ContextSourceList::sources
            ).fieldOf("sources");
    public static final Codec<ContextSourceList> CODEC = MAP_CODEC.codec();

    @Override
    public SpellContext getContext(@NotNull SpellContext spellContext) {
        for (SpellContextSource contextSource: sources) {
            spellContext = contextSource.getContext(spellContext);
        }
        return spellContext;
    }

    public ContextSourceList append(SpellContextSource contextSource) {
        if (contextSource instanceof ContextSourceList) {
            for (SpellContextSource contextSourceElement: ((ContextSourceList)contextSource).sources) {
                this.append(contextSourceElement);
            }
            return this;
        }
        sources.add(contextSource);
        return this;
    }

    public ContextSourceList(SpellContext context) {
        this(List.of(
                new AbsoluteTargetSource(context.target().getUuid()),
                new AbsolutePositionSource(context.position()),
                new AbsoluteRotationSource(context.pitch(), context.yaw())
            )
        );
    }

    @Override
    public MapCodec<ContextSourceList> getSourceCodec() {
        return MAP_CODEC;
    }
}
package io.github.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.predicate.entity.EntityPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public record ConditionCastTransformer(EntityPredicate condition) implements CastTransformer {
    public static final MapCodec<ConditionCastTransformer> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    EntityPredicate.CODEC.fieldOf("condition").forGetter(ConditionCastTransformer::condition)
            ).apply(instance, ConditionCastTransformer::new)
    );

    @Override
    public Collection<SpellCasting> transform(@NotNull SpellCasting cast) {
        SpellContext context = cast.getContext();
        if (condition.test(context.getWorld(), context.position(), context.caster())) {
            return List.of(cast);
        }

        return List.of();
    }

    @Override
    public MapCodec<? extends CastTransformer> getCodec() {
        return CODEC;
    }
}

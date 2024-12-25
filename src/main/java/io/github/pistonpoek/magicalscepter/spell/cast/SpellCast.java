package io.github.pistonpoek.magicalscepter.spell.cast;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.MagicalScepter;
import net.minecraft.entity.LivingEntity;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import io.github.pistonpoek.magicalscepter.spell.cast.delay.SpellCastScheduler;
import io.github.pistonpoek.magicalscepter.spell.cast.transformer.CastTransformer;
import io.github.pistonpoek.magicalscepter.spell.effect.SpellEffect;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public record SpellCast(List<SpellEffect> effects, List<CastTransformer> transformers) {
    public static final Codec<SpellCast> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            SpellEffect.CODEC.listOf().fieldOf("effects").forGetter(SpellCast::effects),
                            CastTransformer.CODEC.listOf().optionalFieldOf("transformers",
                                    new ArrayList<>()).forGetter(SpellCast::transformers)
                    )
                    .apply(instance, SpellCast::new)
    );

    /**
     * Apply the spell effects for this spell cast.
     *
     * @param caster Living entity that casts the spell effects.
     * @return Duration that the cast will take.
     */
    public int apply(@NotNull LivingEntity caster) {
        Collection<SpellCasting> casts = List.of(new SpellCasting(caster));
        for (CastTransformer transformer: transformers) {
            casts = casts.stream().flatMap(cast -> transformer.transform(cast).stream())
                    .toList();
        }
        int duration = 0;
        for (SpellCasting cast: casts) {
            SpellCastScheduler.schedule(cast, effects);
            duration = Math.max(duration, cast.getDelay());
        }
        return duration;
    }

    public static SpellCast.Builder builder() {
        return new SpellCast.Builder();
    }

    public static class Builder {
        private final List<SpellEffect> effects = new ArrayList<>();
        private final List<CastTransformer> transformers = new ArrayList<>();

        public SpellCast.Builder addEffect(SpellEffect effect) {
            effects.add(effect);
            return this;
        }

        public SpellCast.Builder addTransformer(CastTransformer transformer) {
            transformers.add(transformer);
            return this;
        }

        public SpellCast build() {
            return new SpellCast(effects, transformers);
        }
    }

}

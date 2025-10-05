package io.github.pistonpoek.magicalscepter.spell.cast;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import io.github.pistonpoek.magicalscepter.spell.cast.transformer.CastTransformer;
import io.github.pistonpoek.magicalscepter.spell.effect.SpellEffect;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
     * Invoke the spell cast for the specified caster.
     *
     * @param caster Living entity that casts the spell effects.
     */
    public void invoke(@NotNull LivingEntity caster) {
        new SpellCasting(this, caster).invoke();
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

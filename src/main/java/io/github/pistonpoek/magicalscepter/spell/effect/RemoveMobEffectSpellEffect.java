package io.github.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.math.random.Random;

import java.util.Optional;

public record RemoveMobEffectSpellEffect(
        RegistryEntryList<StatusEffect> toRemove
) implements SpellEffect {
    public static final MapCodec<RemoveMobEffectSpellEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            RegistryCodecs.entryList(RegistryKeys.STATUS_EFFECT).fieldOf("to_remove").forGetter(RemoveMobEffectSpellEffect::toRemove)
                    )
                    .apply(instance, RemoveMobEffectSpellEffect::new)
    );

    @Override
    public void apply(SpellContext context) {
        Random random = context.getRandom();
        Optional<LivingEntity> target = context.getLivingTarget();
        if (target.isEmpty()) {
            return;
        }

        Optional<RegistryEntry<StatusEffect>> optional = this.toRemove.getRandom(random);
        optional.ifPresent(statusEffectRegistryEntry ->
                target.get().removeStatusEffect(statusEffectRegistryEntry)
        );
    }

    @Override
    public MapCodec<RemoveMobEffectSpellEffect> getCodec() {
        return MAP_CODEC;
    }
}

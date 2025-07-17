package io.github.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.util.math.random.Random;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;

public record ApplyMobEffectSpellEffect(
        RegistryEntryList<StatusEffect> toApply,
        FloatProvider duration,
        FloatProvider amplifier
) implements SpellEffect {
    public static final MapCodec<ApplyMobEffectSpellEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            RegistryCodecs.entryList(RegistryKeys.STATUS_EFFECT).fieldOf("to_apply").forGetter(ApplyMobEffectSpellEffect::toApply),
                            FloatProvider.VALUE_CODEC.fieldOf("duration").forGetter(ApplyMobEffectSpellEffect::duration),
                            FloatProvider.VALUE_CODEC.fieldOf("amplifier").forGetter(ApplyMobEffectSpellEffect::amplifier)
                    )
                    .apply(instance, ApplyMobEffectSpellEffect::new)
    );

    @Override
    public void apply(SpellContext context) {
        Random random = context.getRandom();
        Optional<LivingEntity> target = context.getLivingTarget();
        if (target.isEmpty()) {
            return;
        }

        Optional<RegistryEntry<StatusEffect>> optional = this.toApply.getRandom(random);
        optional.ifPresent(statusEffectRegistryEntry -> target.get().addStatusEffect(
                new StatusEffectInstance(statusEffectRegistryEntry, getDuration(random), getAmplifier(random))
        ));
    }

    private int getDuration(Random random) {
        return Math.round(duration().get(random) * 20.0F);
    }

    private int getAmplifier(Random random) {
        return Math.max(0, Math.round(amplifier().get(random)));
    }

    @Override
    public MapCodec<ApplyMobEffectSpellEffect> getCodec() {
        return MAP_CODEC;
    }
}


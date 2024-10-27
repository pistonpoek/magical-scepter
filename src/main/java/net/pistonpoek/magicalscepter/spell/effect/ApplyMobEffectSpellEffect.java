package net.pistonpoek.magicalscepter.spell.effect;

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
import net.pistonpoek.magicalscepter.spell.cast.context.SpellContext;

public record ApplyMobEffectSpellEffect(
        RegistryEntryList<StatusEffect> toApply,
        FloatProvider minDuration,
        FloatProvider maxDuration,
        FloatProvider minAmplifier,
        FloatProvider maxAmplifier
) implements SpellEffect {
    public static final MapCodec<ApplyMobEffectSpellEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            RegistryCodecs.entryList(RegistryKeys.STATUS_EFFECT).fieldOf("to_apply").forGetter(ApplyMobEffectSpellEffect::toApply),
                            FloatProvider.VALUE_CODEC.fieldOf("min_duration").forGetter(ApplyMobEffectSpellEffect::minDuration),
                            FloatProvider.VALUE_CODEC.fieldOf("max_duration").forGetter(ApplyMobEffectSpellEffect::maxDuration),
                            FloatProvider.VALUE_CODEC.fieldOf("min_amplifier").forGetter(ApplyMobEffectSpellEffect::minAmplifier),
                            FloatProvider.VALUE_CODEC.fieldOf("max_amplifier").forGetter(ApplyMobEffectSpellEffect::maxAmplifier)
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
        return Math.round(MathHelper.nextBetween(random, this.minDuration.get(random), this.maxDuration.get(random)) * 20.0F);
    }

    private int getAmplifier(Random random) {
        return Math.max(0, Math.round(MathHelper.nextBetween(random, this.minAmplifier.get(random), this.maxAmplifier.get(random))));
    }

    @Override
    public MapCodec<ApplyMobEffectSpellEffect> getCodec() {
        return CODEC;
    }
}


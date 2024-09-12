package net.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.util.math.random.Random;

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
    public void apply(ServerWorld world, Entity user, Vec3d pos) {
        if (user instanceof LivingEntity livingEntity) {
            Random random = livingEntity.getRandom();
            Optional<RegistryEntry<StatusEffect>> optional = this.toApply.getRandom(random);
            if (optional.isPresent()) {
                int duration = Math.round(MathHelper.nextBetween(random, this.minDuration.get(random), this.maxDuration.get(random)) * 20.0F);
                int amplifier = Math.max(0, Math.round(MathHelper.nextBetween(random, this.minAmplifier.get(random), this.maxAmplifier.get(random))));
                livingEntity.addStatusEffect(new StatusEffectInstance((RegistryEntry<StatusEffect>)optional.get(), duration, amplifier));
            }
        }
    }

    @Override
    public MapCodec<ApplyMobEffectSpellEffect> getCodec() {
        return CODEC;
    }
}


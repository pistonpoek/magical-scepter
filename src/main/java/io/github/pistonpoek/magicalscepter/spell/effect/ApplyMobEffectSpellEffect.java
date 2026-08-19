package io.github.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public record ApplyMobEffectSpellEffect(
        HolderSet<MobEffect> toApply,
        FloatProvider duration,
        FloatProvider amplifier
) implements SpellEffect {
    public static final MapCodec<ApplyMobEffectSpellEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            RegistryCodecs.homogeneousList(Registries.MOB_EFFECT).fieldOf("to_apply").forGetter(ApplyMobEffectSpellEffect::toApply),
                            FloatProvider.CODEC.fieldOf("duration").forGetter(ApplyMobEffectSpellEffect::duration),
                            FloatProvider.CODEC.fieldOf("amplifier").forGetter(ApplyMobEffectSpellEffect::amplifier)
                    )
                    .apply(instance, ApplyMobEffectSpellEffect::new)
    );

    @Override
    public void apply(SpellContext context) {
        RandomSource random = context.getRandom();
        Optional<LivingEntity> target = context.getLivingTarget();
        if (target.isEmpty()) {
            return;
        }

        Optional<Holder<MobEffect>> optional = this.toApply.getRandomElement(random);
        optional.ifPresent(statusEffectRegistryEntry -> target.get().addEffect(
                new MobEffectInstance(statusEffectRegistryEntry, getDuration(random), getAmplifier(random))
        ));
    }

    private int getDuration(RandomSource random) {
        return Math.round(duration().sample(random) * 20.0F);
    }

    private int getAmplifier(RandomSource random) {
        return Math.max(0, Math.round(amplifier().sample(random)));
    }

    @Override
    public MapCodec<ApplyMobEffectSpellEffect> getCodec() {
        return MAP_CODEC;
    }
}


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
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;

public record RemoveMobEffectSpellEffect(
        HolderSet<MobEffect> toRemove
) implements SpellEffect {
    public static final MapCodec<RemoveMobEffectSpellEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            RegistryCodecs.homogeneousList(Registries.MOB_EFFECT).fieldOf("to_remove").forGetter(RemoveMobEffectSpellEffect::toRemove)
                    )
                    .apply(instance, RemoveMobEffectSpellEffect::new)
    );

    @Override
    public void apply(SpellContext context) {
        RandomSource random = context.getRandom();
        Optional<LivingEntity> target = context.getLivingTarget();
        if (target.isEmpty()) {
            return;
        }

        Optional<Holder<MobEffect>> optional = this.toRemove.getRandomElement(random);
        optional.ifPresent(statusEffectRegistryEntry ->
                target.get().removeEffect(statusEffectRegistryEntry)
        );
    }

    @Override
    public MapCodec<RemoveMobEffectSpellEffect> getCodec() {
        return MAP_CODEC;
    }
}

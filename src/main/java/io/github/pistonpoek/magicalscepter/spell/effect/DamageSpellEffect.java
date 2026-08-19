package io.github.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.core.Holder;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

public record DamageSpellEffect(FloatProvider amount, Holder<DamageType> damageType) implements SpellEffect {
    public static final MapCodec<DamageSpellEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            FloatProviders.CODEC.fieldOf("amount").forGetter(DamageSpellEffect::amount),
                            DamageType.CODEC.fieldOf("damage_type").forGetter(DamageSpellEffect::damageType)
                    )
                    .apply(instance, DamageSpellEffect::new)
    );

    @Override
    public void apply(SpellContext context) {
        context.target().hurtServer(context.getWorld(),
                new DamageSource(damageType, context.caster()),
                amount.sample(context.getRandom()));
    }

    @Override
    public MapCodec<DamageSpellEffect> getCodec() {
        return MAP_CODEC;
    }
}

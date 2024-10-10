package net.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.pistonpoek.magicalscepter.spell.cast.SpellContext;

public record DamageSpellEffect(FloatProvider amount, RegistryEntry<DamageType> damageType) implements SpellEffect {
    public static final MapCodec<DamageSpellEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            FloatProvider.VALUE_CODEC.fieldOf("amount").forGetter(DamageSpellEffect::amount),
                            DamageType.ENTRY_CODEC.fieldOf("damage_type").forGetter(DamageSpellEffect::damageType)
                    )
                    .apply(instance, DamageSpellEffect::new)
    );

    @Override
    public void apply(SpellContext context) {
        context.target().damage(new DamageSource(damageType, context.caster()), amount.get(context.getRandom()));
    }

    @Override
    public MapCodec<? extends SpellEffect> getCodec() {
        return CODEC;
    }
}

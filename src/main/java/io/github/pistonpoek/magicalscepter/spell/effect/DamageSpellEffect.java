package io.github.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.floatprovider.FloatProvider;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;

public record DamageSpellEffect(FloatProvider amount, RegistryEntry<DamageType> damageType) implements SpellEffect {
    public static final MapCodec<DamageSpellEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            FloatProvider.VALUE_CODEC.fieldOf("amount").forGetter(DamageSpellEffect::amount),
                            DamageType.ENTRY_CODEC.fieldOf("damage_type").forGetter(DamageSpellEffect::damageType)
                    )
                    .apply(instance, DamageSpellEffect::new)
    );

    @Override
    public void apply(SpellContext context) {
        context.target().damage(context.getWorld(),
                new DamageSource(damageType, context.caster()),
                amount.get(context.getRandom()));
    }

    @Override
    public MapCodec<DamageSpellEffect> getCodec() {
        return MAP_CODEC;
    }
}

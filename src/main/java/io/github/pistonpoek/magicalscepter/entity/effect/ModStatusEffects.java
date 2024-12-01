package io.github.pistonpoek.magicalscepter.entity.effect;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;

public class ModStatusEffects {
    public static final RegistryEntry<StatusEffect> STABILITY = ModStatusEffects.register("stability",
            new ModStatusEffect(StatusEffectCategory.BENEFICIAL, 0x074857)
                    .addAttributeModifier(EntityAttributes.KNOCKBACK_RESISTANCE,
                        ModIdentifier.of("stability"),
                        0.3f, EntityAttributeModifier.Operation.ADD_VALUE)
                    .addAttributeModifier(EntityAttributes.EXPLOSION_KNOCKBACK_RESISTANCE,
                        ModIdentifier.of("stability"),
                        0.3f, EntityAttributeModifier.Operation.ADD_VALUE));

    public static void init() {

    }

    private static RegistryEntry<StatusEffect> register(String id, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, ModIdentifier.of(id), statusEffect);
    }
}

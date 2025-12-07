package io.github.pistonpoek.magicalscepter.enchantment;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.UnaryOperator;

public interface ModEnchantmentEffectComponentTypes {
    /**
     * Initialize the class for the static fields.
     */
    static void init() {

    }

    ComponentType<EnchantmentValueEffect> EXPERIENCE_STEP = register(
            ModIdentifier.identifier("experience_step"),
            builder -> builder.codec(EnchantmentValueEffect.CODEC)
    );

    private static <T> ComponentType<T> register(String identifier, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, identifier,
                builderOperator.apply(ComponentType.builder()).build());
    }
}

package io.github.pistonpoek.magicalscepter.enchantment;

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
            "experience_step", builder -> builder.codec(EnchantmentValueEffect.CODEC)
    );

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, id,
                builderOperator.apply(ComponentType.builder()).build());
    }
}

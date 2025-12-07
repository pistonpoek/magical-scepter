package io.github.pistonpoek.magicalscepter.enchantment;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.UnaryOperator;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.component.EnchantmentEffectComponentTypes
 */
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

    /**
     * Register an enchantment effect component type with the specified identifier.
     *
     * @param identifier String identifier to register the component type with.
     * @param builderOperator Builder of the component type to register.
     * @return Enchantment effect component type being registered.
     * @param <T> Type of enchantment effect to register with.
     */
    private static <T> ComponentType<T> register(String identifier, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, identifier,
                builderOperator.apply(ComponentType.builder()).build());
    }
}

package io.github.pistonpoek.magicalscepter.enchantment;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;

import java.util.function.UnaryOperator;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.world.item.enchantment.EnchantmentEffectComponents
 */
public interface ModEnchantmentEffectComponentTypes {
    /**
     * Initialize the class for the static fields.
     */
    static void init() {

    }

    DataComponentType<EnchantmentValueEffect> EXPERIENCE_STEP = register(
            "experience_step",
            builder -> builder.persistent(EnchantmentValueEffect.CODEC)
    );

    /**
     * Register an enchantment effect component type with the specified name.
     *
     * @param name String name to register the component type with.
     * @param builderOperator Builder of the component type to register.
     * @return Enchantment effect component type being registered.
     * @param <T> Type of enchantment effect to register with.
     */
    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, ModIdentifier.of(name),
                builderOperator.apply(DataComponentType.builder()).build());
    }
}

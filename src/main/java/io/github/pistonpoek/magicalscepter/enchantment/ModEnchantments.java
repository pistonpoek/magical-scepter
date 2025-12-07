package io.github.pistonpoek.magicalscepter.enchantment;

import io.github.pistonpoek.magicalscepter.registry.tag.ModItemTags;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.value.MultiplyEnchantmentEffect;
import net.minecraft.item.Item;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.ArrayList;
import java.util.List;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.enchantment.Enchantments
 */
public interface ModEnchantments {
    List<RegistryKey<Enchantment>> KEYS = new ArrayList<>();
    RegistryKey<Enchantment> INSIGHT_KEY = of("insight");

    /**
     * Create the enchantment registry key for a specific identifier.
     *
     * @param identifier String identifier to create registry key with.
     * @return Enchantment registry key for the specified identifier.
     */
    private static RegistryKey<Enchantment> of(String identifier) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, ModIdentifier.of(identifier));
    }

    /**
     * Bootstrap the enchantment registry with mod enchantments.
     *
     * @param registry Enchantment registry to bootstrap.
     */
    static void bootstrap(Registerable<Enchantment> registry) {
        RegistryEntryLookup<Item> itemLookup = registry.getRegistryLookup(RegistryKeys.ITEM);

        register(registry, INSIGHT_KEY, new Enchantment.Builder(
                        Enchantment.definition(
                                itemLookup.getOrThrow(ModItemTags.SCEPTER_ENCHANTABLE),
                                2,
                                3,
                                Enchantment.leveledCost(15, 9),
                                Enchantment.leveledCost(65, 9),
                                4,
                                AttributeModifierSlot.MAINHAND
                        )
                ).addEffect(
                        EnchantmentEffectComponentTypes.MOB_EXPERIENCE,
                        new MultiplyEnchantmentEffect(EnchantmentLevelBasedValue.linear(1.3F, 0.35F))
                ).addNonListEffect(
                        ModEnchantmentEffectComponentTypes.EXPERIENCE_STEP,
                        new MultiplyEnchantmentEffect(EnchantmentLevelBasedValue.lookup(List.of(2.0F, 4.0F, 8.0F), EnchantmentLevelBasedValue.constant(8.0F)))
                )
        );
    }

    /**
     * Register an enchantment for the specified registry key.
     *
     * @param registry Enchantment registry to register the enchantment for.
     * @param key      Enchantment key to register the enchantment at.
     * @param builder  Enchantment builder to create enchantment to register.
     */
    private static void register(Registerable<Enchantment> registry,
                                 RegistryKey<Enchantment> key,
                                 Enchantment.Builder builder) {
        KEYS.add(key);
        registry.register(key, builder.build(key.getValue()));
    }
}

package io.github.pistonpoek.magicalscepter.enchantment;

import io.github.pistonpoek.magicalscepter.registry.tag.ModItemTags;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.value.AddEnchantmentEffect;
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
 * @see net.minecraft.enchantment.Enchantments
 */
public class ModEnchantments {
    public static final List<RegistryKey<Enchantment>> ENCHANTMENT_KEYS = new ArrayList<>();
    public static final RegistryKey<Enchantment> INSIGHT_KEY = of("insight");

    private static RegistryKey<Enchantment> of(String identifier) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, ModIdentifier.of(identifier));
    }

    public static void bootstrap(Registerable<Enchantment> registry) {
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
            )
        );
    }

    /**
     * Register an enchantment for the specified identifier.
     *
     * @param registry Enchantment registry to register the enchantment for.
     * @param key Enchantment key to register the enchantment at.
     * @param builder Enchantment builder to create enchantment to register.
     */
    private static void register(Registerable<Enchantment> registry,
                                 RegistryKey<Enchantment> key,
                                 Enchantment.Builder builder) {
        ENCHANTMENT_KEYS.add(key);
        registry.register(key, builder.build(key.getValue()));
    }
}

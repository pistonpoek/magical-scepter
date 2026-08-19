package io.github.pistonpoek.magicalscepter.enchantment;

import io.github.pistonpoek.magicalscepter.registry.tag.ModItemTags;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.MultiplyValue;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.world.item.enchantment.Enchantments
 */
public interface ModEnchantments {
    List<ResourceKey<Enchantment>> KEYS = new ArrayList<>();
    ResourceKey<Enchantment> INSIGHT_KEY = of("insight");

    /**
     * Create the enchantment registry key for a specific name.
     *
     * @param name String name to create registry key with.
     * @return Enchantment registry key for the specified name.
     */
    private static ResourceKey<Enchantment> of(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, ModIdentifier.of(name));
    }

    /**
     * Bootstrap the enchantment registry with mod enchantments.
     *
     * @param registry Enchantment registry to bootstrap.
     */
    static void bootstrap(BootstrapContext<Enchantment> registry) {
        HolderGetter<Item> itemLookup = registry.lookup(Registries.ITEM);

        register(registry, INSIGHT_KEY, new Enchantment.Builder(
                        Enchantment.definition(
                                itemLookup.getOrThrow(ModItemTags.SCEPTER_ENCHANTABLE),
                                2,
                                3,
                                Enchantment.dynamicCost(15, 9),
                                Enchantment.dynamicCost(65, 9),
                                4,
                                EquipmentSlotGroup.MAINHAND
                        )
                ).withEffect(
                        EnchantmentEffectComponents.MOB_EXPERIENCE,
                        new MultiplyValue(LevelBasedValue.perLevel(1.3F, 0.35F))
                ).withSpecialEffect(
                        ModEnchantmentEffectComponentTypes.EXPERIENCE_STEP,
                        new MultiplyValue(LevelBasedValue.lookup(List.of(2.0F, 4.0F, 8.0F), LevelBasedValue.constant(8.0F)))
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
    private static void register(BootstrapContext<Enchantment> registry,
                                 ResourceKey<Enchantment> key,
                                 Enchantment.Builder builder) {
        KEYS.add(key);
        registry.register(key, builder.build(key.identifier()));
    }
}

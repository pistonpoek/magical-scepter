package io.github.pistonpoek.magicalscepter.item;

import io.github.pistonpoek.magicalscepter.component.ScepterExperienceComponent;
import io.github.pistonpoek.magicalscepter.registry.tag.ScepterTags;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Rarity;
import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.entity.ModEntityType;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;

import java.util.Set;
import java.util.function.Function;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.item.Items
 */
public class ModItems {
    public static final Item SCEPTER = register("scepter", new Item.Settings().maxCount(1).rarity(Rarity.RARE));
    public static final Item ARCANE_SCEPTER = register("arcane_scepter", ArcaneScepterItem::new,
            new Item.Settings().maxDamage(64).rarity(Rarity.RARE)
                    .component(ModDataComponentTypes.SCEPTER_EXPERIENCE, ScepterExperienceComponent.DEFAULT));
    public static final Item MAGICAL_SCEPTER = register("magical_scepter", MagicalScepterItem::new,
            new Item.Settings().maxDamage(64).rarity(Rarity.RARE)
                    .component(ModDataComponentTypes.SCEPTER_CONTENTS, ScepterContentsComponent.DEFAULT));

    public static final Item REFRACTOR_SPAWN_EGG = register("refractor_spawn_egg",
            settings -> new SpawnEggItem(ModEntityType.REFRACTOR, settings));

    /**
     * Initialize the class for the static fields.
     */
    public static void init() {
        MagicalScepter.LOGGER.info("Registering Mod Items for " + ModIdentifier.MOD_NAME);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(ModItems::addItemsToToolsItemGroup);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(ModItems::addItemsToCombatItemGroup);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientsGroup);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(ModItems::addItemsToSpawnEggsGroup);
    }

    /**
     * Get the item stack set of infused scepters.
     *
     * @param registryWrapper Registry wrapper to get scepter registry entries from.
     * @return Set of all infused magical scepter item stacks.
     */
    private static Set<ItemStack> getInfusedScepters(RegistryWrapper<Scepter> registryWrapper) {
        Set<ItemStack> scepters = ItemStackSet.create();
        for (RegistryEntry<Scepter> scepter : registryWrapper.getOrThrow(ScepterTags.INFUSED)) {
            scepters.add(ScepterHelper.createMagicalScepter(scepter));
        }
        return scepters;
    }

    /**
     * Add tools group items to the specified item group entries.
     *
     * @param entries Tools item group entries to add items to.
     */
    private static void addItemsToToolsItemGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Items.FISHING_ROD, ARCANE_SCEPTER);
    }

    /**
     * Add combat group items to the specified item group entries.
     *
     * @param entries Combat item group entries to add items to.
     */
    private static void addItemsToCombatItemGroup(FabricItemGroupEntries entries) {
        entries.getContext().lookup().getOptional(ModRegistryKeys.SCEPTER)
            .ifPresent(registryWrapper -> {
                RegistryEntry<Scepter> magicalScepter = registryWrapper.getOrThrow(Scepters.MAGICAL_KEY);
                entries.addAfter(Items.MACE, ScepterHelper.createMagicalScepter(magicalScepter));
                entries.addAfter(Items.WIND_CHARGE, getInfusedScepters(registryWrapper),
                    ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        });
    }

    /**
     * Add ingredients group items to the specified item group entries.
     *
     * @param entries Ingredients item group entries to add items to.
     */
    private static void addItemsToIngredientsGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Items.HEAVY_CORE, SCEPTER);
        entries.addBefore(Items.BOWL, Items.BROWN_MUSHROOM);
        entries.addBefore(Items.BOWL, Items.RED_MUSHROOM);
    }

    /**
     * Add spawn eggs group items to the specified item group entries.
     *
     * @param entries Spawn eggs item group entries to add items to.
     */
    private static void addItemsToSpawnEggsGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Items.RAVAGER_SPAWN_EGG, REFRACTOR_SPAWN_EGG);
    }

    /**
     * Get the item registry key for the specified identifier.
     *
     * @param identifier String to create item registry key for.
     * @return Item registry key made with the specified identifier.
     */
    private static RegistryKey<Item> keyOf(String identifier) {
        return RegistryKey.of(RegistryKeys.ITEM, ModIdentifier.of(identifier));
    }

    /**
     * Register an item with specified item settings.
     *
     * @param identifier String identifier to register the item for.
     * @param settings Settings to register the item with.
     * @return Item registered.
     */
    public static Item register(String identifier, Item.Settings settings) {
        return register(identifier, Item::new, settings);
    }

    /**
     * Register an item with specified item factory.
     *
     * @param identifier String identifier to register the item for.
     * @param factory Item factory that uses item settings to create an item.
     * @return Item registered.
     */
    private static Item register(String identifier, Function<Item.Settings, Item> factory) {
        return register(identifier, factory, new Item.Settings());
    }

    /**
     * Register an item with a specified item factory and specified item settings.
     *
     * @param identifier String identifier to register the item for.
     * @param factory Item factory that uses item settings to create an item.
     * @param settings Settings to register the item with.
     * @return Item registered.
     */
    private static Item register(String identifier, Function<Item.Settings, Item> factory, Item.Settings settings) {
        return Items.register(keyOf(identifier), factory, settings.useItemPrefixedTranslationKey());
    }
}

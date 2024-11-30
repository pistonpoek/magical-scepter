package io.github.pistonpoek.magicalscepter.item;

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
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.registry.ModTags;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;

import java.util.Set;
import java.util.function.Function;

public class ModItems {
    public static final Item SCEPTER = register("scepter", new Item.Settings().maxCount(1).rarity(Rarity.RARE));
    public static final Item MAGICAL_SCEPTER = register("magical_scepter", MagicalScepterItem::new,
            new Item.Settings().maxDamage(300).rarity(Rarity.RARE)
                    .component(ModDataComponentTypes.SCEPTER_CONTENTS, ScepterContentsComponent.DEFAULT));

    public static final Item REFRACTOR_SPAWN_EGG = register("refractor_spawn_egg",
            settings -> new SpawnEggItem(ModEntityType.REFRACTOR, settings));

    private static void addInfusedScepters(FabricItemGroupEntries entries, RegistryWrapper<Scepter> registryWrapper, ItemGroup.StackVisibility visibility) {
        Set<ItemStack> scepters = ItemStackSet.create();
        registryWrapper.getOrThrow(ModTags.Scepters.INFUSED).stream()
                .map(ScepterHelper::createScepter)
                .forEach(scepters::add);
        entries.addAfter(Items.WIND_CHARGE, scepters, visibility);
    }

    private static void addItemsToCombatItemGroup(FabricItemGroupEntries entries) {
        entries.getContext().lookup().getOptional(ModRegistryKeys.SCEPTER).ifPresent(registryWrapper -> {
            RegistryEntry<Scepter> magicalScepter = registryWrapper.getOrThrow(Scepters.MAGICAL_KEY);
                    entries.addAfter(Items.TRIDENT, ScepterHelper.createScepter(magicalScepter));
            addInfusedScepters(entries, registryWrapper, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        });
    }

    private static void addItemsToIngredientsGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Items.BOWL, SCEPTER);
    }

    private static void addItemsToSpawnEggsGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Items.RAVAGER_SPAWN_EGG, REFRACTOR_SPAWN_EGG);
    }

    private static RegistryKey<Item> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.ITEM, ModIdentifier.of(id));
    }

    public static Item register(String id, Item.Settings settings) {
        return register(id, Item::new, settings);
    }

    private static Item register(String id, Function<Item.Settings, Item> factory) {
        return register(id, factory, new Item.Settings());
    }

    private static Item register(String id, Function<Item.Settings, Item> factory, Item.Settings settings) {
        return Items.register(keyOf(id), factory, settings.useItemPrefixedTranslationKey());
    }

    public static void init() {
        MagicalScepter.LOGGER.info("Registering Mod Items for " + ModIdentifier.MOD_NAME);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(ModItems::addItemsToCombatItemGroup);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientsGroup);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(ModItems::addItemsToSpawnEggsGroup);
    }

}

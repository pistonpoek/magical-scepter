package net.pistonpoek.magicalscepter.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Rarity;
import net.pistonpoek.magicalscepter.MagicalScepter;
import net.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import net.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import net.pistonpoek.magicalscepter.entity.ModEntityType;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.pistonpoek.magicalscepter.scepter.Scepter;
import net.pistonpoek.magicalscepter.scepter.ScepterHelper;
import net.pistonpoek.magicalscepter.scepter.Scepters;

import java.util.Set;
import java.util.function.Predicate;

public class ModItems {
    public static final Item SCEPTER = registerItem("scepter",
            new Item(new Item.Settings().maxCount(1).rarity(Rarity.RARE)));
    public static final Item MAGICAL_SCEPTER = registerItem("magical_scepter",
            new ScepterItem(new Item.Settings().maxDamage(300).rarity(Rarity.RARE)
                    .component(ModDataComponentTypes.SCEPTER_CONTENTS, ScepterContentsComponent.DEFAULT)));

    private static final Predicate<RegistryEntry.Reference<Scepter>> IS_MAGICAL_SCEPTER =
            entry -> entry.matchesKey(Scepters.MAGICAL_KEY);
    public static final Item REFRACTOR_SPAWN_EGG = registerItem("refractor_spawn_egg",
            new SpawnEggItem(ModEntityType.REFRACTOR, 9804699, 6307420, new Item.Settings()));

    private static void addScepters(FabricItemGroupEntries entries, RegistryWrapper<Scepter> registryWrapper, ItemGroup.StackVisibility visibility) {
        Set<ItemStack> scepters = ItemStackSet.create();
        registryWrapper.streamEntries().filter(IS_MAGICAL_SCEPTER.negate())
                .map(ScepterHelper::createScepter)
                .forEach(scepters::add);
        entries.addAfter(Items.WIND_CHARGE, scepters, visibility);
    }

    private static void addItemsToCombatItemGroup(FabricItemGroupEntries entries) {
        entries.getContext().lookup().getOptionalWrapper(ModRegistryKeys.SCEPTER).ifPresent(registryWrapper -> {
            registryWrapper.streamEntries().filter(IS_MAGICAL_SCEPTER).forEach(entry -> {
                    entries.addAfter(Items.TRIDENT, ScepterHelper.createScepter(entry));
            });
            addScepters(entries, registryWrapper, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        });
    }

    private static void addItemsToIngredientsGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Items.BOWL, SCEPTER);
    }

    private static void addItemsToSpawnEggsGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Items.RAVAGER_SPAWN_EGG, REFRACTOR_SPAWN_EGG);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, ModIdentifier.of(name), item);
    }

    public static void init() {
        MagicalScepter.LOGGER.info("Registering Mod Items for " + ModIdentifier.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(ModItems::addItemsToCombatItemGroup);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientsGroup);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(ModItems::addItemsToSpawnEggsGroup);
    }

}

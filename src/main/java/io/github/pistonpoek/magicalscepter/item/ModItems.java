package io.github.pistonpoek.magicalscepter.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryWrapper;
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

public class ModItems {
    public static final Item SCEPTER = registerItem("scepter",
            new Item(new Item.Settings().maxCount(1).rarity(Rarity.RARE)));
    public static final Item MAGICAL_SCEPTER = registerItem("magical_scepter",
            new MagicalScepterItem(new Item.Settings().maxDamage(300).rarity(Rarity.RARE)
                    .component(ModDataComponentTypes.SCEPTER_CONTENTS, ScepterContentsComponent.DEFAULT)));

    public static final Item REFRACTOR_SPAWN_EGG = registerItem("refractor_spawn_egg",
            new SpawnEggItem(ModEntityType.REFRACTOR, 9804699, 6307420, new Item.Settings()));

    private static void addInfusedScepters(FabricItemGroupEntries entries, RegistryWrapper<Scepter> registryWrapper, ItemGroup.StackVisibility visibility) {
        Set<ItemStack> scepters = ItemStackSet.create();
        registryWrapper.getOrThrow(ModTags.Scepters.INFUSED).stream()
                .map(ScepterHelper::createScepter)
                .forEach(scepters::add);
        entries.addAfter(Items.WIND_CHARGE, scepters, visibility);
    }

    private static void addItemsToCombatItemGroup(FabricItemGroupEntries entries) {
        entries.getContext().lookup().getOptionalWrapper(ModRegistryKeys.SCEPTER).ifPresent(registryWrapper -> {
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

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, ModIdentifier.of(name), item);
    }

    public static void init() {
        MagicalScepter.LOGGER.info("Registering Mod Items for " + ModIdentifier.MOD_NAME);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(ModItems::addItemsToCombatItemGroup);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientsGroup);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(ModItems::addItemsToSpawnEggsGroup);
    }

}

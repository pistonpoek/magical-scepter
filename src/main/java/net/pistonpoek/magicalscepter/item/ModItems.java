package net.pistonpoek.magicalscepter.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Rarity;
import net.pistonpoek.magicalscepter.MagicalScepter;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.pistonpoek.magicalscepter.scepter.Scepter;
import net.pistonpoek.magicalscepter.scepter.ScepterHelper;

import java.util.List;

public class ModItems {
    public static final Item EMPTY_SCEPTER = registerItem("empty_scepter",
            new Item(new Item.Settings().maxCount(1).rarity(Rarity.RARE)));
    public static final Item SCEPTER = registerItem("scepter",
            new ScepterItem(new Item.Settings().maxDamage(300).rarity(Rarity.RARE)));

    private static void addScepters(FabricItemGroupEntries entries, RegistryWrapper<Scepter> registryWrapper, Item item, ItemGroup.StackVisibility visibility) {
        registryWrapper.streamEntries().filter(entry -> !ScepterHelper.createScepter(entry).equals(SCEPTER.getDefaultStack()))
                .map(ScepterHelper::createScepter)
                .forEach(stack -> entries.addAfter((ItemConvertible) item, List.of((ItemStack) stack), visibility));
    }

    private static void addItemsToCombatItemGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Items.SPECTRAL_ARROW, SCEPTER);
        entries.getContext().lookup().getOptionalWrapper(ModRegistryKeys.SCEPTER).ifPresent(wrapper -> {
            addScepters(entries, wrapper, SCEPTER, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        });
    }

    private static void addItemsToIngredientsGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Items.BOWL, EMPTY_SCEPTER);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, ModIdentifier.of(name), item);
    }

    public static void registerModItems() {
        MagicalScepter.LOGGER.info("Registering Mod Items for " + ModIdentifier.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(ModItems::addItemsToCombatItemGroup);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientsGroup);
    }

}

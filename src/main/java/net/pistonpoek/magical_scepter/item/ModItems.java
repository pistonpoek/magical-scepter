package net.pistonpoek.magical_scepter.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Rarity;
import net.pistonpoek.magical_scepter.MagicalScepter;
import net.pistonpoek.magical_scepter.ModDataComponentTypes;
import net.pistonpoek.magical_scepter.ModRegistryKeys;
import net.pistonpoek.magical_scepter.item.scepter.*;
import net.pistonpoek.magical_scepter.util.ModIdentifier;

import java.util.List;

public class ModItems {
    public static final Item SCEPTER = registerItem("scepter",
            new ScepterItem(new Item.Settings().maxDamage(300).rarity(Rarity.RARE)
                    .component(ModDataComponentTypes.SCEPTER, Scepters.MAGICAL)));
    public static final Item EMPTY_SCEPTER = registerItem("empty_scepter",
            new Item(new Item.Settings().maxCount(1).rarity(Rarity.RARE)));

    // Trident max damage : 250

    // Scepter max damage : 300?

    // Shield max damage : 336
    // Bow max damage : 384
    // Crossbow max damage : 465

    // See ItemGroups.java for how Potions and alike add the items to the entries.
    private static void addScepters(FabricItemGroupEntries entries, RegistryWrapper<Scepter> registryWrapper, Item item, ItemGroup.StackVisibility visibility) {
        registryWrapper.streamEntries().filter(entry -> !entry.matches(Scepters.MAGICAL))
                .map(entry -> ScepterUtil.setScepter(new ItemStack(item), entry))
                .forEach(stack -> entries.addAfter((ItemConvertible) SCEPTER, List.of((ItemStack) stack), visibility));
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

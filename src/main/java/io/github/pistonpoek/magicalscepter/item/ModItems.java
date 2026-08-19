package io.github.pistonpoek.magicalscepter.item;

import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.component.ScepterExperienceComponent;
import io.github.pistonpoek.magicalscepter.entity.ModEntityType;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.registry.tag.ModItemTags;
import io.github.pistonpoek.magicalscepter.registry.tag.ScepterTags;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTabOutput;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.UseCooldown;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static io.github.pistonpoek.magicalscepter.util.SpawnEggDispenserBehavior.addSpawnEggDispenserBehavior;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.world.item.Items
 */
public class ModItems {
    private static final Function<Item.Properties, Item.Properties> SCEPTER_SETTINGS = (settings) ->
            settings.rarity(Rarity.RARE);
    private static final Function<Item.Properties, Item.Properties> USABLE_SCEPTER_SETTINGS = SCEPTER_SETTINGS
            .andThen((settings) -> settings.durability(64).repairable(ModItemTags.SCEPTER_MATERIALS));
    private static final Function<Item.Properties, Item.Properties> ARCANE_SCEPTER_SETTINGS = USABLE_SCEPTER_SETTINGS
            .andThen((settings) -> settings.component(DataComponents.USE_COOLDOWN,
                    new UseCooldown(0.5F, Optional.of(ModIdentifier.of("arcane_scepters")))));

    public static final Item SCEPTER = register("scepter", SCEPTER_SETTINGS.apply(new Item.Properties().stacksTo(1)));
    public static final Item ARCANE_SCEPTER = register("arcane_scepter", ArcaneScepterItem::new,
            ARCANE_SCEPTER_SETTINGS.apply(new Item.Properties()
                    .component(ModDataComponentTypes.SCEPTER_EXPERIENCE, ScepterExperienceComponent.DEFAULT)));
    public static final Item CHARGED_ARCANE_SCEPTER = register("charged_arcane_scepter", ArcaneScepterItem::new,
            ARCANE_SCEPTER_SETTINGS.apply(new Item.Properties()
                    .component(ModDataComponentTypes.SCEPTER_EXPERIENCE,
                            ScepterExperienceComponent.of(ArcaneScepterItem.EXPERIENCE_STEP))
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)));
    public static final Item MAGICAL_SCEPTER = register("magical_scepter", MagicalScepterItem::new,
            USABLE_SCEPTER_SETTINGS.apply(new Item.Properties()
                    .component(ModDataComponentTypes.SCEPTER_CONTENTS, ScepterContentsComponent.DEFAULT)));

    public static final Item SORCERER_SPAWN_EGG = register("sorcerer_spawn_egg", SpawnEggItem::new,
            new Item.Properties().spawnEgg(ModEntityType.SORCERER));

    /**
     * Initialize the class for the static fields.
     */
    public static void init() {
        MagicalScepter.LOGGER.info("Registering Mod Items for " + ModIdentifier.MOD_NAME);

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(ModItems::addItemsToToolsItemGroup);
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(ModItems::addItemsToCombatItemGroup);
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(ModItems::addItemsToIngredientsGroup);
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS).register(ModItems::addItemsToSpawnEggsGroup);

        addSpawnEggDispenserBehavior(SORCERER_SPAWN_EGG);
    }

    /**
     * Get the item stack set of infused scepters.
     *
     * @param registryWrapper Registry wrapper to get scepter registry entries from.
     * @return Set of all infused magical scepter item stacks.
     */
    private static Set<ItemStack> getInfusedScepters(HolderLookup<Scepter> registryWrapper) {
        Set<ItemStack> scepters = ItemStackLinkedSet.createTypeAndComponentsSet();
        for (Holder<Scepter> scepter : registryWrapper.getOrThrow(ScepterTags.INFUSED)) {
            scepters.add(ScepterHelper.createMagicalScepter(scepter));
        }
        return scepters;
    }

    /**
     * Add tools group items to the specified item group entries.
     *
     * @param entries Tools item group entries to add items to.
     */
    private static void addItemsToToolsItemGroup(FabricCreativeModeTabOutput entries) {
        entries.insertAfter(Items.FISHING_ROD, ARCANE_SCEPTER);
    }

    /**
     * Add combat group items to the specified item group entries.
     *
     * @param entries Combat item group entries to add items to.
     */
    private static void addItemsToCombatItemGroup(FabricCreativeModeTabOutput entries) {
        entries.getContext().holders().lookup(ModRegistryKeys.SCEPTER)
                .ifPresent(registryWrapper -> {
                    Holder<Scepter> magicalScepter = registryWrapper.getOrThrow(Scepters.MAGICAL_KEY);
                    entries.insertAfter(Items.MACE, ScepterHelper.createMagicalScepter(magicalScepter));
                    entries.insertAfter(Items.WIND_CHARGE, getInfusedScepters(registryWrapper),
                            CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                });
    }

    /**
     * Add ingredients group items to the specified item group entries.
     *
     * @param entries Ingredients item group entries to add items to.
     */
    private static void addItemsToIngredientsGroup(FabricCreativeModeTabOutput entries) {
        entries.insertAfter(Items.HEAVY_CORE, SCEPTER);
        entries.insertBefore(Items.BOWL, Items.BROWN_MUSHROOM);
        entries.insertBefore(Items.BOWL, Items.RED_MUSHROOM);
    }

    /**
     * Add spawn eggs group items to the specified item group entries.
     *
     * @param entries Spawn eggs item group entries to add items to.
     */
    private static void addItemsToSpawnEggsGroup(FabricCreativeModeTabOutput entries) {
        entries.insertAfter(Items.RAVAGER_SPAWN_EGG, SORCERER_SPAWN_EGG);
    }

    /**
     * Get the item registry key for the specified identifier.
     *
     * @param identifier String to create item registry key for.
     * @return Item registry key made with the specified identifier.
     */
    private static ResourceKey<Item> keyOf(String identifier) {
        return ResourceKey.create(Registries.ITEM, ModIdentifier.of(identifier));
    }

    /**
     * Register an item with specified item settings.
     *
     * @param identifier String identifier to register the item for.
     * @param settings   Settings to register the item with.
     * @return Item registered.
     */
    public static Item register(String identifier, Item.Properties settings) {
        return register(identifier, Item::new, settings);
    }

    /**
     * Register an item with specified item factory.
     *
     * @param identifier String identifier to register the item for.
     * @param factory    Item factory that uses item settings to create an item.
     * @return Item registered.
     */
    private static Item register(String identifier, Function<Item.Properties, Item> factory) {
        return register(identifier, factory, new Item.Properties());
    }

    /**
     * Register an item with a specified item factory and specified item settings.
     *
     * @param identifier String identifier to register the item for.
     * @param factory    Item factory that uses item settings to create an item.
     * @param settings   Settings to register the item with.
     * @return Item registered.
     */
    private static Item register(String identifier, Function<Item.Properties, Item> factory, Item.Properties settings) {
        return registerItem(keyOf(identifier), factory, settings.useItemDescriptionPrefix());
    }

    /**
     * Register an item.
     *
     * @param key         Resource key to register the item for.
     * @param itemFactory Item factory to create item with.
     * @param properties  Properties to give item.
     * @return Item registered
     */
    private static Item registerItem(final ResourceKey<Item> key, final Function<Item.Properties, Item> itemFactory, final Item.Properties properties) {
        Item item = itemFactory.apply(properties.setId(key));
        if (item instanceof BlockItem blockItem) {
            blockItem.registerBlocks(Item.BY_BLOCK, item);
        }

        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }
}

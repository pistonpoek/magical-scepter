package io.github.pistonpoek.magicalscepter.item;

import io.github.pistonpoek.magicalscepter.entity.ModEntityTypeIds;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.references.ItemIds
 */
public class ModItemIds {
    public static final ResourceKey<Item> SCEPTER = create("scepter");
    public static final ResourceKey<Item> ARCANE_SCEPTER = create("arcane_scepter");
    public static final ResourceKey<Item> CHARGED_ARCANE_SCEPTER = create("charged_arcane_scepter");
    public static final ResourceKey<Item> MAGICAL_SCEPTER = create("magical_scepter");

    public static final ResourceKey<Item> SORCERER_SPAWN_EGG = createSpawnEgg(ModEntityTypeIds.SORCERER);

    /**
     * Create the item resource key for the specified name.
     *
     * @param name Item name to create item resource key for.
     * @return Item resource key made with the specified name.
     */
    private static ResourceKey<Item> create(final String name) {
        return ResourceKey.create(Registries.ITEM, ModIdentifier.of(name));
    }

    /**
     * Create the spawn egg item registry key for the entity.
     *
     * @param entity Entity resource key to create spawn egg item resource key for.
     * @return Resource key made for the entity spawn egg item.
     */
    private static ResourceKey<Item> createSpawnEgg(final ResourceKey<EntityType<?>> entity) {
        return entity.dependent(Registries.ITEM, "_spawn_egg");
    }
}

package io.github.pistonpoek.magicalscepter.registry.tag;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.tags.ItemTags
 */
public class ModItemTags {
    public static final TagKey<Item> SORCERER_PREFERRED_WEAPONS = of("sorcerer_preferred_weapons");
    public static final TagKey<Item> SCEPTERS = of("scepters");
    public static final TagKey<Item> ARCANE_SCEPTERS = of("arcane_scepters");
    public static final TagKey<Item> SCEPTER_MATERIALS = of("scepter_materials");
    public static final TagKey<Item> SCEPTER_ENCHANTABLE = of("enchantable/scepter");

    /**
     * Get the item tag key for the specified name.
     *
     * @param name String name to get item tag for.
     * @return Item tag key for the specified name.
     */
    public static TagKey<Item> of(String name) {
        return TagKey.create(Registries.ITEM, ModIdentifier.of(name));
    }
}

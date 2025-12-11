package io.github.pistonpoek.magicalscepter.registry.tag;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.registry.tag.ItemTags
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
        return TagKey.of(RegistryKeys.ITEM, ModIdentifier.of(name));
    }
}

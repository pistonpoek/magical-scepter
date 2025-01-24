package io.github.pistonpoek.magicalscepter.registry.tag;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.registry.tag.ItemTags
 */
public class ModItemTags {
    public static final TagKey<Item> REFRACTOR_PREFERRED_WEAPONS = of("refractor_preferred_weapons");

    public static TagKey<Item> of(String name) {
        return TagKey.of(RegistryKeys.ITEM, ModIdentifier.of(name));
    }
}

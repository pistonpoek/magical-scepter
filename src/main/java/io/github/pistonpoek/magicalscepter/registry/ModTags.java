package io.github.pistonpoek.magicalscepter.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;

public class ModTags {

    public static class Blocks {

        public static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, ModIdentifier.of(name));
        }
    }

    public static class Items {

        public static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, ModIdentifier.of(name));
        }
    }

    public static class Scepters {
        public static final TagKey<Scepter> INFUSED = createTag("infused");

        public static TagKey<Scepter> createTag(String name) {
            return TagKey.of(ModRegistryKeys.SCEPTER, ModIdentifier.of(name));
        }
    }
}

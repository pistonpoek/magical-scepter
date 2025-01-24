package io.github.pistonpoek.magicalscepter.registry.tag;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import net.minecraft.registry.tag.TagKey;

public class ScepterTags {
    public static final TagKey<Scepter> INFUSED = of("infused");

    public static TagKey<Scepter> of(String name) {
        return TagKey.of(ModRegistryKeys.SCEPTER, ModIdentifier.of(name));
    }
}

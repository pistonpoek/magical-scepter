package io.github.pistonpoek.magicalscepter.registry.tag;

import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.registry.tag.TagKey;

/**
 * Collection of scepter tags.
 */
public class ScepterTags {
    public static final TagKey<Scepter> INFUSED = of("infused");

    /**
     * Get the scepter tag key for the specified name.
     *
     * @param name String name to get scepter tag for.
     * @return Scepter tag key for the specified name.
     */
    public static TagKey<Scepter> of(String name) {
        return TagKey.of(ModRegistryKeys.SCEPTER, ModIdentifier.of(name));
    }
}

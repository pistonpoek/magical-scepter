package io.github.pistonpoek.magicalscepter.registry.tag;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.tags.StructureTags
 */
public interface ModStructureTags {
    TagKey<Structure> ON_OLD_TAIGA_CABIN_MAPS = of("on_old_taiga_cabin_maps");

    /**
     * Create a structure tag key for the mod namespace.
     *
     * @param name Name of the structure tag.
     * @return Structure tag key created.
     */
    private static TagKey<Structure> of(final String name) {
        return TagKey.create(Registries.STRUCTURE, ModIdentifier.of(name));
    }
}

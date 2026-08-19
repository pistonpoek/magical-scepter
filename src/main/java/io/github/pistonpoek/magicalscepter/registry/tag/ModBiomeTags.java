package io.github.pistonpoek.magicalscepter.registry.tag;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.tags.BiomeTags
 */
public class ModBiomeTags {
    public static final TagKey<Biome> OLD_TAIGA_CABIN_HAS_STRUCTURE = of("has_structure/old_taiga_cabin");

    /**
     * Get the biome tag key for the specified name.
     *
     * @param name String name to get biome tag for.
     * @return Biome tag key for the specified name.
     */
    private static TagKey<Biome> of(String name) {
        return TagKey.create(Registries.BIOME, ModIdentifier.of(name));
    }
}

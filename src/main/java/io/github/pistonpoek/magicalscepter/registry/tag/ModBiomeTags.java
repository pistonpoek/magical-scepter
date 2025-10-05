package io.github.pistonpoek.magicalscepter.registry.tag;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.registry.tag.BiomeTags
 */
public class ModBiomeTags {
    public static final TagKey<Biome> OLD_TAIGA_CABIN_HAS_STRUCTURE = of("has_structure/old_taiga_cabin");

    private static TagKey<Biome> of(String identifier) {
        return TagKey.of(RegistryKeys.BIOME, ModIdentifier.of(identifier));
    }
}

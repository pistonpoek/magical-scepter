package net.pistonpoek.magicalscepter.datagen;

import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;

public class ModBiomeTags {
    public static final TagKey<Biome> OLD_GROWTH_TAIGA_HUT_HAS_STRUCTURE = of("has_structure/old_growth_taiga_hut");

    private ModBiomeTags() {
    }

    private static TagKey<Biome> of(String id) {
        return TagKey.of(RegistryKeys.BIOME, ModIdentifier.of(id));
    }
}

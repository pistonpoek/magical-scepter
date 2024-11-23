package io.github.pistonpoek.magicalscepter.datagen;

import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;

public class ModBiomeTags {
    public static final TagKey<Biome> OLD_TAIGA_CABIN_HAS_STRUCTURE = of("has_structure/old_taiga_cabin");

    private ModBiomeTags() {
    }

    private static TagKey<Biome> of(String id) {
        return TagKey.of(RegistryKeys.BIOME, ModIdentifier.of(id));
    }
}

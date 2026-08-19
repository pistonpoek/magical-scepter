package io.github.pistonpoek.magicalscepter.structure;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.StructureSet;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.world.level.levelgen.structure.BuiltinStructureSets
 */
public interface ModStructureSetKeys {
    ResourceKey<StructureSet> OLD_TAIGA_CABIN = of("old_taiga_cabins");

    /**
     * Get a structure set registry key for the specified name.
     *
     * @param name String name to get structure set registry key for.
     * @return Structure set registry key with the specified name.
     */
    private static ResourceKey<StructureSet> of(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, ModIdentifier.of(name));
    }
}

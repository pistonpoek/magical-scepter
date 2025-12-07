package io.github.pistonpoek.magicalscepter.structure;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.StructureSet;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.structure.StructureSetKeys
 */
public interface ModStructureSetKeys {
    RegistryKey<StructureSet> OLD_TAIGA_CABIN = of("old_taiga_cabins");

    /**
     * Get a structure set registry key for the specified name.
     *
     * @param name String name to get structure set registry key for.
     * @return Structure set registry key with the specified name.
     */
    private static RegistryKey<StructureSet> of(String name) {
        return RegistryKey.of(RegistryKeys.STRUCTURE_SET, ModIdentifier.of(name));
    }
}

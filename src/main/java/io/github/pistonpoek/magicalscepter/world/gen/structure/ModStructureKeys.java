package io.github.pistonpoek.magicalscepter.world.gen.structure;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.structure.Structure;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.world.gen.structure.StructureKeys
 */
public interface ModStructureKeys {
    RegistryKey<Structure> OLD_TAIGA_CABIN = of("old_taiga_cabin");

    /**
     * Get the structure registry key for the specified name.
     *
     * @param name String name to get the structure registry key for.
     * @return Structure registry key with the specified name.
     */
    private static RegistryKey<Structure> of(String name) {
        return RegistryKey.of(RegistryKeys.STRUCTURE, ModIdentifier.of(name));
    }
}

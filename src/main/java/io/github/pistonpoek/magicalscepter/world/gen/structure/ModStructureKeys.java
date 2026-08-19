package io.github.pistonpoek.magicalscepter.world.gen.structure;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.world.level.levelgen.structure.BuiltinStructures
 */
public interface ModStructureKeys {
    ResourceKey<Structure> OLD_TAIGA_CABIN = of("old_taiga_cabin");

    /**
     * Get the structure registry key for the specified name.
     *
     * @param name String name to get the structure registry key for.
     * @return Structure registry key with the specified name.
     */
    private static ResourceKey<Structure> of(String name) {
        return ResourceKey.create(Registries.STRUCTURE, ModIdentifier.of(name));
    }
}

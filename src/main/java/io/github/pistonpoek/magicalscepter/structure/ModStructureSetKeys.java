package io.github.pistonpoek.magicalscepter.structure;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.StructureSet;

public interface ModStructureSetKeys {
    RegistryKey<StructureSet> OLD_TAIGA_CABIN = of("old_taiga_cabins");

    private static RegistryKey<StructureSet> of(String identifier) {
        return RegistryKey.of(RegistryKeys.STRUCTURE_SET, ModIdentifier.of(identifier));
    }
}

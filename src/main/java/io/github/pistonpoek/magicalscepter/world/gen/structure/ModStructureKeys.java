package io.github.pistonpoek.magicalscepter.world.gen.structure;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.structure.Structure;

/**
public interface ModStructureKeys {
    RegistryKey<Structure> OLD_TAIGA_CABIN = of("old_taiga_cabin");

    private static RegistryKey<Structure> of(String identifier) {
        return RegistryKey.of(RegistryKeys.STRUCTURE, ModIdentifier.of(identifier));
    }
}

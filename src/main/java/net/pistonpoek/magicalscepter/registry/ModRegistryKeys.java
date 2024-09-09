package net.pistonpoek.magicalscepter.registry;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.pistonpoek.magicalscepter.scepter.Scepter;

public class ModRegistryKeys {
    public static final RegistryKey<Registry<Scepter>> SCEPTER =
            RegistryKey.ofRegistry(ModIdentifier.of("scepter"));
}

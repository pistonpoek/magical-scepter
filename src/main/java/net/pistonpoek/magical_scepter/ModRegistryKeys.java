package net.pistonpoek.magical_scepter;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.pistonpoek.magical_scepter.item.scepter.Scepter;
import net.pistonpoek.magical_scepter.util.ModIdentifier;

public class ModRegistryKeys {
    public static final RegistryKey<Registry<Scepter>> SCEPTER =
            RegistryKey.ofRegistry(ModIdentifier.of("scepter"));
}

package net.pistonpoek.magicalscepter.registry;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.pistonpoek.magicalscepter.scepter.Scepter;
import net.pistonpoek.magicalscepter.spell.Spell;

public class ModRegistryKeys {
    public static final RegistryKey<Registry<Scepter>> SCEPTER =
            RegistryKey.ofRegistry(ModIdentifier.of("scepter"));
    public static final RegistryKey<Registry<Spell>> SPELL =
            RegistryKey.ofRegistry(ModIdentifier.of("spell"));
}

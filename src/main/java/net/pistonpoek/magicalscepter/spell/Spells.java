package net.pistonpoek.magicalscepter.spell;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;

import java.util.Optional;

public class Spells {
    public static final RegistryKey<Spell> EMPTY_KEY = of("empty");

    public static final RegistryKey<Spell> DEFAULT_KEY = EMPTY_KEY;

    public static final Spell EMPTY = new Spell(0, 0, Optional.empty());

    public static final Spell DEFAULT = EMPTY;


    public static Identifier registerAndGetDefault() {
        return DEFAULT_KEY.getValue();
    }

    private static RegistryKey<Spell> of(String id) {
        return RegistryKey.of(ModRegistryKeys.SPELL, ModIdentifier.of(id));
    }
}

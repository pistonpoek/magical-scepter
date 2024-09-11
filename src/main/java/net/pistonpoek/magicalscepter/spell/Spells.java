package net.pistonpoek.magicalscepter.spell;

import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;

import java.util.Optional;

public class Spells {
    public static final RegistryKey<Spell> MAGICAL_KEY = of("magical");
    public static final RegistryKey<Spell> BLAZE_KEY = of("blaze");
    public static final RegistryKey<Spell> BREEZE_KEY = of("breeze");
    public static final RegistryKey<Spell> DRAGON_KEY = of("dragon");
    public static final RegistryKey<Spell> EVOKER_KEY = of("evoker");
    public static final RegistryKey<Spell> GHAST_KEY = of("ghast");
    public static final RegistryKey<Spell> GUARDIAN_KEY = of("guardian");
    public static final RegistryKey<Spell> SHULKER_KEY = of("shulker");
    public static final RegistryKey<Spell> WARDEN_KEY = of("warden");
    public static final RegistryKey<Spell> WITHER_KEY = of("wither");

    public static final RegistryKey<Spell> DEFAULT_KEY = MAGICAL_KEY;

    private static RegistryKey<Spell> of(String id) {
        return RegistryKey.of(ModRegistryKeys.SPELL, ModIdentifier.of(id));
    }

    // Second spell sound effect
    public static final Spell MAGICAL = create(SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL); // Effect (Resistance)
    public static final Spell BLAZE = create(SoundEvents.ENTITY_BLAZE_SHOOT); // Effect (Fire resistance)
    public static final Spell BREEZE = create(SoundEvents.ENTITY_BREEZE_SHOOT); // SoundEvents.ENTITY_BREEZE_JUMP
    public static final Spell DRAGON = create(SoundEvents.ENTITY_ENDER_DRAGON_SHOOT); // SoundEvents.ENTITY_ENDER_DRAGON_GROWL
    public static final Spell EVOKER = create(SoundEvents.ENTITY_EVOKER_CAST_SPELL); // SoundEvents.ENTITY_EVOKER_CAST_SPELL
    public static final Spell GHAST = create(SoundEvents.ENTITY_GHAST_SHOOT); // Effect (Regeneration)
    public static final Spell GUARDIAN = create(SoundEvents.ENTITY_GUARDIAN_ATTACK); // Effect (Haste)
    public static final Spell SHULKER = create(SoundEvents.ENTITY_SHULKER_SHOOT); // SoundEvents.ENTITY_SHULKER_TELEPORT
    public static final Spell WARDEN = create(SoundEvents.ENTITY_WARDEN_SONIC_BOOM); // Effect (Stability)
    public static final Spell WITHER = create(SoundEvents.ENTITY_WITHER_SHOOT); // Effect (Anti wither)

    private static Spell create(SoundEvent sound) {
        return new Spell(30, 100, Optional.ofNullable(sound).map(Registries.SOUND_EVENT::getEntry));
    }

    private static Spell create(int experienceCost, int cooldown, SoundEvent sound) {
        return new Spell(experienceCost, cooldown, Optional.ofNullable(sound).map(Registries.SOUND_EVENT::getEntry));
    }

    public static final Spell DEFAULT = MAGICAL;

    public static void init() {
    }

}

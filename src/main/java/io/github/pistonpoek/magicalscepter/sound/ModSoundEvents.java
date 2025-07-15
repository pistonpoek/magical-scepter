package io.github.pistonpoek.magicalscepter.sound;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.sound.SoundEvents
 */
public class ModSoundEvents {
    public static final SoundEvent ENTITY_PARROT_IMITATE_REFRACTOR = register("entity.parrot.imitate.refractor");
    public static final SoundEvent ENTITY_REFRACTOR_AMBIENT = register("entity.refractor.ambient");
    public static final SoundEvent ENTITY_REFRACTOR_CELEBRATE = register("entity.refractor.celebrate");
    public static final SoundEvent ENTITY_REFRACTOR_DEATH = register("entity.refractor.death");
    public static final SoundEvent ENTITY_REFRACTOR_HURT = register("entity.refractor.hurt");
    public static final SoundEvent ITEM_ARCANE_SCEPTER_COLLECT_EXPERIENCE = register("item.arcane_scepter.charges");
    public static final SoundEvent ITEM_ARCANE_SCEPTER_RELEASE_EXPERIENCE = register("item.arcane_scepter.discharges");
    public static final SoundEvent ITEM_MAGICAL_SCEPTER_CAST_ATTACK_SPELL = register("item.magical_scepter.cast_attack_spell");
    public static final SoundEvent ITEM_MAGICAL_SCEPTER_CAST_PROTECT_SPELL = register("item.magical_scepter.cast_protect_spell");
    public static final SoundEvent ITEM_MAGICAL_SCEPTER_INFUSE = register("item.magical_scepter.infuse");

    public static void init() {

    }

    private static SoundEvent register(String identifier) {
        return register(ModIdentifier.of(identifier));
    }

    private static RegistryEntry.Reference<SoundEvent> registerReference(String identifier) {
        return registerReference(ModIdentifier.of(identifier));
    }

    private static SoundEvent register(Identifier identifier) {
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

    private static RegistryEntry.Reference<SoundEvent> registerReference(Identifier identifier) {
        return Registry.registerReference(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }
}

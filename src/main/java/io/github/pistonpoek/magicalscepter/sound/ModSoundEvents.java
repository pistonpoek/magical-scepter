package io.github.pistonpoek.magicalscepter.sound;

import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSoundEvents {
    public static final SoundEvent ENTITY_REFRACTOR_AMBIENT = register("entity.refractor.ambient");
    public static final SoundEvent ENTITY_REFRACTOR_CELEBRATE = register("entity.refractor.celebrate");
    public static final SoundEvent ENTITY_REFRACTOR_DEATH = register("entity.refractor.death");
    public static final SoundEvent ENTITY_REFRACTOR_HURT = register("entity.refractor.hurt");
    public static final SoundEvent ITEM_MAGICAL_SCEPTER_CAST_ATTACK_SPELL = register("item.magical_scepter.cast_attack_spell");
    public static final SoundEvent ITEM_MAGICAL_SCEPTER_CAST_PROTECT_SPELL = register("item.magical_scepter.cast_protect_spell");

    public static void init() {

    }

    private static SoundEvent register(String id) {
        return register(ModIdentifier.of(id));
    }

    private static RegistryEntry.Reference<SoundEvent> registerReference(String id) {
        return registerReference(ModIdentifier.of(id));
    }

    private static SoundEvent register(Identifier id) {
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    private static RegistryEntry.Reference<SoundEvent> registerReference(Identifier id) {
        return Registry.registerReference(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
}

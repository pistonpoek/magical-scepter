package io.github.pistonpoek.magicalscepter.world.event;

import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.event.GameEvent;

public class ModGameEvent {
    public static final RegistryEntry.Reference<GameEvent> SPELL_CAST = register("spell_cast");

    public static void init() {

    }

    private static RegistryEntry.Reference<GameEvent> register(String id) {
        return register(id, GameEvent.DEFAULT_RANGE);
    }

    private static RegistryEntry.Reference<GameEvent> register(String id, int range) {
        return Registry.registerReference(Registries.GAME_EVENT, ModIdentifier.of(id), new GameEvent(range));
    }
}

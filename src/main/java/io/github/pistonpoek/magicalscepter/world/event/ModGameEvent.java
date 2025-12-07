package io.github.pistonpoek.magicalscepter.world.event;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.event.GameEvent;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see GameEvent
 */
public class ModGameEvent {
    public static final RegistryEntry.Reference<GameEvent> SPELL_CAST = register("spell_cast");

    /**
     * Initialize the class for the static fields.
     */
    public static void init() {

    }

    /**
     * Register a game event with the default range.
     *
     * @param name String name to register the game event for.
     * @return Registry entry reference for registered game event.
     */
    private static RegistryEntry.Reference<GameEvent> register(String name) {
        return register(name, GameEvent.DEFAULT_RANGE);
    }

    /**
     * Register a game event with a specified range.
     *
     * @param name  String name to register the game event for.
     * @param range Range that the game event is detected at.
     * @return Registry entry reference for registered game event.
     */
    private static RegistryEntry.Reference<GameEvent> register(String name, int range) {
        return Registry.registerReference(Registries.GAME_EVENT, ModIdentifier.of(name), new GameEvent(range));
    }
}

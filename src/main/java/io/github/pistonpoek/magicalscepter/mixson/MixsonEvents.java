package io.github.pistonpoek.magicalscepter.mixson;

import com.google.gson.JsonElement;
import io.github.pistonpoek.magicalscepter.mixson.advancement.adventure.KillAMobMixson;
import io.github.pistonpoek.magicalscepter.mixson.advancement.adventure.KillAllMobsMixson;
import io.github.pistonpoek.magicalscepter.mixson.advancement.nether.AllEffectsMixson;
import io.github.pistonpoek.magicalscepter.mixson.world.BiomeSpawnEntryMixson;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.ramixin.mixson.inline.Mixson;
import net.ramixin.mixson.inline.MixsonEvent;

import java.util.List;

/**
 * Collection of helper methods to register mixson events that alter existing json data.
 */
public class MixsonEvents {
    /**
     * Register a mob modification for the specified mob.
     *
     * @param mobIdentifier Identifier of the mob to register a mob modification for.
     */
    public static void registerMobModification(Identifier mobIdentifier) {
        registerMixsonEvent(List.of(Identifier.of("advancement/adventure/kill_a_mob")),
                ModIdentifier.identifier("kill_a_mob_" + mobIdentifier.getPath()),
                new KillAMobMixson(mobIdentifier));
        registerMixsonEvent(List.of(Identifier.of("advancement/adventure/kill_all_mobs")),
                ModIdentifier.identifier("kill_all_mobs_" + mobIdentifier.getPath()),
                new KillAllMobsMixson(mobIdentifier));
    }

    /**
     * Register an effect modification for the specified effect.
     *
     * @param effectIdentifier Identifier of the effect to register an effect modification for.
     */
    public static void registerEffectModification(Identifier effectIdentifier) {
        registerMixsonEvent(List.of(Identifier.of("advancement/nether/all_effects")),
                ModIdentifier.identifier("all_effects_" + effectIdentifier.getPath()),
                new AllEffectsMixson(effectIdentifier));
    }

    /**
     * Register a monster spawn entry for a biome.
     *
     * @param biome Biome registry key to add a spawn entry for.
     * @param group Spawn group to register spawn entry with.
     * @param weight Weight of the spawn entry to register.
     * @param entry Spawn entry to add in the specified biome.
     */
    public static void registerMonsterSpawnEntry(RegistryKey<Biome> biome, SpawnGroup group,
                                                 int weight, SpawnSettings.SpawnEntry entry) {
        String biomePath = biome.getValue().getPath();
        registerMixsonEvent(List.of(Identifier.of("worldgen/biome/" + biomePath)),
                ModIdentifier.identifier(String.join("_", "biome_spawn_entry",
                        biomePath, entry.type().getUntranslatedName())),
                new BiomeSpawnEntryMixson(group, weight, entry));
    }

    /**
     * Register a mixson event to change json file data.
     *
     * @param resource List of identifier of files to change.
     * @param name Name of the mixson event.
     * @param event Mixson event to specify the change to make in the file data.
     */
    public static void registerMixsonEvent(List<Identifier> resource, String name, MixsonEvent<JsonElement> event) {
        Mixson.registerEvent(Mixson.DEFAULT_PRIORITY, resource::contains, name, event, false);
    }
}

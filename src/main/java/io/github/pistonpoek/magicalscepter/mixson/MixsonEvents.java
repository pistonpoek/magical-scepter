package io.github.pistonpoek.magicalscepter.mixson;

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
import net.ramixin.mixson.inline.events.MixsonEvent;

public class MixsonEvents {
    public static void registerMobModification(Identifier mobIdentifier) {
        registerMixsonEvent("advancement/adventure/kill_a_mob",
                ModIdentifier.id("kill_a_mob_" + mobIdentifier.getPath()),
                new KillAMobMixson(mobIdentifier));
        registerMixsonEvent("advancement/adventure/kill_all_mobs",
                ModIdentifier.id("kill_all_mobs_" + mobIdentifier.getPath()),
                new KillAllMobsMixson(mobIdentifier));
    }

    public static void registerEffectModification(Identifier effectIdentifier) {
        registerMixsonEvent("advancement/nether/all_effects",
                ModIdentifier.id("all_effects_" + effectIdentifier.getPath()),
                new AllEffectsMixson(effectIdentifier));
    }

    public static void registerMonsterSpawnEntry(RegistryKey<Biome> biome, SpawnGroup group, SpawnSettings.SpawnEntry entry) {
        String biomePath = biome.getValue().getPath();
        registerMixsonEvent("worldgen/biome/" + biomePath,
                ModIdentifier.id(String.join("_", "biome_spawn_entry",
                                biomePath, entry.type.getUntranslatedName())),
                new BiomeSpawnEntryMixson(group, entry));
    }

    public static void registerMixsonEvent(String resource, String name, MixsonEvent event) {
        Mixson.registerEvent(Mixson.DEFAULT_PRIORITY, resource, name, event);
    }
}

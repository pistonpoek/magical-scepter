package io.github.pistonpoek.magicalscepter.mixson.world;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.SpawnSettings;
import net.ramixin.mixson.inline.EventContext;
import net.ramixin.mixson.inline.events.MixsonEvent;

public record BiomeSpawnEntryMixson(SpawnGroup spawnGroup,
                                    SpawnSettings.SpawnEntry spawnEntry) implements MixsonEvent {
    @Override
    public void runEvent(EventContext context) {
        JsonObject root = context.getFile().getAsJsonObject();
        JsonObject spawners = root.get("spawners").getAsJsonObject();
        JsonArray spawnEntries = spawners.getAsJsonArray(spawnGroup().getName());
        spawnEntries.add(SpawnSettings.SpawnEntry.CODEC.encodeStart(JsonOps.INSTANCE, spawnEntry()).getOrThrow());
    }
}

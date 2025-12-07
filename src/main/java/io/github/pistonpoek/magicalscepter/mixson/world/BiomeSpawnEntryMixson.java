package io.github.pistonpoek.magicalscepter.mixson.world;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.SpawnSettings;
import net.ramixin.mixson.inline.EventContext;
import net.ramixin.mixson.inline.MixsonEvent;

/**
 * Add a biome spawn entry.
 *
 * @param spawnGroup Spawn group to add in the biome spawn entry.
 * @param weight Weight of the spawn entry to add.
 * @param spawnEntry Spawn entry to add.
 */
public record BiomeSpawnEntryMixson(SpawnGroup spawnGroup, int weight,
                                    SpawnSettings.SpawnEntry spawnEntry) implements MixsonEvent<JsonElement> {
    @Override
    public void runEvent(EventContext<JsonElement> context) {
        JsonObject root = context.getFile().getAsJsonObject();
        JsonObject spawners = root.get("spawners").getAsJsonObject();
        JsonArray spawnEntries = spawners.getAsJsonArray(spawnGroup().getName());
        JsonObject mobEntry = SpawnSettings.SpawnEntry.CODEC.codec()
                .encodeStart(JsonOps.INSTANCE, spawnEntry()).getOrThrow().getAsJsonObject();
        mobEntry.addProperty("weight", weight());
        spawnEntries.add(mobEntry);
    }
}

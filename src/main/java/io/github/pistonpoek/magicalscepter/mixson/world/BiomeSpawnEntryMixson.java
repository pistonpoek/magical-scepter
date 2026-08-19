package io.github.pistonpoek.magicalscepter.mixson.world;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.ramixin.mixson.EventContext;
import net.ramixin.mixson.util.functions.Event;

/**
 * Add a biome spawn entry.
 *
 * @param spawnGroup Spawn group to add in the biome spawn entry.
 * @param weight Weight of the spawn entry to add.
 * @param spawnEntry Spawn entry to add.
 */
public record BiomeSpawnEntryMixson(MobCategory spawnGroup, int weight,
                                    MobSpawnSettings.SpawnerData spawnEntry) implements Event<JsonElement> {
    @Override
    public void runEvent(EventContext<JsonElement> context) {
        JsonObject root = context.getFile().getAsJsonObject();
        JsonObject spawners = root.get("spawners").getAsJsonObject();
        JsonArray spawnEntries = spawners.getAsJsonArray(spawnGroup().getName());
        JsonObject mobEntry = MobSpawnSettings.SpawnerData.CODEC.codec()
                .encodeStart(JsonOps.INSTANCE, spawnEntry()).getOrThrow().getAsJsonObject();
        mobEntry.addProperty("weight", weight());
        spawnEntries.add(mobEntry);
    }
}

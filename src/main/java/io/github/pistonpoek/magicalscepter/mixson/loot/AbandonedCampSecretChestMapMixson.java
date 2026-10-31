package io.github.pistonpoek.magicalscepter.mixson.loot;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.ramixin.mixson.EventContext;
import net.ramixin.mixson.util.functions.Event;

/**
 * Add an explorer map loot entry to the abandoned camp secret chest.
 *
 * @param structure Structure tag to create map item with.
 * @param decoration Map decoration to use on map item.
 * @param item Item to use as base for map item.
 */
public record AbandonedCampSecretChestMapMixson(TagKey<Structure> structure, Holder<MapDecorationType> decoration, Item item) implements Event<JsonElement> {
    private static final String Pools = "pools";
    private static final String Entries = "entries";
    private static final String Modifier = "modifier";
    /**
     * @see net.minecraft.world.level.storage.loot.functions.LootItemFunctions
     */
    private static final String ExplorationLootFunction = "minecraft:exploration_map";

    @Override
    public void runEvent(EventContext<JsonElement> context) {
        JsonArray entries = GetExplorerMapPoolEntries(context.getFile().getAsJsonObject());

        String itemId = BuiltInRegistries.ITEM.getKey(this.item).toString();

        String decorId = decoration.unwrapKey()
                .map(key -> key.identifier().toString())
                .orElseThrow(() -> new IllegalStateException("Decoration missing registered key"));

        String structureTag = "#" + structure.location();

        String jsonBlock = """
            {
              "type": "minecraft:item",
              "name": "%s",
              "modifier": [
                {
                  "type": "minecraft:exploration_map",
                  "decoration": "%s",
                  "destination": "%s"
                },
                {
                  "type": "minecraft:filtered",
                  "item_filter": {
                    "predicates": {
                      "minecraft:map_id": {}
                    }
                  },
                  "on_fail": {
                    "type": "minecraft:discard"
                  }
                }
              ]
            }
            """.formatted(itemId, decorId, structureTag);

        JsonObject newEntry = JsonParser.parseString(jsonBlock).getAsJsonObject();

        entries.add(newEntry);
    }

    /**
     * Get the entries of the loot table pool with the explorer maps.
     *
     * @param root Root of the loot table json object.
     * @return Json array of loot table entries for the pool with explorer maps.
     */
    private JsonArray GetExplorerMapPoolEntries(JsonObject root) {
        if (!root.has(Pools) || !root.get(Pools).isJsonArray()) {
            throw new IllegalStateException("Loot table root missing 'pools' array: " + root);
        }
        JsonArray pools = root.getAsJsonArray(Pools);

        JsonObject explorerMapPool = null;
        for (JsonElement poolElement : pools) {
            JsonObject pool = poolElement.getAsJsonObject();
            if (pool.has(Entries) && pool.get(Entries).isJsonArray()) {
                JsonArray entries = pool.getAsJsonArray(Entries);

                if (isExplorerMapPool(entries)) {
                    explorerMapPool = pool;
                    break;
                }
            }
        }

        if (explorerMapPool == null) {
            throw new IllegalStateException("Could not dynamically locate the explorer map pool inside the loot table!");
        }

        return explorerMapPool.getAsJsonArray(Entries);
    }

    /**
     * Check if the entries in the pool use the explorer map loot function.
     */
    private boolean isExplorerMapPool(JsonArray entries) {
        for (JsonElement entryElement : entries) {
            JsonObject entry = entryElement.getAsJsonObject();
            if (entry.has(Modifier)) {
                JsonElement modifierElement = entry.get(Modifier);

                if (modifierElement.isJsonArray()) {
                    for (JsonElement mod : modifierElement.getAsJsonArray()) {
                        if (mod.isJsonObject() && isExplorationMapModifier(mod.getAsJsonObject())) {
                            return true;
                        }
                    }
                } else if (modifierElement.isJsonObject()) {
                    if (isExplorationMapModifier(modifierElement.getAsJsonObject())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Check that the json modifier object is of exploration map loot function type.
     *
     * @param modifier Modifier json object to check.
     * @return Truth assignment, if modifier object has exploration map loot function type.
     */
    private boolean isExplorationMapModifier(JsonObject modifier) {
        return modifier.has("type") && ExplorationLootFunction.equals(modifier.get("type").getAsString());
    }
}

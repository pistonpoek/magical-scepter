package io.github.pistonpoek.magicalscepter.mixson.advancement.adventure;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.ramixin.mixson.inline.EventContext;
import net.ramixin.mixson.inline.MixsonEvent;

/**
 * Add a mob to the kill all mobs advancement using the specified identifier.
 *
 * @param mobIdentifier Identifier of the mob to add to the advancement.
 */
public record KillAllMobsMixson(Identifier mobIdentifier) implements MixsonEvent<JsonElement> {
    @Override
    public void runEvent(EventContext<JsonElement> context) {
        JsonObject root = context.getFile().getAsJsonObject();
        RegistryEntry<EntityType<?>> mobEntry = Registries.ENTITY_TYPE.getEntry(mobIdentifier).orElseThrow();
        String mobReference = mobEntry.getIdAsString();
        JsonElement mobCondition = JsonParser.parseString(
                """   
                    {
                        "conditions": {
                            "entity": [
                                {
                                    "condition": "minecraft:entity_properties",
                                    "entity": "this",
                                    "predicate": {
                                        "type": "%s"
                                    }
                                }
                            ]
                        },
                        "trigger": "minecraft:player_killed_entity"
                    }
                """.formatted(mobReference)
        );
        root.getAsJsonObject("criteria").add(mobReference, mobCondition);
        JsonArray mobRequirement = new JsonArray();
        mobRequirement.add(mobReference);
        root.getAsJsonArray("requirements").getAsJsonArray().add(mobRequirement);
    }
}

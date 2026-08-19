package io.github.pistonpoek.magicalscepter.mixson.advancement.adventure;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
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
        Holder<EntityType<?>> mobEntry = BuiltInRegistries.ENTITY_TYPE.get(mobIdentifier).orElseThrow();
        String mobReference = mobEntry.getRegisteredName();
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

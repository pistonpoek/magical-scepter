package io.github.pistonpoek.magicalscepter.mixson.advancement.adventure;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resources.Identifier;
import net.ramixin.mixson.EventContext;
import net.ramixin.mixson.util.functions.Event;

/**
 * Add a mob to the kill a mobs advancement using the specified identifier.
 *
 * @param mobIdentifier Identifier of the mob to add to the advancement.
 */
public record KillAMobMixson(Identifier mobIdentifier) implements Event<JsonElement> {
    @Override
    public void runEvent(EventContext<JsonElement> context) {
        JsonObject root = context.getFile().getAsJsonObject();
        String mobReference = mobIdentifier.toString();
        JsonElement mobCondition = JsonParser.parseString(
                """   
                    {
                        "conditions": {
                            "entity": [
                                {
                                    "condition": "minecraft:entity_properties",
                                    "entity": "this",
                                    "predicate": {
                                        "minecraft:entity_type": "%s"
                                    }
                                }
                            ]
                        },
                        "trigger": "minecraft:player_killed_entity"
                    }
                """.formatted(mobReference)
        );
        root.getAsJsonObject("criteria").add(mobReference, mobCondition);
        root.getAsJsonArray("requirements").get(0).getAsJsonArray().add(mobReference);
    }
}

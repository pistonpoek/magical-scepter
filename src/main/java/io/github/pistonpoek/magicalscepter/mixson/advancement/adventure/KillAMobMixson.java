package io.github.pistonpoek.magicalscepter.mixson.advancement.adventure;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.util.Identifier;
import net.ramixin.mixson.inline.EventContext;
import net.ramixin.mixson.inline.MixsonEvent;

public record KillAMobMixson(Identifier mobIdentifier) implements MixsonEvent<JsonElement> {
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
        root.getAsJsonArray("requirements").get(0).getAsJsonArray().add(mobReference);
    }
}

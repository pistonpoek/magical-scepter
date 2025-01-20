package io.github.pistonpoek.magicalscepter.mixson.advancement.adventure;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.util.Identifier;
import net.ramixin.mixson.inline.EventContext;
import net.ramixin.mixson.inline.events.MixsonEvent;

public record KillAMobMixson(Identifier mobIdentifier) implements MixsonEvent {
    @Override
    public void runEvent(EventContext context) {
        JsonObject root = context.getFile().getAsJsonObject();
        String mobReference = mobIdentifier.toString();
        JsonElement mobCondition = JsonParser.parseString(
                "{" +
                    "\"conditions\": {" +
                        "\"entity\": [" +
                            "{" +
                                "\"condition\": \"minecraft:entity_properties\"," +
                                "\"entity\": \"this\"," +
                                "\"predicate\": {" +
                                    "\"type\": \"" + mobReference + "\"" +
                                "}" +
                            "}" +
                        "]" +
                    "}," +
                    "\"trigger\": \"minecraft:player_killed_entity\"" +
                "}"
        );
        root.getAsJsonObject("criteria").add(mobReference, mobCondition);
        root.getAsJsonArray("requirements").get(0).getAsJsonArray().add(mobReference);
    }
}

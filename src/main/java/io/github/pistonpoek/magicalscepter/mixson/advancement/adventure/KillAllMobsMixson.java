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
import net.ramixin.mixson.inline.events.MixsonEvent;

public record KillAllMobsMixson(Identifier mobIdentifier) implements MixsonEvent {
    @Override
    public void runEvent(EventContext context) {
        JsonObject root = context.getFile().getAsJsonObject();
        RegistryEntry<EntityType<?>> mobEntry = Registries.ENTITY_TYPE.getEntry(mobIdentifier).orElseThrow();
        String mobReference = mobEntry.getIdAsString();
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
        JsonArray mobRequirement = new JsonArray();
        mobRequirement.add(mobReference);
        root.getAsJsonArray("requirements").getAsJsonArray().add(mobRequirement);
    }
}

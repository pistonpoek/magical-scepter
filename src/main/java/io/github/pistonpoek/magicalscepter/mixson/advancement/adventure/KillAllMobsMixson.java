package io.github.pistonpoek.magicalscepter.mixson.advancement.adventure;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.mixson.MixsonModification;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public record KillAllMobsMixson(Identifier mobIdentifier) implements MixsonModification {
    public static Identifier EVENT_ID = ModIdentifier.of("kill_all_mobs");
    public static String FILE_PATH = "advancement/adventure/kill_all_mobs";

    @Override
    public JsonElement run(JsonElement jsonElement) {
        JsonObject root = jsonElement.getAsJsonObject();
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
        return root;
    }

    @Override
    public Identifier getEventIdentifier() {
        return EVENT_ID;
    }

    @Override
    public Identifier getResourceIdentifier() {
        return Identifier.ofVanilla(FILE_PATH);
    }
}

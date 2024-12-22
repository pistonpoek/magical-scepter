package io.github.pistonpoek.magicalscepter.mixson.advancement.adventure;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.mixson.MixsonModification;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.minecraft.util.Identifier;

public record KillAMobMixson(Identifier mobIdentifier) implements MixsonModification {
    public static Identifier EVENT_ID = ModIdentifier.of("kill_a_mob");
    public static String FILE_PATH = "advancement/adventure/kill_a_mob";

    @Override
    public JsonElement run(JsonElement jsonElement) {
        JsonObject root = jsonElement.getAsJsonObject();
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

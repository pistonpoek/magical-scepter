package io.github.pistonpoek.magicalscepter.mixson.advancement.nether;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.pistonpoek.magicalscepter.mixson.MixsonModification;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.minecraft.util.Identifier;

public record AllEffectsMixson(Identifier effectIdentifier) implements MixsonModification {
    public static Identifier EVENT_ID = ModIdentifier.of("all_effects");
    public static String FILE_PATH = "advancement/nether/all_effects";

    @Override
    public JsonElement run(JsonElement jsonElement) {
        JsonObject root = jsonElement.getAsJsonObject();
        String effectReference = effectIdentifier.toString();

        root.getAsJsonObject("criteria").getAsJsonObject("all_effects")
                .getAsJsonObject("conditions").getAsJsonObject("effects")
                .add(effectReference, new JsonObject());
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

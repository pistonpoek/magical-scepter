package io.github.pistonpoek.magicalscepter.mixson.advancement.nether;

import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;
import net.ramixin.mixson.inline.EventContext;
import net.ramixin.mixson.inline.events.MixsonEvent;

public record AllEffectsMixson(Identifier effectIdentifier) implements MixsonEvent {
    @Override
    public void runEvent(EventContext context) {
        JsonObject root = context.getFile().getAsJsonObject();
        String effectReference = effectIdentifier.toString();

        root.getAsJsonObject("criteria").getAsJsonObject("all_effects")
                .getAsJsonObject("conditions").getAsJsonObject("effects")
                .add(effectReference, new JsonObject());
    }
}

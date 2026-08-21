package io.github.pistonpoek.magicalscepter.mixson.advancement.adventure;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
        JsonObject mobCriterion = MobCriterion.create(mobIdentifier);
        root.getAsJsonObject("criteria").add(mobReference, mobCriterion);
        root.getAsJsonArray("requirements").get(0).getAsJsonArray().add(mobReference);
    }
}

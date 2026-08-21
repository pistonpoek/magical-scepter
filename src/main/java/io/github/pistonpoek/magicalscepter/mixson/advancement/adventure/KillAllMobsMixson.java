package io.github.pistonpoek.magicalscepter.mixson.advancement.adventure;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.ramixin.mixson.EventContext;
import net.ramixin.mixson.util.functions.Event;

/**
 * Add a mob to the kill all mobs advancement using the specified identifier.
 *
 * @param mobIdentifier Identifier of the mob to add to the advancement.
 */
public record KillAllMobsMixson(Identifier mobIdentifier) implements Event<JsonElement> {
    @Override
    public void runEvent(EventContext<JsonElement> context) {
        JsonObject root = context.getFile().getAsJsonObject();
        Holder<EntityType<?>> mobEntry = BuiltInRegistries.ENTITY_TYPE.get(mobIdentifier).orElseThrow();
        String mobReference = mobEntry.getRegisteredName();
        JsonObject mobCriterion = MobCriterion.create(mobIdentifier);
        root.getAsJsonObject("criteria").add(mobReference, mobCriterion);
        JsonArray mobRequirement = new JsonArray();
        mobRequirement.add(mobReference);
        root.getAsJsonArray("requirements").add(mobRequirement);
    }
}

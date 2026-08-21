package io.github.pistonpoek.magicalscepter.mixson.advancement.adventure;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resources.Identifier;

/**
 * Mob criterion class for mixson util.
 *
 * @see net.minecraft.advancements.triggers.Criterion
 * @see net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition
 */
public class MobCriterion {
    /**
     * Create a mob criterion for killing the specified mob.
     *
     * @param mob Identifier of mob to kill for criterion.
     * @return JsonObject that is a serialized criterion.
     */
    public static JsonObject create(Identifier mob) {
        String mobReference = mob.toString();
        return JsonParser.parseString(
                """   
                    {
                        "conditions": {
                            "entity" : {
                                "type" : "minecraft:entity_properties",
                                "entity" : "this",
                                "predicate" : {
                                    "minecraft:entity_type" : "%s"
                                }
                            }
                        },
                        "trigger": "minecraft:player_killed_entity"
                    }
                """.formatted(mobReference)
        ).getAsJsonObject();
    }
}

package io.github.pistonpoek.magicalscepter.mixson;

import io.github.pistonpoek.magicalscepter.mixson.advancement.adventure.KillAMobMixson;
import io.github.pistonpoek.magicalscepter.mixson.advancement.adventure.KillAllMobsMixson;
import io.github.pistonpoek.magicalscepter.mixson.advancement.nether.AllEffectsMixson;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.minecraft.util.Identifier;
import net.ramixin.mixson.inline.Mixson;
import net.ramixin.mixson.inline.events.MixsonEvent;

public class MixsonEvents {
    public static void registerMobModification(Identifier mobIdentifier) {
        registerMixsonEvent("advancement/adventure/kill_a_mob",
                ModIdentifier.name("kill_a_mob"), new KillAMobMixson(mobIdentifier));
        registerMixsonEvent("advancement/adventure/kill_all_mobs",
                ModIdentifier.name("kill_all_mobs"), new KillAllMobsMixson(mobIdentifier));
    }

    public static void registerEffectModification(Identifier effectIdentifier) {
        registerMixsonEvent("advancement/nether/all_effects",
                ModIdentifier.name("all_effects"),
                new AllEffectsMixson(effectIdentifier));
    }

    public static void registerMixsonEvent(String resource, String name, MixsonEvent event) {
        Mixson.registerEvent(Mixson.DEFAULT_PRIORITY, resource, name, event);
    }
}

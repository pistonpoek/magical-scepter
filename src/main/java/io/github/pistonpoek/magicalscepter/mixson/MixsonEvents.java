package io.github.pistonpoek.magicalscepter.mixson;

import io.github.pistonpoek.magicalscepter.mixson.advancement.adventure.KillAMobMixson;
import io.github.pistonpoek.magicalscepter.mixson.advancement.adventure.KillAllMobsMixson;
import io.github.pistonpoek.magicalscepter.mixson.advancement.nether.AllEffectsMixson;
import net.minecraft.util.Identifier;
import net.ramixin.mixson.Mixson;

public class MixsonEvents {

    public static void init() {
    }

    public static void registerMobModification(Identifier mobIdentifier) {
        registerMixsonModification(new KillAMobMixson(mobIdentifier));
        registerMixsonModification(new KillAllMobsMixson(mobIdentifier));
    }

    public static void registerEffectModification(Identifier effectIdentifier) {
        registerMixsonModification(new AllEffectsMixson(effectIdentifier));
    }

    public static void registerMixsonModification(MixsonModification modification) {
        Mixson.registerModificationEvent(modification.getResourceIdentifier(),
                modification.getEventIdentifier(), modification);
    }
}

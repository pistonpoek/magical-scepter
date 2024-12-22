package io.github.pistonpoek.magicalscepter.mixson;

import net.minecraft.util.Identifier;
import net.ramixin.mixson.events.ModificationEvent;

public interface MixsonModification extends ModificationEvent {
    Identifier getEventIdentifier();
    Identifier getResourceIdentifier();
}

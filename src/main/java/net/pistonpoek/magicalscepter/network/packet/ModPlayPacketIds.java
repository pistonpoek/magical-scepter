package net.pistonpoek.magicalscepter.network.packet;

import net.minecraft.util.Identifier;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;

public abstract class ModPlayPacketIds {
    static Identifier ATTACK_ITEM_PACKET_ID = ModIdentifier.of("attack_item");
    static Identifier SWING_HAND_PACKET_ID = ModIdentifier.of("swing_type");
}

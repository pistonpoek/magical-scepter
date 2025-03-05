package io.github.pistonpoek.magicalscepter.item;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;

/**
 * Specifies different hand swing animation types.
 */
public enum SwingType {
    HIT,
    PROTECT;

    public static final PacketCodec<PacketByteBuf, SwingType> PACKET_CODEC = PacketCodec.of(
            (value, buf) -> buf.writeVarInt(value.ordinal()),
            buf -> SwingType.values()[buf.readVarInt()]
    );
}

package io.github.pistonpoek.magicalscepter.item;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * Specifies different hand swing animation types.
 */
public enum SwingType {
    HIT,
    PROTECT;

    public static final StreamCodec<FriendlyByteBuf, SwingType> PACKET_CODEC = StreamCodec.ofMember(
            (value, buf) -> buf.writeVarInt(value.ordinal()),
            buf -> SwingType.values()[buf.readVarInt()]
    );
}

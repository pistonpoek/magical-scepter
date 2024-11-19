package net.pistonpoek.magicalscepter.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Hand;
import net.pistonpoek.magicalscepter.item.SwingType;
import net.pistonpoek.magicalscepter.util.LivingEntityHand;

public record SwingHandPayload(int id, Hand hand, SwingType swingType) implements CustomPayload {
    public static final CustomPayload.Id<SwingHandPayload> ID =
            new CustomPayload.Id<>(ModPlayPacketIds.SWING_HAND_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, SwingHandPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, SwingHandPayload::id,
            LivingEntityHand.PACKET_CODEC, SwingHandPayload::hand,
            SwingType.PACKET_CODEC, SwingHandPayload::swingType,
            SwingHandPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

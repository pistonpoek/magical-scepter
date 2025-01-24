package io.github.pistonpoek.magicalscepter.network.packet;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Hand;
import io.github.pistonpoek.magicalscepter.item.SwingType;
import io.github.pistonpoek.magicalscepter.util.LivingEntityHand;

public record SwingHandPayload(int id, Hand hand, SwingType swingType) implements CustomPayload {
    public static final CustomPayload.Id<SwingHandPayload> ID =
            new CustomPayload.Id<>(ModIdentifier.of("swing_type"));
    public static final PacketCodec<RegistryByteBuf, SwingHandPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, SwingHandPayload::id,
            LivingEntityHand.PACKET_CODEC, SwingHandPayload::hand,
            SwingType.PACKET_CODEC, SwingHandPayload::swingType,
            SwingHandPayload::new
    );

    @Override
    public Id<SwingHandPayload> getId() {
        return ID;
    }
}

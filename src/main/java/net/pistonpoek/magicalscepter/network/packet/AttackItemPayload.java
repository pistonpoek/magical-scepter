package net.pistonpoek.magicalscepter.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record AttackItemPayload(float yaw, float pitch) implements CustomPayload {
    public static final CustomPayload.Id<AttackItemPayload> ID =
            new CustomPayload.Id<>(ModPlayPacketIds.ATTACK_ITEM_PACKET_ID);

    public static final PacketCodec<RegistryByteBuf, AttackItemPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.FLOAT, AttackItemPayload::yaw,
            PacketCodecs.FLOAT, AttackItemPayload::pitch,
            AttackItemPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

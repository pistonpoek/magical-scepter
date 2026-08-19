package io.github.pistonpoek.magicalscepter.network.packet;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Payload to try to attack with an item with the specified yaw and pitch.
 *
 * @param yaw Float yaw angle of the player during the attack.
 * @param pitch Float pitch angle of the player during the attack.
 */
public record AttackItemPayload(float yaw, float pitch) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AttackItemPayload> ID =
            new CustomPacketPayload.Type<>(ModIdentifier.of("attack_item"));

    public static final StreamCodec<RegistryFriendlyByteBuf, AttackItemPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, AttackItemPayload::yaw,
            ByteBufCodecs.FLOAT, AttackItemPayload::pitch,
            AttackItemPayload::new
    );

    @Override
    public Type<AttackItemPayload> type() {
        return ID;
    }
}

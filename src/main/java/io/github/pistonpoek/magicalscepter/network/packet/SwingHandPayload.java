package io.github.pistonpoek.magicalscepter.network.packet;

import io.github.pistonpoek.magicalscepter.item.SwingType;
import io.github.pistonpoek.magicalscepter.util.LivingEntityHand;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;

/**
 * Payload to swing the specified hand with specified swing type.
 *
 * @param identifier Integer network identifier of the entity to swing hand for.
 * @param hand Hand of the entity to swing.
 * @param swingType Swing type to use when swinging the hand.
 */
public record SwingHandPayload(int identifier, InteractionHand hand, SwingType swingType) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SwingHandPayload> ID =
            new CustomPacketPayload.Type<>(ModIdentifier.of("swing_type"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SwingHandPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SwingHandPayload::identifier,
            LivingEntityHand.PACKET_CODEC, SwingHandPayload::hand,
            SwingType.PACKET_CODEC, SwingHandPayload::swingType,
            SwingHandPayload::new
    );

    @Override
    public Type<SwingHandPayload> type() {
        return ID;
    }
}

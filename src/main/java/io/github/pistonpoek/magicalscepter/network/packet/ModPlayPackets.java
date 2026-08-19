package io.github.pistonpoek.magicalscepter.network.packet;

import io.github.pistonpoek.magicalscepter.network.handler.AttackItemHandler;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.network.protocol.game.GamePacketTypes
 */
public class ModPlayPackets {
    public static final CustomPacketPayload.TypeAndCodec<?, AttackItemPayload> ATTACK_ITEM =
            registerClientToServerPayload(AttackItemPayload.ID, AttackItemPayload.CODEC);
    public static final CustomPacketPayload.TypeAndCodec<?, SwingHandPayload> SWING_HAND =
            registerServerToClientPayload(SwingHandPayload.ID, SwingHandPayload.CODEC);

    /**
     * Initialize the class for the static fields.
     */
    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(ATTACK_ITEM.type(), new AttackItemHandler());
    }

    /**
     * Register a server to client payload.
     *
     * @param identifier Identifier of the custom payload to register.
     * @param codec Codec of the custom payload to register.
     * @return Custom payload type that is being registered.
     * @param <T> Type of the payload to register.
     */
    private static <T extends CustomPacketPayload> CustomPacketPayload.TypeAndCodec<? super RegistryFriendlyByteBuf, T>
    registerServerToClientPayload(CustomPacketPayload.Type<T> identifier, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        return PayloadTypeRegistry.playS2C().register(identifier, codec);
    }

    /**
     * Register a client to server payload.
     *
     * @param identifier Identifier of the custom payload to register.
     * @param codec Codec of the custom payload to register.
     * @return Custom payload type that is being registered.
     * @param <T> Type of the payload to register.
     */
    private static <T extends CustomPacketPayload> CustomPacketPayload.TypeAndCodec<? super RegistryFriendlyByteBuf, T>
    registerClientToServerPayload(CustomPacketPayload.Type<T> identifier, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        return PayloadTypeRegistry.playC2S().register(identifier, codec);
    }

}

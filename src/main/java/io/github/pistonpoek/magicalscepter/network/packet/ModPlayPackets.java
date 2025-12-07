package io.github.pistonpoek.magicalscepter.network.packet;

import io.github.pistonpoek.magicalscepter.network.handler.AttackItemHandler;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.network.packet.PlayPackets
 */
public class ModPlayPackets {
    public static final CustomPayload.Type<?, AttackItemPayload> ATTACK_ITEM =
            registerClientToServerPayload(AttackItemPayload.ID, AttackItemPayload.CODEC);
    public static final CustomPayload.Type<?, SwingHandPayload> SWING_HAND =
            registerServerToClientPayload(SwingHandPayload.ID, SwingHandPayload.CODEC);

    /**
     * Initialize the class for the static fields.
     */
    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(ATTACK_ITEM.id(), new AttackItemHandler());
    }

    /**
     * Register a server to client payload.
     *
     * @param identifier Identifier of the custom payload to register.
     * @param codec Codec of the custom payload to register.
     * @return Custom payload type that is being registered.
     * @param <T> Type of the payload to register.
     */
    private static <T extends CustomPayload> CustomPayload.Type<? super RegistryByteBuf, T>
    registerServerToClientPayload(CustomPayload.Id<T> identifier, PacketCodec<RegistryByteBuf, T> codec) {
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
    private static <T extends CustomPayload> CustomPayload.Type<? super RegistryByteBuf, T>
    registerClientToServerPayload(CustomPayload.Id<T> identifier, PacketCodec<RegistryByteBuf, T> codec) {
        return PayloadTypeRegistry.playC2S().register(identifier, codec);
    }

}

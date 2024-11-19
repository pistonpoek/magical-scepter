package net.pistonpoek.magicalscepter.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.pistonpoek.magicalscepter.network.handler.AttackItemHandler;
import net.pistonpoek.magicalscepter.network.packet.AttackItemPayload;
import net.pistonpoek.magicalscepter.network.packet.SwingHandPayload;

public class ModPlayPackets {
    public static final CustomPayload.Type<?, AttackItemPayload> ATTACK_ITEM =
            registerClientToServerPayload(AttackItemPayload.ID, AttackItemPayload.CODEC);
    public static final CustomPayload.Type<?, SwingHandPayload> SWING_HAND =
            registerServerToClientPayload(SwingHandPayload.ID, SwingHandPayload.CODEC);

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(ATTACK_ITEM.id(), new AttackItemHandler());
    }

    private static <T  extends CustomPayload> CustomPayload.Type<? super RegistryByteBuf, T>
    registerServerToClientPayload(CustomPayload.Id<T> id, PacketCodec<RegistryByteBuf, T> codec) {
        return PayloadTypeRegistry.playS2C().register(id, codec);
    }

    private static <T  extends CustomPayload> CustomPayload.Type<? super RegistryByteBuf, T>
    registerClientToServerPayload(CustomPayload.Id<T> id, PacketCodec<RegistryByteBuf, T> codec) {
        return PayloadTypeRegistry.playC2S().register(id, codec);
    }

}

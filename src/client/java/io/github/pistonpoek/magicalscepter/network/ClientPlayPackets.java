package io.github.pistonpoek.magicalscepter.network;

import io.github.pistonpoek.magicalscepter.network.handler.SwingHandHandler;
import io.github.pistonpoek.magicalscepter.network.packet.ModPlayPackets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

/**
 * TODO
 */
@Environment(EnvType.CLIENT)
public class ClientPlayPackets {
    /**
     * Register mod play packets.
     */
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(ModPlayPackets.SWING_HAND.id(), new SwingHandHandler());
    }
}

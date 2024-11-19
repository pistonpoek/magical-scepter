package net.pistonpoek.magicalscepter.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.pistonpoek.magicalscepter.network.handler.SwingHandHandler;

public class ModClientPlayPackets {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(ModPlayPackets.SWING_HAND.id(), new SwingHandHandler());
    }
}

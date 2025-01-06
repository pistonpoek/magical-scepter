package io.github.pistonpoek.magicalscepter.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import io.github.pistonpoek.magicalscepter.network.handler.SwingHandHandler;

@Environment(EnvType.CLIENT)
public class ModClientPlayPackets {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(ModPlayPackets.SWING_HAND.id(), new SwingHandHandler());
    }
}

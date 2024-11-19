package net.pistonpoek.magicalscepter.network.handler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.pistonpoek.magicalscepter.item.SwingHandLivingEntity;
import net.pistonpoek.magicalscepter.network.packet.SwingHandPayload;

@Environment(EnvType.CLIENT)
public class SwingHandHandler implements ClientPlayNetworking.PlayPayloadHandler<SwingHandPayload> {
    @Override
    public void receive(SwingHandPayload payload, ClientPlayNetworking.Context context) {
        Entity entity = context.player().getEntityWorld().getEntityById(payload.id());
        if (entity instanceof SwingHandLivingEntity) {
            ((SwingHandLivingEntity) entity).swingHand(payload.hand(), payload.swingType());
        }
    }

}

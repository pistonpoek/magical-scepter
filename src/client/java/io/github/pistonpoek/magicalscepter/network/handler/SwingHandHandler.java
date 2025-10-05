package io.github.pistonpoek.magicalscepter.network.handler;

import io.github.pistonpoek.magicalscepter.item.SwingHandLivingEntity;
import io.github.pistonpoek.magicalscepter.network.packet.SwingHandPayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;

@Environment(EnvType.CLIENT)
public class SwingHandHandler implements ClientPlayNetworking.PlayPayloadHandler<SwingHandPayload> {
    @Override
    public void receive(SwingHandPayload payload, ClientPlayNetworking.Context context) {
        Entity entity = context.player().getWorld().getEntityById(payload.identifier());
        if (entity instanceof SwingHandLivingEntity swingHandLivingEntity) {
            swingHandLivingEntity.magical_scepter$swingHand(payload.hand(), payload.swingType());
        }
    }

}

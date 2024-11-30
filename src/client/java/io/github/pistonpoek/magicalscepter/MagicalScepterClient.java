package io.github.pistonpoek.magicalscepter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import io.github.pistonpoek.magicalscepter.attack.ItemAttackCallback;
import io.github.pistonpoek.magicalscepter.entity.ModEntityType;
import io.github.pistonpoek.magicalscepter.entity.ModEntityModelLayers;
import io.github.pistonpoek.magicalscepter.entity.RefractorEntityRenderer;
import io.github.pistonpoek.magicalscepter.network.ModClientPlayPackets;

public class MagicalScepterClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModClientPlayPackets.init();

		EntityRendererRegistry.register(ModEntityType.REFRACTOR, RefractorEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.REFRACTOR,
				IllagerEntityModel::getTexturedModelData);
		ClientPreAttackCallback.EVENT.register(new ItemAttackCallback());
	}
}
package io.github.pistonpoek.magicalscepter;

import io.github.pistonpoek.magicalscepter.render.entity.GuardianBoltEntityRenderer;
import io.github.pistonpoek.magicalscepter.render.entity.model.GuardianBoltEntityModel;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import io.github.pistonpoek.magicalscepter.attack.ItemAttackCallback;
import io.github.pistonpoek.magicalscepter.entity.ModEntityType;
import io.github.pistonpoek.magicalscepter.render.entity.model.ModEntityModelLayers;
import io.github.pistonpoek.magicalscepter.render.entity.RefractorEntityRenderer;
import io.github.pistonpoek.magicalscepter.network.ModClientPlayPackets;

@Environment(EnvType.CLIENT)
public class MagicalScepterClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModClientPlayPackets.init();

		EntityRendererRegistry.register(ModEntityType.REFRACTOR, RefractorEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.REFRACTOR,
				IllagerEntityModel::getTexturedModelData);

		EntityRendererRegistry.register(ModEntityType.GUARDIAN_BOLT, GuardianBoltEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.GUARDIAN_BOLT,
				GuardianBoltEntityModel::getTexturedModelData);

		ClientPreAttackCallback.EVENT.register(new ItemAttackCallback());
	}
}
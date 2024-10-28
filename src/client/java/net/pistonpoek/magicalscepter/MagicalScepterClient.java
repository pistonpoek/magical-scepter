package net.pistonpoek.magicalscepter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import net.pistonpoek.magicalscepter.entity.ModEntityType;
import net.pistonpoek.magicalscepter.entity.ModEntityModelLayers;
import net.pistonpoek.magicalscepter.entity.RefractorEntityRenderer;
import net.pistonpoek.magicalscepter.item.ModItems;

public class MagicalScepterClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 :
				ScepterContentsComponent.getColor(stack),
				ModItems.SCEPTER);

		EntityRendererRegistry.register(ModEntityType.REFRACTOR, RefractorEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.REFRACTOR, IllagerEntityModel::getTexturedModelData);
	}
}
package net.pistonpoek.magicalscepter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.util.math.ColorHelper;
import net.pistonpoek.magicalscepter.item.ModItems;
import net.pistonpoek.magicalscepter.scepter.ScepterHelper;

public class MagicalScepterClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ?
				-1 : ColorHelper.Argb.fullAlpha(ScepterHelper.getScepter(stack).getColor()), ModItems.SCEPTER);
	}
}
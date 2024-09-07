package net.pistonpoek.magical_scepter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.util.math.ColorHelper;
import net.pistonpoek.magical_scepter.item.ModItems;
import net.pistonpoek.magical_scepter.item.scepter.ScepterHelper;
import net.pistonpoek.magical_scepter.item.scepter.ScepterUtil;

public class MagicalScepterClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ?
				-1 : ColorHelper.Argb.fullAlpha(ScepterHelper.getScepter(stack).getColor()), ModItems.SCEPTER);
	}
}
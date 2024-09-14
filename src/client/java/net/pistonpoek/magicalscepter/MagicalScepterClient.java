package net.pistonpoek.magicalscepter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ColorHelper;
import net.pistonpoek.magicalscepter.item.ModItems;
import net.pistonpoek.magicalscepter.scepter.ScepterHelper;

import java.util.Optional;

public class MagicalScepterClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 :
				ScepterHelper.getColor(stack),
				ModItems.SCEPTER);
	}
}
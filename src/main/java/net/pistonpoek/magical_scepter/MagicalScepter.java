package net.pistonpoek.magical_scepter;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.item.Items;
import net.pistonpoek.magical_scepter.item.ModItems;
import net.pistonpoek.magical_scepter.item.scepter.ScepterContentsComponent;
import net.pistonpoek.magical_scepter.item.scepter.Scepters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.pistonpoek.magical_scepter.item.ModItems.registerModItems;
import static net.pistonpoek.magical_scepter.util.ModIdentifier.MOD_ID;

public class MagicalScepter implements ModInitializer {
	/**
	 * Logger for this mod.
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	// https://docs.fabricmc.net/develop/items/custom-data-components


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		registerModItems();
		ModDataComponentTypes.init();
		LOGGER.info("Ready for some magic?!");
	}

}
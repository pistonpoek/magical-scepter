package net.pistonpoek.magicalscepter;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import net.pistonpoek.magicalscepter.item.ModItems;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.pistonpoek.magicalscepter.scepter.Scepter;
import net.pistonpoek.magicalscepter.scepter.Scepters;
import net.pistonpoek.magicalscepter.spell.Spell;
import net.pistonpoek.magicalscepter.spell.Spells;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MagicalScepter implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger(ModIdentifier.MOD_ID);

	@Override
	public void onInitialize() {
		Scepters.registerAndGetDefault();
		Spells.registerAndGetDefault();
		ModItems.registerModItems();
		ModDataComponentTypes.init();
		DynamicRegistries.registerSynced(ModRegistryKeys.SCEPTER, Scepter.CODEC);
		DynamicRegistries.registerSynced(ModRegistryKeys.SPELL, Spell.CODEC);
	}
}
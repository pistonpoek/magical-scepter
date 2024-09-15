package net.pistonpoek.magicalscepter;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import net.pistonpoek.magicalscepter.entity.effect.ModStatusEffects;
import net.pistonpoek.magicalscepter.item.ModItems;
import net.pistonpoek.magicalscepter.recipe.ModRecipeSerializer;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.registry.ModRegistries;
import net.pistonpoek.magicalscepter.scepter.ScepterInfusion;
import net.pistonpoek.magicalscepter.spell.Spell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MagicalScepter implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger(ModIdentifier.MOD_ID);

	@Override
	public void onInitialize() {
		ModRegistries.init();
		ModItems.init();
		ModRecipeSerializer.init();
		ModStatusEffects.init();
		ModDataComponentTypes.init();
		ServerLivingEntityEvents.AFTER_DEATH.register(Spell.Cast::afterDeath);
		ServerLivingEntityEvents.AFTER_DAMAGE.register(ScepterInfusion::afterDamage);
	}
}
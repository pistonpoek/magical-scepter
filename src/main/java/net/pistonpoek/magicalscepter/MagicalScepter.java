package net.pistonpoek.magicalscepter;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.timer.TimerCallbackSerializer;
import net.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import net.pistonpoek.magicalscepter.entity.ModEntityType;
import net.pistonpoek.magicalscepter.entity.effect.ModStatusEffects;
import net.pistonpoek.magicalscepter.entity.mob.RefractorEntity;
import net.pistonpoek.magicalscepter.item.ModItems;
import net.pistonpoek.magicalscepter.loot.function.ModLootFunctionTypes;
import net.pistonpoek.magicalscepter.recipe.ModRecipeSerializer;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.registry.ModRegistries;
import net.pistonpoek.magicalscepter.scepter.ScepterInfusion;
import net.pistonpoek.magicalscepter.spell.cast.delay.SpellCastScheduler;
import net.pistonpoek.magicalscepter.spell.cast.delay.SpellCastTimerCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MagicalScepter implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger(ModIdentifier.MOD_NAME);

	@Override
	public void onInitialize() {
		ModRegistries.init();
		ModItems.init();
		ModRecipeSerializer.init();
		ModStatusEffects.init();
		ModDataComponentTypes.init();
		ModEntityType.init();
		ModLootFunctionTypes.init();

		ServerLivingEntityEvents.AFTER_DEATH.register(SpellCastScheduler::afterDeath);
		ServerLivingEntityEvents.AFTER_DAMAGE.register(ScepterInfusion::afterDamage);

		TimerCallbackSerializer.INSTANCE.registerSerializer(new SpellCastTimerCallback.Serializer());
		FabricDefaultAttributeRegistry.register(ModEntityType.REFRACTOR, RefractorEntity.createRefractorAttributes());
	}
}
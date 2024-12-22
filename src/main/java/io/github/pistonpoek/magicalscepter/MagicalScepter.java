package io.github.pistonpoek.magicalscepter;

import io.github.pistonpoek.magicalscepter.command.SpellCommand;
import io.github.pistonpoek.magicalscepter.mixson.MixsonEvents;
import io.github.pistonpoek.magicalscepter.sound.ModSoundEvents;
import io.github.pistonpoek.magicalscepter.world.event.ModGameEvent;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.timer.TimerCallbackSerializer;
import io.github.pistonpoek.magicalscepter.advancement.criteria.ModCriteria;
import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import io.github.pistonpoek.magicalscepter.entity.ModEntityType;
import io.github.pistonpoek.magicalscepter.entity.effect.ModStatusEffects;
import io.github.pistonpoek.magicalscepter.entity.mob.RefractorEntity;
import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.loot.function.ModLootFunctionTypes;
import io.github.pistonpoek.magicalscepter.network.ModPlayPackets;
import io.github.pistonpoek.magicalscepter.recipe.ModRecipeSerializer;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;
import io.github.pistonpoek.magicalscepter.registry.ModRegistries;
import io.github.pistonpoek.magicalscepter.scepter.ScepterInfusion;
import io.github.pistonpoek.magicalscepter.spell.cast.delay.SpellCastScheduler;
import io.github.pistonpoek.magicalscepter.spell.cast.delay.SpellCastTimerCallback;
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
		ModPlayPackets.init();
		ModCriteria.init();
		ModGameEvent.init();
		ModSoundEvents.init();
		MixsonEvents.init();

		ServerLivingEntityEvents.AFTER_DEATH.register(SpellCastScheduler::afterDeath);
		ServerLivingEntityEvents.AFTER_DAMAGE.register(ScepterInfusion::afterDamage);
		CommandRegistrationCallback.EVENT.register(SpellCommand::register);

		TimerCallbackSerializer.INSTANCE.registerSerializer(new SpellCastTimerCallback.Serializer());
		FabricDefaultAttributeRegistry.register(ModEntityType.REFRACTOR, RefractorEntity.createRefractorAttributes());
	}
}
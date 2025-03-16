package io.github.pistonpoek.magicalscepter.datagen.tag;

import io.github.pistonpoek.magicalscepter.world.event.ModGameEvent;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.GameEventTags;
import net.minecraft.world.event.GameEvent;

import java.util.concurrent.CompletableFuture;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.data.tag.vanilla.VanillaGameEventTagProvider
 */
public class ModGameEventTagProvider extends FabricTagProvider<GameEvent> {
    /**
     * Construct a mod game event tag provider for data generation.
     *
     * @param output Data output to generate game event tag data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ModGameEventTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.GAME_EVENT, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        FabricTagBuilder IgnoreVibrationsSneakingTagBuilder =
                getOrCreateTagBuilder(GameEventTags.IGNORE_VIBRATIONS_SNEAKING);
        IgnoreVibrationsSneakingTagBuilder.add(ModGameEvent.SPELL_CAST.registryKey());

        FabricTagBuilder VibrationsTagBuilder =
                getOrCreateTagBuilder(GameEventTags.VIBRATIONS);
        VibrationsTagBuilder.add(ModGameEvent.SPELL_CAST.registryKey());

        FabricTagBuilder WardenCanListenTagBuilder =
                getOrCreateTagBuilder(GameEventTags.WARDEN_CAN_LISTEN);
        WardenCanListenTagBuilder.add(ModGameEvent.SPELL_CAST.registryKey());
    }
}

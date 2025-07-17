package io.github.pistonpoek.magicalscepter.datagen.tag;

import io.github.pistonpoek.magicalscepter.world.event.ModGameEvent;
import net.minecraft.data.DataOutput;
import net.minecraft.data.tag.vanilla.VanillaGameEventTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.GameEventTags;

import java.util.concurrent.CompletableFuture;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.data.tag.vanilla.VanillaGameEventTagProvider
 */
public class ModGameEventTagProvider extends VanillaGameEventTagProvider {
    /**
     * Construct a mod game event tag provider for data generation.
     *
     * @param output Data output to generate game event tag data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ModGameEventTagProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        this.builder(GameEventTags.IGNORE_VIBRATIONS_SNEAKING)
                .add(ModGameEvent.SPELL_CAST.registryKey());

        this.builder(GameEventTags.VIBRATIONS)
                .add(ModGameEvent.SPELL_CAST.registryKey());

        this.builder(GameEventTags.WARDEN_CAN_LISTEN)
                .add(ModGameEvent.SPELL_CAST.registryKey());
    }
}

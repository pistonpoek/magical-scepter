package io.github.pistonpoek.magicalscepter.datagen.tag;

import io.github.pistonpoek.magicalscepter.world.event.ModGameEvent;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.GameEventTagsProvider;
import net.minecraft.tags.GameEventTags;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.data.tags.GameEventTagsProvider
 */
public class ModGameEventTagProvider extends GameEventTagsProvider {
    /**
     * Construct a mod game event tag provider for data generation.
     *
     * @param output           Data output to generate game event tag data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ModGameEventTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        this.tag(GameEventTags.IGNORE_VIBRATIONS_SNEAKING)
                .add(ModGameEvent.SPELL_CAST.key());

        this.tag(GameEventTags.VIBRATIONS)
                .add(ModGameEvent.SPELL_CAST.key());

        this.tag(GameEventTags.WARDEN_CAN_LISTEN)
                .add(ModGameEvent.SPELL_CAST.key());
    }
}

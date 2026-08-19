package io.github.pistonpoek.magicalscepter.datagen.tag;

import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.registry.tag.ScepterTags;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;

/**
 * Mod data generator for scepter tags.
 */
public class ScepterTagProvider extends KeyTagProvider<Scepter> {
    /**
     * Construct a mod scepter tag provider for data generation.
     *
     * @param output           Data output to generate scepter tag data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ScepterTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, ModRegistryKeys.SCEPTER, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        this.tag(ScepterTags.INFUSED).addAll(Scepters.ALL_INFUSED_SCEPTERS);
    }
}

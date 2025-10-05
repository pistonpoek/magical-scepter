package io.github.pistonpoek.magicalscepter.datagen.tag;

import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.registry.tag.ScepterTags;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import net.minecraft.data.DataOutput;
import net.minecraft.data.tag.SimpleTagProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ScepterTagProvider extends SimpleTagProvider<Scepter> {
    /**
     * Construct a mod scepter tag provider for data generation.
     *
     * @param output           Data output to generate scepter tag data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ScepterTagProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, ModRegistryKeys.SCEPTER, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.builder(ScepterTags.INFUSED).add(Scepters.ALL_INFUSED_SCEPTERS);
    }
}

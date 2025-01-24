package io.github.pistonpoek.magicalscepter.datagen.tag;

import io.github.pistonpoek.magicalscepter.registry.tag.ScepterTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;

import java.util.concurrent.CompletableFuture;

public class ScepterTagProvider extends FabricTagProvider<Scepter> {
    public ScepterTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, ModRegistryKeys.SCEPTER, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        FabricTagBuilder infusedScepterTagBuilder = getOrCreateTagBuilder(ScepterTags.INFUSED);
        for (RegistryKey<Scepter> scepter: Scepters.ALL_INFUSED_SCEPTERS) {
            infusedScepterTagBuilder.add(scepter);
        }
    }
}

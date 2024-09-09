package net.pistonpoek.magicalscepter.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.pistonpoek.magicalscepter.scepter.Scepter;

import java.util.concurrent.CompletableFuture;

public class ModScepterTagProvider extends FabricTagProvider<Scepter> {
    public ModScepterTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, ModRegistryKeys.SCEPTER, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
//        getOrCreateTagBuilder(ModTags.Scepters.INFUSABLE)
//                .add(Scepters.MAGICAL_KEY);
    }
}

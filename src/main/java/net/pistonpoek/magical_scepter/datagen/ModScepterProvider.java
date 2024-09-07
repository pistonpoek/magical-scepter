package net.pistonpoek.magical_scepter.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.pistonpoek.magical_scepter.item.scepter.Scepters;

import java.util.concurrent.CompletableFuture;

public class ModScepterProvider extends ScepterProvider {

    public ModScepterProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate() {
        scepters.put(Scepters.MAGICAL_KEY, Scepters.MAGICAL_BUILDER);
        scepters.put(Scepters.BLAZE_KEY, Scepters.BLAZE_BUILDER);
        scepters.put(Scepters.BREEZE_KEY, Scepters.BREEZE_BUILDER);
        scepters.put(Scepters.DRAGON_KEY, Scepters.DRAGON_BUILDER);
        scepters.put(Scepters.EVOKER_KEY, Scepters.EVOKER_BUILDER);
        scepters.put(Scepters.GHAST_KEY, Scepters.GHAST_BUILDER);
    }
}

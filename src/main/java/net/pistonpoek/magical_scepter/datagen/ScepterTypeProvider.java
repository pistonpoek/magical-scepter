package net.pistonpoek.magical_scepter.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.pistonpoek.magical_scepter.ModRegistryKeys;
import net.pistonpoek.magical_scepter.item.scepter.Scepter;
import net.pistonpoek.magical_scepter.item.scepter.Scepters;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ScepterTypeProvider extends FabricCodecDataProvider<Scepter> {
    public ScepterTypeProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture, ModRegistryKeys.SCEPTER, Scepter.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, Scepter> provider, RegistryWrapper.WrapperLookup lookup) {
        provider.accept(Scepters.MAGICAL_KEY.getValue(), Scepters.MAGICAL);
        provider.accept(Scepters.BLAZE_KEY.getValue(), Scepters.BLAZE);
        provider.accept(Scepters.BREEZE_KEY.getValue(), Scepters.BREEZE);
        provider.accept(Scepters.DRAGON_KEY.getValue(), Scepters.DRAGON);
        provider.accept(Scepters.EVOKER_KEY.getValue(), Scepters.EVOKER);
        provider.accept(Scepters.GHAST_KEY.getValue(), Scepters.GHAST);
        provider.accept(Scepters.GUARDIAN_KEY.getValue(), Scepters.GUARDIAN);
        provider.accept(Scepters.WARDEN_KEY.getValue(), Scepters.WARDEN);
        provider.accept(Scepters.WITHER_KEY.getValue(), Scepters.WITHER);
    }

    @Override
    public String getName() {
        return "ScepterTypeProvider";
    }
}

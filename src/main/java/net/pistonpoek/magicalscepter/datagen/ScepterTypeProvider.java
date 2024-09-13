package net.pistonpoek.magicalscepter.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.pistonpoek.magicalscepter.scepter.Scepter;
import net.pistonpoek.magicalscepter.scepter.Scepters;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ScepterTypeProvider extends FabricCodecDataProvider<Scepter> {
    public ScepterTypeProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture, DataOutput.OutputType.DATA_PACK, ModRegistryKeys.SCEPTER.getValue().toString().replace(":","/"), Scepter.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, Scepter> provider, RegistryWrapper.WrapperLookup lookup) {
        RegistryEntryLookup<Scepter> scepterLookup = lookup.createRegistryLookup().getOrThrow(ModRegistryKeys.SCEPTER);

        for (RegistryKey<Scepter> scepterKey: Scepters.SCEPTER_KEYS) {
            addSpell(provider, scepterLookup, scepterKey);
        }
    }

    private static void addSpell(BiConsumer<Identifier, Scepter> provider,
                                 RegistryEntryLookup<Scepter> lookup,
                                 RegistryKey<Scepter> key) {
        provider.accept(key.getValue(), lookup.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "ScepterTypeProvider";
    }
}

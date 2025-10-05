package io.github.pistonpoek.magicalscepter.datagen.type;

import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ScepterProvider extends FabricCodecDataProvider<Scepter> {
    /**
     * Construct a scepter provider for data generation.
     *
     * @param output           Data output to generate scepter data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ScepterProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture, DataOutput.OutputType.DATA_PACK,
                ModRegistryKeys.directory(ModRegistryKeys.SCEPTER), Scepter.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, Scepter> provider, RegistryWrapper.WrapperLookup registries) {
        RegistryEntryLookup<Scepter> scepterLookup = registries.getOrThrow(ModRegistryKeys.SCEPTER);

        for (RegistryKey<Scepter> scepterKey : Scepters.SCEPTER_KEYS) {
            addScepter(provider, scepterLookup, scepterKey);
        }
    }

    /**
     * Add a scepter to the specified provider.
     *
     * @param provider Provider to add the scepter to.
     * @param lookup   Registry entry lookup for the scepter.
     * @param key      Registry key to add to the scepter provider.
     */
    private static void addScepter(BiConsumer<Identifier, Scepter> provider,
                                   RegistryEntryLookup<Scepter> lookup,
                                   RegistryKey<Scepter> key) {
        provider.accept(key.getValue(), lookup.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Scepter";
    }
}

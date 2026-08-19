package io.github.pistonpoek.magicalscepter.datagen.codec;

import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * Mod data provider for scepters.
 *
 * @see Scepters
 */
public class ScepterProvider extends FabricCodecDataProvider<Scepter> {
    /**
     * Construct a scepter provider for data generation.
     *
     * @param output           Data output to generate scepter data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ScepterProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture, PackOutput.Target.DATA_PACK,
                ModRegistryKeys.directory(ModRegistryKeys.SCEPTER), Scepter.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, Scepter> provider, HolderLookup.Provider registries) {
        HolderGetter<Scepter> scepterLookup = registries.lookupOrThrow(ModRegistryKeys.SCEPTER);

        for (ResourceKey<Scepter> scepterKey : Scepters.KEYS) {
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
                                   HolderGetter<Scepter> lookup,
                                   ResourceKey<Scepter> key) {
        provider.accept(key.identifier(), lookup.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Scepter";
    }
}

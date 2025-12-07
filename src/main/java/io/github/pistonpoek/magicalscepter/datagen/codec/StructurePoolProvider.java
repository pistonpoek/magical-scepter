package io.github.pistonpoek.magicalscepter.datagen.codec;

import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.structure.pool.ModStructurePools;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * Mod data provider for structure pools.
 *
 * @see ModStructurePools
 */
public class StructurePoolProvider extends FabricCodecDataProvider<StructurePool> {
    /**
     * Construct a mod structure pool provider for data generation.
     *
     * @param output           Data output to generate structure pool data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public StructurePoolProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture, DataOutput.OutputType.DATA_PACK,
                ModRegistryKeys.directory(RegistryKeys.TEMPLATE_POOL), StructurePool.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, StructurePool> provider, RegistryWrapper.WrapperLookup registries) {
        RegistryEntryLookup<StructurePool> structurePoolLookup = registries.getOrThrow(RegistryKeys.TEMPLATE_POOL);

        for (RegistryKey<StructurePool> structurePoolKey : ModStructurePools.KEYS) {
            addStructurePool(provider, structurePoolLookup, structurePoolKey);
        }
    }

    /**
     * Add a structure pool to the specified provider.
     *
     * @param provider Provider to add the structure pool to.
     * @param lookup   Registry entry lookup for the structure pool.
     * @param key      Registry key to add to the structure pool provider.
     */
    private static void addStructurePool(BiConsumer<Identifier, StructurePool> provider,
                                       RegistryEntryLookup<StructurePool> lookup,
                                       RegistryKey<StructurePool> key) {
        provider.accept(key.getValue(), lookup.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Structure Pool";
    }
}

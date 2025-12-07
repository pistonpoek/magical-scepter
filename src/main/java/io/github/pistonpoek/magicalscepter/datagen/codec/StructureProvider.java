package io.github.pistonpoek.magicalscepter.datagen.codec;

import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.world.gen.structure.ModStructures;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * Mod data provider for structures.
 *
 * @see ModStructures
 */
public class StructureProvider extends FabricCodecDataProvider<Structure> {
    /**
     * Construct a mod structure provider for data generation.
     *
     * @param output           Data output to generate structure data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public StructureProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture, DataOutput.OutputType.DATA_PACK,
                ModRegistryKeys.directory(RegistryKeys.STRUCTURE), Structure.STRUCTURE_CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, Structure> provider, RegistryWrapper.WrapperLookup registries) {
        RegistryEntryLookup<Structure> structureLookup = registries.getOrThrow(RegistryKeys.STRUCTURE);

        for (RegistryKey<Structure> structureKey : ModStructures.KEYS) {
            addStructure(provider, structureLookup, structureKey);
        }
    }

    /**
     * Add a structure to the specified provider.
     *
     * @param provider Provider to add the structure to.
     * @param lookup   Registry entry lookup for the structure.
     * @param key      Registry key to add to the structure provider.
     */
    private static void addStructure(BiConsumer<Identifier, Structure> provider,
                                         RegistryEntryLookup<Structure> lookup,
                                         RegistryKey<Structure> key) {
        provider.accept(key.getValue(), lookup.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Structure";
    }
}

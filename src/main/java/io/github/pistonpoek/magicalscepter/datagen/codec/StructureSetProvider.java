package io.github.pistonpoek.magicalscepter.datagen.codec;

import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.structure.ModStructureSets;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * Mod data provider for structure sets.
 *
 * @see ModStructureSets
 */
public class StructureSetProvider extends FabricCodecDataProvider<StructureSet> {
    /**
     * Construct a mod structure set provider for data generation.
     *
     * @param output           Data output to generate structure set data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public StructureSetProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture, DataOutput.OutputType.DATA_PACK,
                ModRegistryKeys.directory(RegistryKeys.STRUCTURE_SET), StructureSet.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, StructureSet> provider, RegistryWrapper.WrapperLookup registries) {
        RegistryEntryLookup<StructureSet> structureSetLookup = registries.getOrThrow(RegistryKeys.STRUCTURE_SET);

        for (RegistryKey<StructureSet> structureSetKey : ModStructureSets.KEYS) {
            addStructureSet(provider, structureSetLookup, structureSetKey);
        }
    }

    /**
     * Add a structure set to the specified provider.
     *
     * @param provider Provider to add the structure set to.
     * @param lookup   Registry entry lookup for the structure set.
     * @param key      Registry key to add to the structure set provider.
     */
    private static void addStructureSet(BiConsumer<Identifier, StructureSet> provider,
                                         RegistryEntryLookup<StructureSet> lookup,
                                         RegistryKey<StructureSet> key) {
        provider.accept(key.getValue(), lookup.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Structure Set";
    }
}
package io.github.pistonpoek.magicalscepter.datagen.codec;

import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.world.gen.structure.ModStructures;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
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
    public StructureProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture, PackOutput.Target.DATA_PACK,
                ModRegistryKeys.directory(Registries.STRUCTURE), Structure.DIRECT_CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, Structure> provider, HolderLookup.Provider registries) {
        HolderGetter<Structure> structureLookup = registries.lookupOrThrow(Registries.STRUCTURE);

        for (ResourceKey<Structure> structureKey : ModStructures.KEYS) {
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
                                         HolderGetter<Structure> lookup,
                                         ResourceKey<Structure> key) {
        provider.accept(key.identifier(), lookup.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Structure";
    }
}

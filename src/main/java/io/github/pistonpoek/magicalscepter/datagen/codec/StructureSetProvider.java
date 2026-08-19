package io.github.pistonpoek.magicalscepter.datagen.codec;

import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.structure.ModStructureSets;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.StructureSet;
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
    public StructureSetProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture, PackOutput.Target.DATA_PACK,
                ModRegistryKeys.directory(Registries.STRUCTURE_SET), StructureSet.DIRECT_CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, StructureSet> provider, HolderLookup.Provider registries) {
        HolderGetter<StructureSet> structureSetLookup = registries.lookupOrThrow(Registries.STRUCTURE_SET);

        for (ResourceKey<StructureSet> structureSetKey : ModStructureSets.KEYS) {
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
                                         HolderGetter<StructureSet> lookup,
                                         ResourceKey<StructureSet> key) {
        provider.accept(key.identifier(), lookup.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Structure Set";
    }
}
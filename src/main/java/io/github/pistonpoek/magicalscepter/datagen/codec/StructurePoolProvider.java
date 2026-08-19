package io.github.pistonpoek.magicalscepter.datagen.codec;

import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.structure.pool.ModStructurePools;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * Mod data provider for structure pools.
 *
 * @see ModStructurePools
 */
public class StructurePoolProvider extends FabricCodecDataProvider<StructureTemplatePool> {
    /**
     * Construct a mod structure pool provider for data generation.
     *
     * @param output           Data output to generate structure pool data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public StructurePoolProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture, PackOutput.Target.DATA_PACK,
                ModRegistryKeys.directory(Registries.TEMPLATE_POOL), StructureTemplatePool.DIRECT_CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, StructureTemplatePool> provider, HolderLookup.Provider registries) {
        HolderGetter<StructureTemplatePool> structurePoolLookup = registries.lookupOrThrow(Registries.TEMPLATE_POOL);

        for (ResourceKey<StructureTemplatePool> structurePoolKey : ModStructurePools.KEYS) {
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
    private static void addStructurePool(BiConsumer<Identifier, StructureTemplatePool> provider,
                                       HolderGetter<StructureTemplatePool> lookup,
                                       ResourceKey<StructureTemplatePool> key) {
        provider.accept(key.identifier(), lookup.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Structure Pool";
    }
}

package io.github.pistonpoek.magicalscepter.structure;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import io.github.pistonpoek.magicalscepter.structure.pool.ModStructurePools;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorLists;

/**
 * Structure pool generator for the old taiga cabin structure.
 */
public class OldTaigaCabinGenerator {
    public static final RegistryKey<StructurePool> STRUCTURE_POOL = ModStructurePools.of("old_taiga_cabin");

    /**
     * Boostrap the structure pool registry with old taiga cabin structure pools.
     *
     * @param registry Structure pool registry to bootstrap.
     * @return Structure pool registry key of the generator that is registered.
     */
    public static RegistryKey<StructurePool> bootstrap(Registerable<StructurePool> registry) {
        RegistryEntryLookup<StructureProcessorList> processorListLookup = registry.getRegistryLookup(RegistryKeys.PROCESSOR_LIST);
        RegistryEntry<StructureProcessorList> mossifyProcessorEntry = processorListLookup.getOrThrow(StructureProcessorLists.MOSSIFY_70_PERCENT);
        RegistryEntryLookup<StructurePool> structurePoolLookup = registry.getRegistryLookup(RegistryKeys.TEMPLATE_POOL);
        RegistryEntry<StructurePool> emptyStructurePool = structurePoolLookup.getOrThrow(StructurePools.EMPTY);
        registry.register(
                STRUCTURE_POOL,
                new StructurePool(
                        emptyStructurePool,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.ofProcessedSingle(
                                        ModIdentifier.identifier("old_taiga_cabin"),
                                        mossifyProcessorEntry
                                ), 1),
                                Pair.of(StructurePoolElement.ofProcessedSingle(
                                        ModIdentifier.identifier("old_taiga_cabin_mirrored"),
                                        mossifyProcessorEntry
                                ), 1)
                        ),
                        StructurePool.Projection.RIGID
                )
        );
        return STRUCTURE_POOL;
    }
}

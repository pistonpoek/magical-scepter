package io.github.pistonpoek.magicalscepter.structure;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import io.github.pistonpoek.magicalscepter.structure.pool.ModStructurePools;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

/**
 * Structure pool generator for the old taiga cabin structure.
 */
public class OldTaigaCabinGenerator {
    public static final ResourceKey<StructureTemplatePool> STRUCTURE_POOL = ModStructurePools.of("old_taiga_cabin");

    /**
     * Boostrap the structure pool registry with old taiga cabin structure pools.
     *
     * @param registry Structure pool registry to bootstrap.
     * @return Structure pool registry key of the generator that is registered.
     */
    public static ResourceKey<StructureTemplatePool> bootstrap(BootstrapContext<StructureTemplatePool> registry) {
        HolderGetter<StructureProcessorList> processorListLookup = registry.lookup(Registries.PROCESSOR_LIST);
        Holder<StructureProcessorList> mossifyProcessorEntry = processorListLookup.getOrThrow(ProcessorLists.MOSSIFY_70_PERCENT);
        HolderGetter<StructureTemplatePool> structurePoolLookup = registry.lookup(Registries.TEMPLATE_POOL);
        Holder<StructureTemplatePool> emptyStructurePool = structurePoolLookup.getOrThrow(Pools.EMPTY);
        registry.register(
                STRUCTURE_POOL,
                new StructureTemplatePool(
                        emptyStructurePool,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single(
                                        ModIdentifier.identifier("old_taiga_cabin"),
                                        mossifyProcessorEntry
                                ), 1),
                                Pair.of(StructurePoolElement.single(
                                        ModIdentifier.identifier("old_taiga_cabin_mirrored"),
                                        mossifyProcessorEntry
                                ), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );
        return STRUCTURE_POOL;
    }
}

package io.github.pistonpoek.magicalscepter.structure;

import io.github.pistonpoek.magicalscepter.world.gen.structure.ModStructureKeys;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.AbstractSpreadingStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.data.worldgen.StructureSets
 */
public interface ModStructureSets {
    List<ResourceKey<StructureSet>> KEYS = new ArrayList<>();

    /**
     * Bootstrap the structure set registry.
     *
     * @param registry Structure set registry to bootstrap.
     */
    static void bootstrap(BootstrapContext<StructureSet> registry) {
        HolderGetter<Structure> structureLookup = registry.lookup(Registries.STRUCTURE);
        HolderGetter<StructureSet> structureSetLookup = registry.lookup(Registries.STRUCTURE_SET);
        register(registry, ModStructureSetKeys.OLD_TAIGA_CABIN,
                new StructureSet(
                        structureLookup.getOrThrow(ModStructureKeys.OLD_TAIGA_CABIN),
                        new RandomSpreadStructurePlacement(
                                Vec3i.ZERO,
                                AbstractSpreadingStructurePlacement.FrequencyReductionMethod.DEFAULT,
                                1.0F,
                                1685961,
                                Optional.of(new AbstractSpreadingStructurePlacement.ExclusionZone(
                                                structureSetLookup.getOrThrow(BuiltinStructureSets.VILLAGES), 10
                                )),
                                28,
                                8,
                                RandomSpreadType.LINEAR
                        )
                )
        );
    }

    /**
     * Register the specified structure set to the registry under the specified registry key.
     *
     * @param registry Structure set registry to register in.
     * @param key Structure set registry key to register under.
     * @param structureSet Structure set to register.
     */
    private static void register(BootstrapContext<StructureSet> registry,
                                 ResourceKey<StructureSet> key, StructureSet structureSet) {
        KEYS.add(key);
        registry.register(key, structureSet);
    }
}

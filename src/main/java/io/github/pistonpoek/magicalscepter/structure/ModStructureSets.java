package io.github.pistonpoek.magicalscepter.structure;

import io.github.pistonpoek.magicalscepter.world.gen.structure.ModStructureKeys;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.StructureSet;
import net.minecraft.structure.StructureSetKeys;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.gen.chunk.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.gen.chunk.placement.SpreadType;
import net.minecraft.world.gen.chunk.placement.StructurePlacement;
import net.minecraft.world.gen.structure.Structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ModStructureSets {
    List<RegistryKey<StructureSet>> KEYS = new ArrayList<>();

    static void bootstrap(Registerable<StructureSet> registry) {
        RegistryEntryLookup<Structure> structureLookup = registry.getRegistryLookup(RegistryKeys.STRUCTURE);
        RegistryEntryLookup<StructureSet> structureSetLookup = registry.getRegistryLookup(RegistryKeys.STRUCTURE_SET);
        register(registry, ModStructureSetKeys.OLD_TAIGA_CABIN,
                new StructureSet(
                        structureLookup.getOrThrow(ModStructureKeys.OLD_TAIGA_CABIN),
                        new RandomSpreadStructurePlacement(
                                Vec3i.ZERO,
                                StructurePlacement.FrequencyReductionMethod.DEFAULT,
                                1.0F,
                                1685961,
                                Optional.of(new StructurePlacement.ExclusionZone(
                                                structureSetLookup.getOrThrow(StructureSetKeys.VILLAGES), 10
                                        )),
                                32,
                                8,
                                SpreadType.LINEAR
                        )
                )
        );
    }

    private static void register(Registerable<StructureSet> registry,
                                 RegistryKey<StructureSet> key, StructureSet structureSet) {
        KEYS.add(key);
        registry.register(key, structureSet);
    }
}

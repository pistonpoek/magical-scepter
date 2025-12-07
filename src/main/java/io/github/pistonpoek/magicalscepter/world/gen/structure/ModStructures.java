package io.github.pistonpoek.magicalscepter.world.gen.structure;

import io.github.pistonpoek.magicalscepter.entity.ModEntityType;
import io.github.pistonpoek.magicalscepter.registry.tag.ModBiomeTags;
import io.github.pistonpoek.magicalscepter.structure.OldTaigaCabinGenerator;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.StructureLiquidSettings;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.collection.Pool;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureSpawns;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.heightprovider.ConstantHeightProvider;
import net.minecraft.world.gen.structure.DimensionPadding;
import net.minecraft.world.gen.structure.JigsawStructure;
import net.minecraft.world.gen.structure.Structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.world.gen.structure.Structures
 */
public class ModStructures {
    public static final List<RegistryKey<Structure>> KEYS = new ArrayList<>();

    /**
     * Bootstrap the structure registry.
     *
     * @param registry Structure registry to bootstrap.
     */
    public static void bootstrap(Registerable<Structure> registry) {
        RegistryEntryLookup<StructurePool> structurePoolLookup = registry.getRegistryLookup(RegistryKeys.TEMPLATE_POOL);
        RegistryEntryLookup<Biome> biomeLookup = registry.getRegistryLookup(RegistryKeys.BIOME);

        register(registry, ModStructureKeys.OLD_TAIGA_CABIN,
                new JigsawStructure(
                        new Structure.Config.Builder(biomeLookup.getOrThrow(ModBiomeTags.OLD_TAIGA_CABIN_HAS_STRUCTURE))
                                .spawnOverrides(Map.of(SpawnGroup.MONSTER, new StructureSpawns(
                                        StructureSpawns.BoundingBox.PIECE,
                                        Pool.of(new SpawnSettings.SpawnEntry(ModEntityType.SORCERER, 1, 1))
                                )))
                                .step(GenerationStep.Feature.SURFACE_STRUCTURES)
                                .terrainAdaptation(StructureTerrainAdaptation.BEARD_THIN).build(),
                        structurePoolLookup.getOrThrow(OldTaigaCabinGenerator.STRUCTURE_POOL),
                        Optional.empty(),
                        1,
                        ConstantHeightProvider.ZERO,
                        false,
                        Optional.of(Heightmap.Type.WORLD_SURFACE_WG),
                        new JigsawStructure.MaxDistanceFromCenter(80),
                        List.of(),
                        DimensionPadding.NONE,
                        StructureLiquidSettings.APPLY_WATERLOGGING
                )
        );
    }

    /**
     * Register the specified structure to the registry under the specified registry key.
     *
     * @param registry Structure registry to register in.
     * @param key Structure registry key to register under.
     * @param structure Structure to register.
     */
    private static void register(Registerable<Structure> registry, RegistryKey<Structure> key, Structure structure) {
        KEYS.add(key);
        registry.register(key, structure);
    }
}

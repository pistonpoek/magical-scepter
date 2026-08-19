package io.github.pistonpoek.magicalscepter.world.gen.structure;

import io.github.pistonpoek.magicalscepter.entity.ModEntityType;
import io.github.pistonpoek.magicalscepter.registry.tag.ModBiomeTags;
import io.github.pistonpoek.magicalscepter.structure.OldTaigaCabinGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.data.worldgen.Structures
 */
public class ModStructures {
    public static final List<ResourceKey<Structure>> KEYS = new ArrayList<>();

    /**
     * Bootstrap the structure registry.
     *
     * @param registry Structure registry to bootstrap.
     */
    public static void bootstrap(BootstrapContext<Structure> registry) {
        HolderGetter<StructureTemplatePool> structurePoolLookup = registry.lookup(Registries.TEMPLATE_POOL);
        HolderGetter<Biome> biomeLookup = registry.lookup(Registries.BIOME);

        register(registry, ModStructureKeys.OLD_TAIGA_CABIN,
                new JigsawStructure(
                        new Structure.StructureSettings.Builder(biomeLookup.getOrThrow(ModBiomeTags.OLD_TAIGA_CABIN_HAS_STRUCTURE))
                                .spawnOverrides(Map.of(MobCategory.MONSTER, new StructureSpawnOverride(
                                        StructureSpawnOverride.BoundingBoxType.PIECE,
                                        WeightedList.of(new MobSpawnSettings.SpawnerData(ModEntityType.SORCERER, 1, 1))
                                )))
                                .generationStep(GenerationStep.Decoration.SURFACE_STRUCTURES)
                                .terrainAdapation(TerrainAdjustment.BEARD_THIN).build(),
                        structurePoolLookup.getOrThrow(OldTaigaCabinGenerator.STRUCTURE_POOL),
                        Optional.empty(),
                        1,
                        ConstantHeight.ZERO,
                        false,
                        Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
                        new JigsawStructure.MaxDistance(80),
                        List.of(),
                        DimensionPadding.ZERO,
                        LiquidSettings.APPLY_WATERLOGGING
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
    private static void register(BootstrapContext<Structure> registry, ResourceKey<Structure> key, Structure structure) {
        KEYS.add(key);
        registry.register(key, structure);
    }
}

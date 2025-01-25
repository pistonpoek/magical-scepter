package io.github.pistonpoek.magicalscepter.entity;

import io.github.pistonpoek.magicalscepter.entity.projectile.GuardianBoltEntity;
import io.github.pistonpoek.magicalscepter.mixson.MixsonEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.entity.mob.RefractorEntity;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.SpawnSettings;

import java.util.Collection;
import java.util.Set;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.entity.EntityType
 */
public class ModEntityType {
    private static final Collection<RegistryKey<Biome>> REFRACTOR_SPAWN_BIOMES = Set.of(BiomeKeys.PLAINS, BiomeKeys.SUNFLOWER_PLAINS, BiomeKeys.SNOWY_PLAINS, BiomeKeys.ICE_SPIKES, BiomeKeys.DESERT, BiomeKeys.SWAMP, BiomeKeys.MANGROVE_SWAMP, BiomeKeys.FOREST, BiomeKeys.FLOWER_FOREST, BiomeKeys.BIRCH_FOREST, BiomeKeys.DARK_FOREST, BiomeKeys.OLD_GROWTH_BIRCH_FOREST, BiomeKeys.OLD_GROWTH_PINE_TAIGA, BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA, BiomeKeys.TAIGA, BiomeKeys.SNOWY_TAIGA, BiomeKeys.SAVANNA, BiomeKeys.SAVANNA_PLATEAU, BiomeKeys.WINDSWEPT_HILLS, BiomeKeys.WINDSWEPT_GRAVELLY_HILLS, BiomeKeys.WINDSWEPT_FOREST, BiomeKeys.WINDSWEPT_SAVANNA, BiomeKeys.JUNGLE, BiomeKeys.SPARSE_JUNGLE, BiomeKeys.BAMBOO_JUNGLE, BiomeKeys.BADLANDS, BiomeKeys.ERODED_BADLANDS, BiomeKeys.WOODED_BADLANDS, BiomeKeys.MEADOW, BiomeKeys.CHERRY_GROVE, BiomeKeys.GROVE, BiomeKeys.SNOWY_SLOPES, BiomeKeys.FROZEN_PEAKS, BiomeKeys.JAGGED_PEAKS, BiomeKeys.STONY_PEAKS, BiomeKeys.RIVER, BiomeKeys.FROZEN_RIVER, BiomeKeys.BEACH, BiomeKeys.SNOWY_BEACH, BiomeKeys.STONY_SHORE, BiomeKeys.WARM_OCEAN, BiomeKeys.LUKEWARM_OCEAN, BiomeKeys.DEEP_LUKEWARM_OCEAN, BiomeKeys.OCEAN, BiomeKeys.DEEP_OCEAN, BiomeKeys.COLD_OCEAN, BiomeKeys.DEEP_COLD_OCEAN, BiomeKeys.FROZEN_OCEAN, BiomeKeys.DEEP_FROZEN_OCEAN, BiomeKeys.DRIPSTONE_CAVES, BiomeKeys.LUSH_CAVES);
    public static final EntityType<RefractorEntity> REFRACTOR = registerMob(
        "refractor",
        EntityType.Builder.create(RefractorEntity::new, SpawnGroup.MONSTER)
                .dimensions(0.6F, 1.95F)
                .passengerAttachments(2.0F)
                .vehicleAttachment(-0.6F)
                .maxTrackingRange(8)
    );
    public static final EntityType<GuardianBoltEntity> GUARDIAN_BOLT = register(
            "guardian_bolt",
            EntityType.Builder.create(GuardianBoltEntity::new, SpawnGroup.MISC)
                    .dropsNothing()
                    .dimensions(0.3125F, 0.3125F)
                    .eyeHeight(0.0F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(10)
    );

    /**
     * Initialize the class for the static fields.
     */
    public static void init() {
        MagicalScepter.LOGGER.info("Registering Entities for " + ModIdentifier.MOD_NAME);
        ModSpawnRestriction.init();
        for (RegistryKey<Biome> biome : REFRACTOR_SPAWN_BIOMES) {
            MixsonEvents.registerMonsterSpawnEntry(biome, SpawnGroup.MONSTER,
                    new SpawnSettings.SpawnEntry(REFRACTOR, 5, 1, 1));
        }
    }

    private static <T extends Entity> EntityType<T> registerMob(String id, EntityType.Builder<T> type) {
        EntityType<T> entityType = register(id, type);
        MixsonEvents.registerMobModification(ModIdentifier.of(id));
        return entityType;
    }

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, ModIdentifier.of(id),
                type.build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, ModIdentifier.of(id))));
    }
}

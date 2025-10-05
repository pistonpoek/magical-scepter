package io.github.pistonpoek.magicalscepter.entity;

import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.entity.mob.RefractorEntity;
import io.github.pistonpoek.magicalscepter.entity.projectile.BlazeFireChargeEntity;
import io.github.pistonpoek.magicalscepter.entity.projectile.GhastFireballEntity;
import io.github.pistonpoek.magicalscepter.entity.projectile.GuardianBoltEntity;
import io.github.pistonpoek.magicalscepter.entity.projectile.WitherSkullEntity;
import io.github.pistonpoek.magicalscepter.mixson.MixsonEvents;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.SpawnSettings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.entity.EntityType
 */
public class ModEntityType {
    private static final Collection<RegistryKey<Biome>> REFRACTOR_SPAWN_BIOMES = Set.of(BiomeKeys.PLAINS, BiomeKeys.SUNFLOWER_PLAINS, BiomeKeys.SNOWY_PLAINS, BiomeKeys.ICE_SPIKES, BiomeKeys.DESERT, BiomeKeys.SWAMP, BiomeKeys.MANGROVE_SWAMP, BiomeKeys.FOREST, BiomeKeys.FLOWER_FOREST, BiomeKeys.BIRCH_FOREST, BiomeKeys.DARK_FOREST, BiomeKeys.OLD_GROWTH_BIRCH_FOREST, BiomeKeys.OLD_GROWTH_PINE_TAIGA, BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA, BiomeKeys.TAIGA, BiomeKeys.SNOWY_TAIGA, BiomeKeys.SAVANNA, BiomeKeys.SAVANNA_PLATEAU, BiomeKeys.WINDSWEPT_HILLS, BiomeKeys.WINDSWEPT_GRAVELLY_HILLS, BiomeKeys.WINDSWEPT_FOREST, BiomeKeys.WINDSWEPT_SAVANNA, BiomeKeys.JUNGLE, BiomeKeys.SPARSE_JUNGLE, BiomeKeys.BAMBOO_JUNGLE, BiomeKeys.BADLANDS, BiomeKeys.ERODED_BADLANDS, BiomeKeys.WOODED_BADLANDS, BiomeKeys.MEADOW, BiomeKeys.CHERRY_GROVE, BiomeKeys.GROVE, BiomeKeys.SNOWY_SLOPES, BiomeKeys.FROZEN_PEAKS, BiomeKeys.JAGGED_PEAKS, BiomeKeys.STONY_PEAKS, BiomeKeys.RIVER, BiomeKeys.FROZEN_RIVER, BiomeKeys.BEACH, BiomeKeys.SNOWY_BEACH, BiomeKeys.STONY_SHORE, BiomeKeys.WARM_OCEAN, BiomeKeys.LUKEWARM_OCEAN, BiomeKeys.DEEP_LUKEWARM_OCEAN, BiomeKeys.OCEAN, BiomeKeys.DEEP_OCEAN, BiomeKeys.COLD_OCEAN, BiomeKeys.DEEP_COLD_OCEAN, BiomeKeys.FROZEN_OCEAN, BiomeKeys.DEEP_FROZEN_OCEAN, BiomeKeys.DRIPSTONE_CAVES, BiomeKeys.LUSH_CAVES);
    private static final Collection<Identifier> monsters = new ArrayList<>();
    public static final EntityType<RefractorEntity> REFRACTOR = registerMonster(
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
    public static final EntityType<BlazeFireChargeEntity> BLAZE_FIRE_CHARGE = register(
            "blaze_fire_charge",
            EntityType.Builder.create(BlazeFireChargeEntity::new, SpawnGroup.MISC)
                    .dropsNothing()
                    .dimensions(0.3125F, 0.3125F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(10)
    );
    public static final EntityType<GhastFireballEntity> GHAST_FIREBALL = register(
            "ghast_fireball",
            EntityType.Builder.create(GhastFireballEntity::new, SpawnGroup.MISC)
                    .dropsNothing()
                    .dimensions(1.0F, 1.0F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(10)
    );
    public static final EntityType<WitherSkullEntity> WITHER_SKULL = register(
            "wither_skull",
            EntityType.Builder.create(WitherSkullEntity::new, SpawnGroup.MISC)
                    .dropsNothing()
                    .dimensions(0.3125F, 0.3125F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(10)
    );

    /**
     * Initialize the class for the static fields.
     */
    public static void init() {
        MagicalScepter.LOGGER.info("Registering Entities for " + ModIdentifier.MOD_NAME);
        ModSpawnRestriction.init();
        for (Identifier monster : monsters) {
            MixsonEvents.registerMobModification(monster);
        }
        for (RegistryKey<Biome> biome : REFRACTOR_SPAWN_BIOMES) {
            MixsonEvents.registerMonsterSpawnEntry(biome, SpawnGroup.MONSTER, 5,
                    new SpawnSettings.SpawnEntry(REFRACTOR, 1, 1));
        }
    }

    /**
     * Register an entity type for the specified identifier and add it as monster to the advancements.
     *
     * @param identifier String to create mod identifier with for the entity type.
     * @param type       Entity type to register.
     * @param <T>        Entity type to register.
     * @return Registered registry entry of the entity type.
     */
    private static <T extends Entity> EntityType<T> registerMonster(String identifier, EntityType.Builder<T> type) {
        monsters.add(ModIdentifier.of(identifier));
        return register(identifier, type);
    }

    /**
     * Register an entity type for the specified identifier.
     *
     * @param identifier String to create mod identifier with for the entity type.
     * @param type       Entity type to register.
     * @param <T>        Entity type to register.
     * @return Registered registry entry of the entity type.
     */
    private static <T extends Entity> EntityType<T> register(String identifier, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, ModIdentifier.of(identifier),
                type.build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, ModIdentifier.of(identifier))));
    }
}

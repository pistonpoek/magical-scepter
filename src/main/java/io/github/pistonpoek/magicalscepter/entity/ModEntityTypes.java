package io.github.pistonpoek.magicalscepter.entity;

import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.entity.mob.SorcererEntity;
import io.github.pistonpoek.magicalscepter.entity.spell.*;
import io.github.pistonpoek.magicalscepter.mixson.MixsonEvents;
import io.github.pistonpoek.magicalscepter.sound.ModSoundEvents;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import io.github.pistonpoek.magicalscepter.util.ParrotEntityUtil;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.world.entity.EntityTypes
 */
public class ModEntityTypes {
    private static final Collection<ResourceKey<Biome>> SORCERER_SPAWN_BIOMES = Set.of(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.SNOWY_PLAINS, Biomes.ICE_SPIKES, Biomes.DESERT, Biomes.SWAMP, Biomes.MANGROVE_SWAMP, Biomes.FOREST, Biomes.FLOWER_FOREST, Biomes.BIRCH_FOREST, Biomes.DARK_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.TAIGA, Biomes.SNOWY_TAIGA, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_SAVANNA, Biomes.JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE, Biomes.BADLANDS, Biomes.ERODED_BADLANDS, Biomes.WOODED_BADLANDS, Biomes.MEADOW, Biomes.CHERRY_GROVE, Biomes.GROVE, Biomes.SNOWY_SLOPES, Biomes.FROZEN_PEAKS, Biomes.JAGGED_PEAKS, Biomes.STONY_PEAKS, Biomes.RIVER, Biomes.FROZEN_RIVER, Biomes.BEACH, Biomes.SNOWY_BEACH, Biomes.STONY_SHORE, Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.OCEAN, Biomes.DEEP_OCEAN, Biomes.COLD_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN, Biomes.DRIPSTONE_CAVES, Biomes.LUSH_CAVES);
    private static final Collection<ResourceKey<EntityType<?>>> MONSTERS = new ArrayList<>();
    public static final EntityType<SorcererEntity> SORCERER = registerMonster(
            ModEntityTypeIds.SORCERER,
            EntityType.Builder.of(SorcererEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .passengerAttachments(2.0F)
                    .ridingOffset(-0.6F)
                    .clientTrackingRange(8)
    );
    public static final EntityType<SpellDragonFireballEntity> SPELL_DRAGON_FIREBALL = register(
            ModEntityTypeIds.SPELL_DRAGON_FIREBALL,
            EntityType.Builder.of(SpellDragonFireballEntity::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
    );
    public static final EntityType<SpellFireChargeEntity> SPELL_FIRE_CHARGE = register(
            ModEntityTypeIds.SPELL_FIRE_CHARGE,
            EntityType.Builder.of(SpellFireChargeEntity::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.3125F, 0.3125F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
    );
    public static final EntityType<SpellFireballEntity> SPELL_FIREBALL = register(
            ModEntityTypeIds.SPELL_FIREBALL,
            EntityType.Builder.of(SpellFireballEntity::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
    );
    public static final EntityType<SpellGuardianBeamEntity> SPELL_GUARDIAN_BEAM = register(
            ModEntityTypeIds.SPELL_GUARDIAN_BEAM,
            EntityType.Builder.of(SpellGuardianBeamEntity::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.0F, 0.0F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
    );
    public static final EntityType<SpellWitherSkullEntity> SPELL_WITHER_SKULL = register(
            ModEntityTypeIds.SPELL_WITHER_SKULL,
            EntityType.Builder.of(SpellWitherSkullEntity::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.3125F, 0.3125F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
    );

    /**
     * Initialize the class for the static fields.
     */
    public static void init() {
        MagicalScepter.LOGGER.info("Registering Entities for " + ModIdentifier.MOD_NAME);
        ModSpawnRestriction.init();
        for (ResourceKey<EntityType<?>> monster : MONSTERS) {
            MixsonEvents.registerMobModification(monster.identifier());
        }
        for (ResourceKey<Biome> biome : SORCERER_SPAWN_BIOMES) {
            MixsonEvents.registerMonsterSpawnEntry(biome, MobCategory.MONSTER, 5,
                    new MobSpawnSettings.SpawnerData(SORCERER, 1, 1));
        }
        registerDefaultAttributes();
        registerParrotMimicSounds();
    }

    /**
     * Register default attributes for all living entity types.
     */
    private static void registerDefaultAttributes() {
        FabricDefaultAttributeRegistry.register(SORCERER, SorcererEntity.createSorcererAttributes());
    }

    /**
     * Register parrot mimic sounds for some entity types.
     */
    private static void registerParrotMimicSounds() {
        ParrotEntityUtil.AddMobSound(ModEntityTypes.SORCERER, ModSoundEvents.ENTITY_PARROT_IMITATE_SORCERER);
    }

    /**
     * Register a mod entity type for the specified name and add it as monster to the advancements.
     *
     * @param key        Resource key to register under.
     * @param type       Entity type to register.
     * @param <T>        Entity type to register.
     * @return Registered registry entry of the entity type.
     */
    private static <T extends Entity> EntityType<T> registerMonster(ResourceKey<EntityType<?>> key, EntityType.Builder<T> type) {
        MONSTERS.add(key);
        return register(key, type);
    }

    /**
     * Register a mod entity type for the specified key.
     *
     * @param key        Registry key to register under.
     * @param type       Entity type to register.
     * @param <T>        Entity type to register.
     * @return Registered registry entry of the entity type.
     */
    private static <T extends Entity> EntityType<T> register(
            ResourceKey<EntityType<?>> key,
            EntityType.Builder<T> type
    ) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key,
                type.build(key));
    }
}

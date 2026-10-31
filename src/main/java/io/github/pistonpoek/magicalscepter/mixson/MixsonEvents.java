package io.github.pistonpoek.magicalscepter.mixson;

import com.google.gson.JsonElement;
import io.github.pistonpoek.magicalscepter.mixson.advancement.adventure.KillAMobMixson;
import io.github.pistonpoek.magicalscepter.mixson.advancement.adventure.KillAllMobsMixson;
import io.github.pistonpoek.magicalscepter.mixson.advancement.nether.AllEffectsMixson;
import io.github.pistonpoek.magicalscepter.mixson.loot.AbandonedCampSecretChestMapMixson;
import io.github.pistonpoek.magicalscepter.mixson.world.BiomeSpawnEntryMixson;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.SharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.ramixin.mixson.Mixson;
import net.ramixin.mixson.enums.ErrorPolicy;
import net.ramixin.mixson.enums.Lifetime;
import net.ramixin.mixson.util.Index;
import net.ramixin.mixson.util.functions.Event;

import java.util.List;

/**
 * Collection of helper methods to register mixson events that alter existing json data.
 */
public class MixsonEvents {
    /**
     * Register a mob modification for the specified mob.
     *
     * @param mobIdentifier Identifier of the mob to register a mob modification for.
     *
     * @see net.minecraft.data.advancements.packs.VanillaAdventureAdvancements#createMonsterHunterAdvancement 
     */
    public static void registerMobModification(Identifier mobIdentifier) {
        registerMixsonEvent(List.of(new Index(Identifier.parse("advancement/adventure/kill_a_mob"))),
                ModIdentifier.identifier("kill_a_mob_" + mobIdentifier.getPath()),
                new KillAMobMixson(mobIdentifier));
        registerMixsonEvent(List.of(new Index(Identifier.parse("advancement/adventure/kill_all_mobs"))),
                ModIdentifier.identifier("kill_all_mobs_" + mobIdentifier.getPath()),
                new KillAllMobsMixson(mobIdentifier));
    }

    /**
     * Register an effect modification for the specified effect.
     *
     * @param effectIdentifier Identifier of the effect to register an effect modification for.
     */
    public static void registerEffectModification(Identifier effectIdentifier) {
        registerMixsonEvent(List.of(new Index(Identifier.parse("advancement/nether/all_effects"))),
                ModIdentifier.identifier("all_effects_" + effectIdentifier.getPath()),
                new AllEffectsMixson(effectIdentifier));
    }

    /**
     * Register a monster spawn entry for a biome.
     *
     * @param biome Biome registry key to add a spawn entry for.
     * @param group Spawn group to register spawn entry with.
     * @param weight Weight of the spawn entry to register.
     * @param entry Spawn entry to add in the specified biome.
     */
    public static void registerMonsterSpawnEntry(ResourceKey<Biome> biome, MobCategory group,
                                                 int weight, MobSpawnSettings.SpawnerData entry) {
        String name = ModIdentifier.identifier(String.join("_", "biome_spawn_entry",
                biome.identifier().getPath(), entry.type().toShortString()));

        registerMixsonEvent(biome, name,
                new BiomeSpawnEntryMixson(group, weight, entry));
    }

    /**
     * Register an abandoned camp secret map.
     *
     * @param structure Structure tag to create map item with.
     * @param decoration Map decoration to use on map item.
     * @param item Item to use as base for map item.
     */
    public static void registerAbandonedCampSecretMap(TagKey<Structure> structure,
                                                      Holder<MapDecorationType> decoration,
                                                      Item item) {
        String name = ModIdentifier.identifier(String.join("_", "abandoned_camp_secret_map",
                        decoration.value().assetId().getPath()));

        registerMixsonEvent(BuiltInLootTables.ABANDONED_CAMP_SECRET_CHEST, name,
                new AbandonedCampSecretChestMapMixson(structure, decoration, item));
    }

    /**
     * Register a mixson event to change json file data.
     *
     * @param resource Resource key to change data for.
     * @param name Name of the mixson event.
     * @param event Mixson event to specify the change to make in the file data.
     */
    public static void registerMixsonEvent(ResourceKey<?> resource, String name, Event<JsonElement> event) {
        registerMixsonEvent(List.of(createIndex(resource)), name, event);
    }

    /**
     * Register a mixson event to change json file data.
     *
     * @param resource List of indexes, which reference to files, to change.
     * @param name Name of the mixson event.
     * @param event Mixson event to specify the change to make in the file data.
     */
    public static void registerMixsonEvent(List<Index> resource, String name, Event<JsonElement> event) {
        Mixson.registerEvent(Mixson.DEFAULT_PRIORITY, Lifetime.ONCE, getErrorPolicy(), name, resource::contains, event);
    }

    private static ErrorPolicy getErrorPolicy() {
        return SharedConstants.IS_RUNNING_IN_IDE ?
                ErrorPolicy.THROW :
                ErrorPolicy.LOG;
    }

    /**
     * Create a mixson index from a resource key.
     *
     * @param resourceKey Resource key to create index for.
     * @return Index for the specified resource key.
     */
    private static Index createIndex(ResourceKey<?> resourceKey) {
        String resourcePrefixPath = Registries.elementsDirPath(resourceKey.registryKey());
        Identifier identifier = resourceKey.identifier().withPrefix(resourcePrefixPath + "/");
        return new Index(identifier);
    }
}

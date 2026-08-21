package io.github.pistonpoek.magicalscepter.datagen.loot;

import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.loot.ModLootTables;
import io.github.pistonpoek.magicalscepter.loot.function.SetScepterLootFunction;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableSubProvider;
import net.minecraft.advancements.predicates.LocationPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.StructureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.VillagerTrades;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.UniformContainerBase;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jspecify.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * Mod data generator for chest loot tables.
 *
 * @see net.minecraft.data.loot.packs.VanillaChestLoot
 */
public class ModChestLootProvider extends SimpleFabricLootTableSubProvider {
    private final CompletableFuture<HolderLookup.Provider> registriesFuture;
    private HolderLookup.RegistryLookup<Scepter> scepters() { return lookupOrThrow(ModRegistryKeys.SCEPTER); }
    private HolderLookup.RegistryLookup<Structure> structures() { return lookupOrThrow(Registries.STRUCTURE); }

    /**
     * Construct a mod chest loot table provider for data generation.
     *
     * @param output           Data output to generate chest loot table data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ModChestLootProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture, LootContextParamSets.CHEST);
        this.registriesFuture = registriesFuture;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
        consumer.accept(
                ModLootTables.OLD_TAIGA_CABIN_CHEST,
                LootTable.lootTable()
                        .pool(LootPool.lootPool()
                                .add(LootItem.lootTableItem(ModItems.SCEPTER).setWeight(5))
                                .add(LootItem.lootTableItem(ModItems.MAGICAL_SCEPTER).setWeight(2)
                                        .apply(SetScepterLootFunction.builder(
                                                scepters().getOrThrow(Scepters.MAGICAL_KEY)
                                        ))
                                )
                                .build()
                        )
                        .pool(LootPool.lootPool()
                                .add(LootItem.lootTableItem(Items.BROWN_MUSHROOM))
                                .add(LootItem.lootTableItem(Items.LAPIS_LAZULI))
                                .add(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE))
                                .setRolls(UniformGenerator.between(6.0f, 9.0f))
                                .build()
                        )
                        .pool(LootPool.lootPool()
                                .add(LootItem.lootTableItem(Items.ROTTEN_FLESH)
                                        .apply(SetItemCountFunction.setCount(
                                                UniformGenerator.between(2.0f, 5.0f)
                                        ))
                                )
                                .add(LootItem.lootTableItem(Items.BONE)
                                        .apply(SetItemCountFunction.setCount(
                                                UniformGenerator.between(1.0f, 4.0f)
                                        ))
                                )
                                .add(LootItem.lootTableItem(Items.STRING)
                                        .apply(SetItemCountFunction.setCount(
                                                UniformGenerator.between(1.0f, 4.0f)
                                        ))
                                )
                                .add(LootItem.lootTableItem(Items.GUNPOWDER)
                                        .apply(SetItemCountFunction.setCount(
                                                UniformGenerator.between(1.0f, 2.0f)
                                        ))
                                )
                                .build()
                        )
                        .pool(LootPool.lootPool()
                                .add(LootItem.lootTableItem(Items.SWEET_BERRIES).setWeight(3)
                                        .apply(SetItemCountFunction.setCount(
                                                UniformGenerator.between(2.0f, 5.0f)
                                        ))
                                )
                                .add(LootItem.lootTableItem(Items.BREAD).setWeight(3))
                                .add(LootItem.lootTableItem(Items.WHEAT).setWeight(2)
                                        .apply(SetItemCountFunction.setCount(
                                                UniformGenerator.between(1.0f, 4.0f)
                                        ))
                                )
                                .build()
                        )
                        .pool(LootPool.lootPool()
                                .add(LootItem.lootTableItem(Items.EMERALD).setWeight(18)
                                        .apply(SetItemCountFunction.setCount(
                                                UniformGenerator.between(1.0f, 4.0f)
                                        ))
                                )
                                .add(this.explorationMapItem(
                                        StructureTags.ON_WOODLAND_EXPLORER_MAPS,
                                        MapDecorationTypes.WOODLAND_MANSION,
                                        Items.WOODLAND_EXPLORER_MAP
                                ))
                                .add(this.explorationMapItem(
                                        StructureTags.ON_TRIAL_CHAMBERS_MAPS,
                                        MapDecorationTypes.TRIAL_CHAMBERS,
                                        Items.TRIAL_EXPLORER_MAP
                                ))
                                .add(this.explorationMapItem(
                                        StructureTags.ON_OCEAN_EXPLORER_MAPS,
                                        MapDecorationTypes.OCEAN_MONUMENT,
                                        Items.OCEAN_EXPLORER_MAP
                                ))
                                .add(LootItem.lootTableItem(Items.COAL).setWeight(3)
                                        .apply(SetItemCountFunction.setCount(
                                                UniformGenerator.between(1.0f, 4.0f)
                                        ))
                                )
                                .build()
                        )
        );
    }

    private UniformContainerBase.Builder<?> explorationMapItem(
            final TagKey<Structure> structureTag, final Holder<MapDecorationType> decoration, final Item mapItem
    ) {
        return this.explorationMapItem(structureTag, decoration, mapItem, null);
    }

    private UniformContainerBase.Builder<?> explorationMapItem(
            final TagKey<Structure> structureTag, final Holder<MapDecorationType> decoration, final Item mapItem, final @Nullable String translationKey
    ) {
        UniformContainerBase.Builder<?> entry = LootItem.lootTableItem(mapItem).setWeight(1);
        if (translationKey != null) {
            entry = entry.apply(SetNameFunction.setName(Component.translatable(translationKey), SetNameFunction.Target.ITEM_NAME));
        }
        return entry.apply(
                        ExplorationMapFunction.makeExplorationMap(structures().getOrThrow(structureTag)).setMapDecoration(decoration).setSkipKnownStructures(true)
                )
                .apply(discardIfNotValidMap());
    }

    private UniformContainerBase.Builder<?> explorationMapItemExcludingBiome(
            final TagKey<Structure> structureTag,
            final Holder<MapDecorationType> decoration,
            final Item mapItem,
            final String translationKey,
            final Holder<Biome> excludedBiome
    ) {
        return this.explorationMapItem(structureTag, decoration, mapItem, translationKey)
                .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(excludedBiome))).invert());
    }

    private UniformContainerBase.Builder<?> buriedTreasureMapItem() {
        return LootItem.lootTableItem(Items.BURIED_TREASURE_MAP)
                .apply(
                        ExplorationMapFunction.makeExplorationMap(structures().getOrThrow(StructureTags.ON_TREASURE_MAPS))
                                .setMapDecoration(MapDecorationTypes.RED_X)
                                .setZoom((byte)1)
                                .setSkipKnownStructures(false)
                )
                .apply(discardIfNotValidMap());
    }

    private static FilteredFunction.Builder discardIfNotValidMap() {
        return FilteredFunction.filtered(VillagerTrades.anyValidMap().build()).onFail(DiscardItem.discardItem().build());
    }

    @Override
    public void run() {
        // Intentionally empty.
        // Fabric uses generate() for data generation.
    }

    /**
     * Get registry lookup from registries for data generation.
     *
     * @param registryKey Registry key to get lookup for.
     * @return Holder lookup that can be used to get registry entries.
     */
    private <T> HolderLookup.RegistryLookup<T> lookupOrThrow(ResourceKey<Registry<T>> registryKey) {
        return registriesFuture.join().lookupOrThrow(registryKey);
    }
}
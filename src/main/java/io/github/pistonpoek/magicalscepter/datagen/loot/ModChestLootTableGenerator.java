package io.github.pistonpoek.magicalscepter.datagen.loot;

import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.loot.ModLootTables;
import io.github.pistonpoek.magicalscepter.loot.function.SetScepterLootFunction;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import java.util.function.BiConsumer;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetNameFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

/**
 * Mod data generator for chest loot tables.
 */
public record ModChestLootTableGenerator(HolderLookup.Provider registries) implements LootTableSubProvider {
    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
        consumer.accept(
                ModLootTables.OLD_TAIGA_CABIN_CHEST,
                LootTable.lootTable()
                        .pool(LootPool.lootPool()
                                .add(LootItem.lootTableItem(ModItems.SCEPTER).setWeight(5))
                                .add(LootItem.lootTableItem(ModItems.MAGICAL_SCEPTER).setWeight(2)
                                        .apply(SetScepterLootFunction.builder(registries(), Scepters.MAGICAL_KEY))
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
                                .add(LootItem.lootTableItem(Items.MAP)
                                        .apply(ExplorationMapFunction.makeExplorationMap()
                                                .setMapDecoration(MapDecorationTypes.WOODLAND_MANSION)
                                                .setDestination(StructureTags.ON_WOODLAND_EXPLORER_MAPS)
                                                .setSkipKnownStructures(false)
                                        )
                                        .apply(SetNameFunction.setName(
                                                Component.translatable("filled_map.mansion"),
                                                SetNameFunction.Target.ITEM_NAME
                                        ))
                                )
                                .add(LootItem.lootTableItem(Items.MAP)
                                        .apply(ExplorationMapFunction.makeExplorationMap()
                                                .setMapDecoration(MapDecorationTypes.TRIAL_CHAMBERS)
                                                .setDestination(StructureTags.ON_TRIAL_CHAMBERS_MAPS)
                                                .setSkipKnownStructures(false)
                                        )
                                        .apply(SetNameFunction.setName(
                                                Component.translatable("filled_map.trial_chambers"),
                                                SetNameFunction.Target.ITEM_NAME
                                        ))
                                )
                                .add(LootItem.lootTableItem(Items.MAP)
                                        .apply(ExplorationMapFunction.makeExplorationMap()
                                                .setMapDecoration(MapDecorationTypes.OCEAN_MONUMENT)
                                                .setDestination(StructureTags.ON_OCEAN_EXPLORER_MAPS)
                                                .setSkipKnownStructures(false)
                                        )
                                        .apply(SetNameFunction.setName(
                                                Component.translatable("filled_map.monument"),
                                                SetNameFunction.Target.ITEM_NAME
                                        ))
                                )
                                .add(LootItem.lootTableItem(Items.COAL).setWeight(3)
                                        .apply(SetItemCountFunction.setCount(
                                                UniformGenerator.between(1.0f, 4.0f)
                                        ))
                                )
                                .build()
                        )
        );
    }
}

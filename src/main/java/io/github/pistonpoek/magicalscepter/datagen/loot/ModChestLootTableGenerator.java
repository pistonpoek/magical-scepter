package io.github.pistonpoek.magicalscepter.datagen.loot;

import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.loot.ModLootTables;
import io.github.pistonpoek.magicalscepter.loot.function.SetScepterLootFunction;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import net.minecraft.data.loottable.LootTableGenerator;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapDecorationTypes;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ExplorationMapLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetNameLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.text.Text;

import java.util.function.BiConsumer;

/**
 * Mod data generator for chest loot tables.
 */
public record ModChestLootTableGenerator(RegistryWrapper.WrapperLookup registries) implements LootTableGenerator {
    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> consumer) {
        consumer.accept(
                ModLootTables.OLD_TAIGA_CABIN_CHEST,
                LootTable.builder()
                        .pool(LootPool.builder()
                                .with(ItemEntry.builder(ModItems.SCEPTER).weight(5))
                                .with(ItemEntry.builder(ModItems.MAGICAL_SCEPTER).weight(2)
                                        .apply(SetScepterLootFunction.builder(registries(), Scepters.MAGICAL_KEY))
                                )
                                .build()
                        )
                        .pool(LootPool.builder()
                                .with(ItemEntry.builder(Items.BROWN_MUSHROOM))
                                .with(ItemEntry.builder(Items.LAPIS_LAZULI))
                                .with(ItemEntry.builder(Items.EXPERIENCE_BOTTLE))
                                .rolls(UniformLootNumberProvider.create(6.0f, 9.0f))
                                .build()
                        )
                        .pool(LootPool.builder()
                                .with(ItemEntry.builder(Items.ROTTEN_FLESH)
                                        .apply(SetCountLootFunction.builder(
                                                UniformLootNumberProvider.create(2.0f, 5.0f)
                                        ))
                                )
                                .with(ItemEntry.builder(Items.BONE)
                                        .apply(SetCountLootFunction.builder(
                                                UniformLootNumberProvider.create(1.0f, 4.0f)
                                        ))
                                )
                                .with(ItemEntry.builder(Items.STRING)
                                        .apply(SetCountLootFunction.builder(
                                                UniformLootNumberProvider.create(1.0f, 4.0f)
                                        ))
                                )
                                .with(ItemEntry.builder(Items.GUNPOWDER)
                                        .apply(SetCountLootFunction.builder(
                                                UniformLootNumberProvider.create(1.0f, 2.0f)
                                        ))
                                )
                                .build()
                        )
                        .pool(LootPool.builder()
                                .with(ItemEntry.builder(Items.SWEET_BERRIES).weight(3)
                                        .apply(SetCountLootFunction.builder(
                                                UniformLootNumberProvider.create(2.0f, 5.0f)
                                        ))
                                )
                                .with(ItemEntry.builder(Items.BREAD).weight(3))
                                .with(ItemEntry.builder(Items.WHEAT).weight(2)
                                        .apply(SetCountLootFunction.builder(
                                                UniformLootNumberProvider.create(1.0f, 4.0f)
                                        ))
                                )
                                .build()
                        )
                        .pool(LootPool.builder()
                                .with(ItemEntry.builder(Items.EMERALD).weight(18)
                                        .apply(SetCountLootFunction.builder(
                                                UniformLootNumberProvider.create(1.0f, 4.0f)
                                        ))
                                )
                                .with(ItemEntry.builder(Items.MAP)
                                        .apply(ExplorationMapLootFunction.builder()
                                                .withDecoration(MapDecorationTypes.MANSION)
                                                .withDestination(StructureTags.ON_WOODLAND_EXPLORER_MAPS)
                                                .withSkipExistingChunks(false)
                                        )
                                        .apply(SetNameLootFunction.builder(
                                                Text.translatable("filled_map.mansion"),
                                                SetNameLootFunction.Target.ITEM_NAME
                                        ))
                                )
                                .with(ItemEntry.builder(Items.MAP)
                                        .apply(ExplorationMapLootFunction.builder()
                                                .withDecoration(MapDecorationTypes.TRIAL_CHAMBERS)
                                                .withDestination(StructureTags.ON_TRIAL_CHAMBERS_MAPS)
                                                .withSkipExistingChunks(false)
                                        )
                                        .apply(SetNameLootFunction.builder(
                                                Text.translatable("filled_map.trial_chambers"),
                                                SetNameLootFunction.Target.ITEM_NAME
                                        ))
                                )
                                .with(ItemEntry.builder(Items.MAP)
                                        .apply(ExplorationMapLootFunction.builder()
                                                .withDecoration(MapDecorationTypes.MONUMENT)
                                                .withDestination(StructureTags.ON_OCEAN_EXPLORER_MAPS)
                                                .withSkipExistingChunks(false)
                                        )
                                        .apply(SetNameLootFunction.builder(
                                                Text.translatable("filled_map.monument"),
                                                SetNameLootFunction.Target.ITEM_NAME
                                        ))
                                )
                                .with(ItemEntry.builder(Items.COAL).weight(3)
                                        .apply(SetCountLootFunction.builder(
                                                UniformLootNumberProvider.create(1.0f, 4.0f)
                                        ))
                                )
                                .build()
                        )
        );
    }
}

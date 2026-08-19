package io.github.pistonpoek.magicalscepter.datagen.loot;

import io.github.pistonpoek.magicalscepter.entity.ModEntityType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricEntityLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import java.util.concurrent.CompletableFuture;

/**
 * Mod data provider for entity loot tables.
 */
public class ModEntityLootTableProvider extends FabricEntityLootTableProvider {
    /**
     * Construct a mod entity loot table provider for data generation.
     *
     * @param output           Data output to generate entity loot table data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ModEntityLootTableProvider(FabricDataOutput output,
                                      CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate() {
        add(ModEntityType.SORCERER,
                LootTable.lootTable()
                        .pool(LootPool.lootPool()
                                .add(LootItem.lootTableItem(Items.BROWN_MUSHROOM)
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries,
                                                UniformGenerator.between(0.0f, 1.0f))
                                        ))
                                .setRolls(UniformGenerator.between(0.0f, 1.0f))
                                .build()
                        )
                        .pool(LootPool.lootPool()
                                .add(LootItem.lootTableItem(Items.LAPIS_LAZULI)
                                        .apply(SetItemCountFunction.setCount(
                                                UniformGenerator.between(4.0f, 8.0f)
                                        ))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries,
                                                UniformGenerator.between(0.0f, 1.0f)
                                        ))
                                ).build()
                        )
        );
    }
}

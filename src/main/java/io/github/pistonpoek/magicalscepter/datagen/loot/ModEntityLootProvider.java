package io.github.pistonpoek.magicalscepter.datagen.loot;

import io.github.pistonpoek.magicalscepter.entity.ModEntityTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricEntityLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import java.util.concurrent.CompletableFuture;

/**
 * Mod data provider for entity loot tables.
 *
 * @see net.minecraft.data.loot.packs.VanillaEntityLoot
 */
public class ModEntityLootProvider extends FabricEntityLootSubProvider {
    /**
     * Construct a mod entity loot table provider for data generation.
     *
     * @param output           Data output to generate entity loot table data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ModEntityLootProvider(
            FabricPackOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture
    ) {
        super(output, registriesFuture);
    }

    @Override
    public void generate() {
        add(ModEntityTypes.SORCERER,
                LootTable.lootTable()
                        .pool(LootPool.lootPool()
                                .add(LootItem.lootTableItem(Items.BROWN_MUSHROOM)
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(enchantments,
                                                ContextFloatProviders.between(0.0f, 1.0f))
                                        ))
                                .setRolls(ContextIntProviders.between(0, 1))
                                .build()
                        )
                        .pool(LootPool.lootPool()
                                .add(LootItem.lootTableItem(Items.LAPIS_LAZULI)
                                        .apply(SetItemCountFunction.setCount(
                                                ContextIntProviders.between(4, 8)
                                        ))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(enchantments,
                                                ContextFloatProviders.between(0.0f, 1.0f)
                                        ))
                                ).build()
                        )
        );
    }
}

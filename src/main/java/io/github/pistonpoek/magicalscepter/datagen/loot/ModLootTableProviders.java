package io.github.pistonpoek.magicalscepter.datagen.loot;

import io.github.pistonpoek.magicalscepter.loot.ModLootTables;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Mod data provider that contains generic generators for loot tables.
 */
public class ModLootTableProviders extends LootTableProvider {
    /**
     * Construct a mod loot table provider for data generation.
     *
     * @param output           Data output to generate loot table data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ModLootTableProviders(FabricPackOutput output,
                                 CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, ModLootTables.getAll(), getModLootTables(), registriesFuture);
    }

    /**
     * Get the list of generators for loot tables to provide in the mod.
     *
     * @return List of loot table generators.
     */
    private static List<LootTableProvider.SubProviderEntry> getModLootTables() {
        return List.of(
                new LootTableProvider.SubProviderEntry(ModChestLootTableGenerator::new, LootContextParamSets.CHEST)
        );
    }
}

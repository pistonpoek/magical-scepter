package io.github.pistonpoek.magicalscepter.datagen.loot;

import io.github.pistonpoek.magicalscepter.loot.ModLootTables;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.loottable.LootTableProvider;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.RegistryWrapper;

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
    public ModLootTableProviders(FabricDataOutput output,
                                 CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, ModLootTables.getAll(), getModLootTables(), registriesFuture);
    }

    /**
     * Get the list of generators for loot tables to provide in the mod.
     *
     * @return List of loot table generators.
     */
    private static List<LootTableProvider.LootTypeGenerator> getModLootTables() {
        return List.of(
                new LootTableProvider.LootTypeGenerator(ModChestLootTableGenerator::new, LootContextTypes.CHEST)
        );
    }
}

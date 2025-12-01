package io.github.pistonpoek.magicalscepter.datagen.loot;

import io.github.pistonpoek.magicalscepter.loot.ModLootTables;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.loottable.LootTableProvider;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.RegistryWrapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModLootTableProviders extends LootTableProvider {
    public ModLootTableProviders(FabricDataOutput output,
                                 CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, ModLootTables.getAll(), getModLootTables(), registriesFuture);
    }

    private static List<LootTableProvider.LootTypeGenerator> getModLootTables() {
        return List.of(
                new LootTableProvider.LootTypeGenerator(ModChestLootTableProvider::new, LootContextTypes.CHEST)
        );
    }
}

package io.github.pistonpoek.magicalscepter.datagen.loot;

import io.github.pistonpoek.magicalscepter.entity.ModEntityType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricEntityLootTableProvider;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantedCountIncreaseLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModEntityLootTableProvider extends FabricEntityLootTableProvider {
    public ModEntityLootTableProvider(FabricDataOutput output,
                                      CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate() {
        register(ModEntityType.SORCERER,
                LootTable.builder()
                        .pool(LootPool.builder()
                                .with(ItemEntry.builder(Items.BROWN_MUSHROOM)
                                        .apply(EnchantedCountIncreaseLootFunction.builder(registries,
                                                UniformLootNumberProvider.create(0.0f, 1.0f))
                                        ))
                                .rolls(UniformLootNumberProvider.create(0.0f, 1.0f))
                                .build()
                        )
                        .pool(LootPool.builder()
                                .with(ItemEntry.builder(Items.LAPIS_LAZULI)
                                        .apply(SetCountLootFunction.builder(
                                                UniformLootNumberProvider.create(4.0f, 8.0f)
                                        ))
                                        .apply(EnchantedCountIncreaseLootFunction.builder(registries,
                                                UniformLootNumberProvider.create(0.0f, 1.0f)
                                        ))
                                ).build()
                        )
        );
    }
}

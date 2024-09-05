package net.pistonpoek.magical_scepter.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.pistonpoek.magical_scepter.MagicalScepter;
import net.pistonpoek.magical_scepter.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.SCEPTER)
                .input(Items.BROWN_MUSHROOM).input(Items.RED_MUSHROOM).input(ModItems.EMPTY_SCEPTER).input(Items.LAPIS_LAZULI)
                .criterion(FabricRecipeProvider.hasItem(ModItems.EMPTY_SCEPTER),
                FabricRecipeProvider.conditionsFromItem(ModItems.EMPTY_SCEPTER)).offerTo(exporter);
    }
}
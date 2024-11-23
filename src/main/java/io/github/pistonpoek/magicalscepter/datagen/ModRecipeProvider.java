package io.github.pistonpoek.magicalscepter.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.recipe.MagicalScepterRecipe;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        Identifier recipeId = ModIdentifier.of("magical_scepter");
        RecipeCategory category = RecipeCategory.COMBAT;
        exporter.accept(recipeId,
                new MagicalScepterRecipe(CraftingRecipeJsonBuilder.toCraftingCategory(category)),
                exporter.getAdvancementBuilder()
                        .criterion("has_scepter", conditionsFromPredicates(ItemPredicate.Builder.create().items(ModItems.SCEPTER)))
                        .criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId))
                        .rewards(AdvancementRewards.Builder.recipe(recipeId))
                        .criteriaMerger(AdvancementRequirements.CriterionMerger.OR)
                        .build(recipeId.withPrefixedPath("recipes/" + category.getName() + "/")));
    }
}
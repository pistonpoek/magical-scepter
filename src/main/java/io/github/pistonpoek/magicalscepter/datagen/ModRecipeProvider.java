package io.github.pistonpoek.magicalscepter.datagen;

import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.recipe.MagicalScepterRecipe;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;

import java.util.concurrent.CompletableFuture;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.data.recipe.RecipeGenerator.RecipeProvider
 */
public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
        return new RecipeGenerator(registries, exporter) {
            @Override
            public void generate() {
                Identifier recipeId = ModIdentifier.of("magical_scepter");
                RegistryKey<Recipe<?>> recipeRegistryKey = RegistryKey.of(RegistryKeys.RECIPE, recipeId);
                RecipeCategory category = RecipeCategory.COMBAT;

                RegistryWrapper<Scepter> scepterRegistry = registries.getOrThrow(ModRegistryKeys.SCEPTER);
                RegistryEntry<Scepter> magicalScepter = scepterRegistry.getOrThrow(Scepters.MAGICAL_KEY);

                exporter.accept(recipeRegistryKey,
                        new MagicalScepterRecipe(magicalScepter, CraftingRecipeJsonBuilder.toCraftingCategory(category)),
                        exporter.getAdvancementBuilder()
                                .criterion("has_scepter", this.conditionsFromItem(ModItems.SCEPTER))
                                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeRegistryKey))
                                .rewards(AdvancementRewards.Builder.recipe(recipeRegistryKey))
                                .criteriaMerger(AdvancementRequirements.CriterionMerger.OR)
                                .build(recipeId.withPrefixedPath("recipes/" + category.getName() + "/")));
            }
        };
    }

    @Override
    public String getName() {
        return "ModRecipeProvider";
    }
}
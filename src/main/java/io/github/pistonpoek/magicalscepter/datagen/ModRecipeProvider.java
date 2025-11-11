package io.github.pistonpoek.magicalscepter.datagen;

import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.recipe.ExperienceBottleRecipe;
import io.github.pistonpoek.magicalscepter.recipe.MagicalScepterRecipe;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.TransmuteRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.data.recipe.RecipeGenerator.RecipeProvider
 */
public class ModRecipeProvider extends FabricRecipeProvider {
    /**
     * Construct a mod recipe provider for data generation.
     *
     * @param output           Data output to generate recipe data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
        return new RecipeGenerator(registries, exporter) {
            @Override
            public void generate() {
                RegistryWrapper<Scepter> scepterRegistry = registries.getOrThrow(ModRegistryKeys.SCEPTER);
                RegistryEntry<Scepter> magicalScepter = scepterRegistry.getOrThrow(Scepters.MAGICAL_KEY);

                exportRecipe("magical_scepter", category ->
                                new MagicalScepterRecipe(magicalScepter, category), RecipeCategory.COMBAT,
                        exporter, exporter.getAdvancementBuilder()
                                .criterion("has_scepter", this.conditionsFromItem(ModItems.SCEPTER))
                );

                TransmuteRecipeJsonBuilder.create(
                                RecipeCategory.TOOLS, Ingredient.ofItem(ModItems.SCEPTER),
                                Ingredient.ofItem(Items.LAPIS_LAZULI), ModItems.ARCANE_SCEPTER
                        )
                        .criterion("has_scepter", this.conditionsFromItem(ModItems.SCEPTER))
                        .offerTo(this.exporter);

                exportRecipe("experience_bottle", ExperienceBottleRecipe::new, RecipeCategory.MISC,
                        exporter, exporter.getAdvancementBuilder()
                                .criterion("has_arcane_scepter", this.conditionsFromItem(ModItems.ARCANE_SCEPTER))
                );

            }
        };
    }

    @Override
    public String getName() {
        return "ModRecipeProvider";
    }

    /**
     * Get the mod recipe registry key for the specified string identifier.
     *
     * @param id String identifier to get mod registry key for.
     * @return Registry key of the mod recipe for the specified identifier.
     */
    public static RegistryKey<Recipe<?>> getRecipeRegistryKey(String id) {
        return RegistryKey.of(RegistryKeys.RECIPE, ModIdentifier.of(id));
    }

    /**
     * Export a crafting recipe with a standard recipe advancement.
     *
     * @param id                 String identifier for the recipe.
     * @param recipe             Factory to create the recipe from the category.
     * @param category           Recipe category for the recipe.
     * @param exporter           Recipe exporter to use.
     * @param advancementBuilder Advancement builder of the exporter that may specify recipe obtainment criteria.
     */
    public static void exportRecipe(String id,
                                    Function<CraftingRecipeCategory, CraftingRecipe> recipe,
                                    RecipeCategory category,
                                    RecipeExporter exporter,
                                    Advancement.Builder advancementBuilder
    ) {
        RegistryKey<Recipe<?>> recipeRegistryKey = getRecipeRegistryKey(id);
        exporter.accept(recipeRegistryKey,
                recipe.apply(CraftingRecipeJsonBuilder.toCraftingCategory(category)),
                advancementBuilder
                        .criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeRegistryKey))
                        .rewards(AdvancementRewards.Builder.recipe(recipeRegistryKey))
                        .criteriaMerger(AdvancementRequirements.CriterionMerger.OR)
                        .build(ModIdentifier.of(id).withPrefixedPath("recipes/" + category.getName() + "/"))
        );
    }

}
package io.github.pistonpoek.magicalscepter.datagen;

import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.recipe.ExperienceBottleRecipe;
import io.github.pistonpoek.magicalscepter.recipe.MagicalScepterRecipe;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.TransmuteRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.data.recipes.RecipeProvider.Runner
 */
public class ModRecipeProvider extends FabricRecipeProvider {
    /**
     * Construct a mod recipe provider for data generation.
     *
     * @param output           Data output to generate recipe data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ModRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput exporter) {
        return new RecipeProvider(registries, exporter) {
            @Override
            public void buildRecipes() {
                HolderLookup<Scepter> scepterRegistry = registries.lookupOrThrow(ModRegistryKeys.SCEPTER);
                Holder<Scepter> magicalScepter = scepterRegistry.getOrThrow(Scepters.MAGICAL_KEY);

                exportRecipe("magical_scepter", _ ->
                                new MagicalScepterRecipe(magicalScepter), RecipeCategory.COMBAT,
                        output, output.advancement()
                                .addCriterion("has_scepter", this.has(ModItems.SCEPTER))
                );

                TransmuteRecipeBuilder.transmute(
                                RecipeCategory.TOOLS, Ingredient.of(ModItems.SCEPTER),
                                Ingredient.of(Items.LAPIS_LAZULI), ModItems.ARCANE_SCEPTER
                        )
                        .unlockedBy("has_scepter", this.has(ModItems.SCEPTER))
                        .save(this.output);

                exportRecipe("experience_bottle", _ -> new ExperienceBottleRecipe(), RecipeCategory.MISC,
                        output, output.advancement()
                                .addCriterion("has_arcane_scepter", this.has(ModItems.ARCANE_SCEPTER))
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
    public static ResourceKey<Recipe<?>> getRecipeRegistryKey(String id) {
        return ResourceKey.create(Registries.RECIPE, ModIdentifier.of(id));
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
                                    Function<CraftingBookCategory, CraftingRecipe> recipe,
                                    RecipeCategory category,
                                    RecipeOutput exporter,
                                    Advancement.Builder advancementBuilder
    ) {
        ResourceKey<Recipe<?>> recipeRegistryKey = getRecipeRegistryKey(id);
        exporter.accept(recipeRegistryKey,
                recipe.apply(RecipeBuilder.determineCraftingBookCategory(category)),
                advancementBuilder
                        .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeRegistryKey))
                        .rewards(AdvancementRewards.Builder.recipe(recipeRegistryKey))
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(ModIdentifier.of(id).withPrefix("recipes/" + category.getFolderName() + "/"))
        );
    }

}
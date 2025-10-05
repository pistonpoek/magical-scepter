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
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.recipe.*;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

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
                Identifier magicalScepterRecipeId = ModIdentifier.of("magical_scepter");
                RegistryKey<Recipe<?>> magicalScepterRecipeRegistryKey =
                        RegistryKey.of(RegistryKeys.RECIPE, magicalScepterRecipeId);
                RecipeCategory category = RecipeCategory.COMBAT;

                RegistryWrapper<Scepter> scepterRegistry = registries.getOrThrow(ModRegistryKeys.SCEPTER);
                RegistryEntry<Scepter> magicalScepter = scepterRegistry.getOrThrow(Scepters.MAGICAL_KEY);

                exporter.accept(magicalScepterRecipeRegistryKey,
                        new MagicalScepterRecipe(magicalScepter, CraftingRecipeJsonBuilder.toCraftingCategory(category)),
                        exporter.getAdvancementBuilder()
                                .criterion("has_scepter", this.conditionsFromItem(ModItems.SCEPTER))
                                .criterion("has_the_recipe",
                                        RecipeUnlockedCriterion.create(magicalScepterRecipeRegistryKey))
                                .rewards(AdvancementRewards.Builder.recipe(magicalScepterRecipeRegistryKey))
                                .criteriaMerger(AdvancementRequirements.CriterionMerger.OR)
                                .build(magicalScepterRecipeId.withPrefixedPath("recipes/" + category.getName() + "/")));


                TransmuteRecipeJsonBuilder.create(
                                RecipeCategory.TOOLS, Ingredient.ofItem(ModItems.SCEPTER),
                                Ingredient.ofItem(Items.LAPIS_LAZULI), ModItems.ARCANE_SCEPTER
                        )
                        .criterion("has_scepter", this.conditionsFromItem(ModItems.SCEPTER))
                        .offerTo(this.exporter);

                ComplexRecipeJsonBuilder.create(ExperienceBottleRecipe::new).offerTo(exporter,
                        RegistryKey.of(RegistryKeys.RECIPE, ModIdentifier.of("experience_bottle")));
            }
        };
    }

    @Override
    public String getName() {
        return "ModRecipeProvider";
    }
}
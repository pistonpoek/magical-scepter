package io.github.pistonpoek.magicalscepter.datagen;

import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.recipe.ExperienceBottleRecipe;
import io.github.pistonpoek.magicalscepter.recipe.MagicalScepterRecipe;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.MultiRegistryBootstrap;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.data.recipes.TransmuteRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Mod recipe provider wrapper to allow fabric to generate the recipes.
 *
 * @see Provider
 */
public class ModRecipeProvider extends FabricRecipeProvider {
    /**
     * Construct a mod recipe provider for data generation.
     *
     * @param output           Data output to generate recipe data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ModRecipeProvider(
            FabricPackOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(
            HolderLookup.Provider registries,
            BootstrapContext<Recipe<?>> recipes,
            BootstrapContext<Advancement> advancements
    ) {
        return new Provider(recipes, advancements);
    }

    /**
     * Mod specific class that provides similar functionality to respective vanilla class.
     *
     * @see net.minecraft.data.recipes.packs.VanillaRecipeProvider
     */
    public static class Provider extends RecipeProvider {
        private final HolderGetter<Scepter> scepters;

        protected Provider(BootstrapContext<Recipe<?>> recipeOutput, BootstrapContext<Advancement> advancementOutput) {
            super(recipeOutput, advancementOutput);
            this.scepters = recipeOutput.lookup(ModRegistryKeys.SCEPTER);
        }

        public static MultiRegistryBootstrap create() {
            return new MultiRegistryBootstrap() {
                @Override
                public Set<ResourceKey<? extends Registry<?>>> requestedRegistries() {
                    return Set.of(Registries.RECIPE, Registries.ADVANCEMENT);
                }

                @Override
                public void run(final MultiRegistryBootstrap.BootstrapGetter registries) {
                    new Provider(registries.get(Registries.RECIPE), registries.get(Registries.ADVANCEMENT)).buildRecipes();
                }
            };
        }

        @Override
        public void buildRecipes() {
            Holder<Scepter> magicalScepter = scepters.getOrThrow(Scepters.MAGICAL_KEY);

            SpecialRecipeBuilder.special(() -> new MagicalScepterRecipe(magicalScepter))
                    .unlockedBy("has_scepter", this.has(ModItems.SCEPTER))
                    .save(output, "magical_scepter");

            TransmuteRecipeBuilder.transmute(
                            RecipeCategory.TOOLS, Ingredient.of(ModItems.SCEPTER),
                            Ingredient.of(Items.LAPIS_LAZULI), ModItems.ARCANE_SCEPTER
                    )
                    .unlockedBy("has_scepter", this.has(ModItems.SCEPTER))
                    .save(this.output, "arcane_scepter");

            SpecialRecipeBuilder.special(ExperienceBottleRecipe::new)
                    .unlockedBy("has_arcane_scepter", this.has(ModItems.ARCANE_SCEPTER))
                    .save(output, "experience_bottle");
        }
    }
}
package io.github.pistonpoek.magicalscepter.recipe;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.recipe.RecipeSerializer
 */
public interface ModRecipeSerializer {
    RecipeSerializer<ExperienceBottleRecipe> EXPERIENCE_BOTTLE = register("crafting_experience_bottle",
            new SpecialCraftingRecipe.SpecialRecipeSerializer<>(ExperienceBottleRecipe::new));
    RecipeSerializer<MagicalScepterRecipe> MAGICAL_SCEPTER = register("crafting_magical_scepter",
            new MagicalScepterRecipeSerializer());

    /**
     * Initialize the class for the static fields.
     */
    static void init() {

    }

    /**
     * Register a recipe serializer that can be used by a custom recipe.
     *
     * @param name String name to use for the recipe serializer.
     * @param serializer Serializer to register for the name.
     * @return Recipe serializer that is registered.
     * @param <S> Serializer type to register.
     * @param <T> Recipe type to register serializer for.
     */
    static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String name, S serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, ModIdentifier.of(name), serializer);
    }
}

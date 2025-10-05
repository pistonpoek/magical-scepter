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

    static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String identifier, S serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, ModIdentifier.of(identifier), serializer);
    }
}

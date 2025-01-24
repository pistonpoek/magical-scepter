package io.github.pistonpoek.magicalscepter.recipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.recipe.RecipeSerializer
 */
public interface ModRecipeSerializer {
    RecipeSerializer<MagicalScepterRecipe> MAGICAL_SCEPTER = register("crafting_magical_scepter",
            new MagicalScepterRecipeSerializer());

    /**
     * Initialize the class for the static fields.
     */
    static void init() {

    }

    static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, ModIdentifier.of(id), serializer);
    }
}

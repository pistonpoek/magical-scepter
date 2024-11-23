package io.github.pistonpoek.magicalscepter.recipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;

public interface ModRecipeSerializer {
    RecipeSerializer<MagicalScepterRecipe> MAGICAL_SCEPTER = register("crafting_special_magical_scepter", new SpecialRecipeSerializer<>(MagicalScepterRecipe::new));

    static void init() {

    }

    static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, ModIdentifier.of(id), serializer);
    }
}

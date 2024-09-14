package net.pistonpoek.magicalscepter.recipe;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.pistonpoek.magicalscepter.item.ModItems;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.pistonpoek.magicalscepter.scepter.Scepter;
import net.pistonpoek.magicalscepter.scepter.ScepterHelper;
import net.pistonpoek.magicalscepter.scepter.Scepters;

public class MagicalScepterRecipe extends SpecialCraftingRecipe {
    public MagicalScepterRecipe(CraftingRecipeCategory craftingRecipeCategory) {
        super(craftingRecipeCategory);
    }

    public boolean matches(CraftingRecipeInput craftingRecipeInput, World world) {
        boolean containsLapisLazuli = false;
        boolean containsRedMushroom = false;
        boolean containsBrownMushroom = false;
        boolean containsEmptyScepter = false;

        for (int i = 0; i < craftingRecipeInput.getSize(); i++) {
            ItemStack itemStack = craftingRecipeInput.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                if (itemStack.isOf(Blocks.BROWN_MUSHROOM.asItem()) && !containsBrownMushroom) {
                    containsBrownMushroom = true;
                } else if (itemStack.isOf(Blocks.RED_MUSHROOM.asItem()) && !containsRedMushroom) {
                    containsRedMushroom = true;
                } else if (itemStack.isOf(Items.LAPIS_LAZULI) && !containsLapisLazuli) {
                    containsLapisLazuli = true;
                } else if (ScepterHelper.IS_EMPTY_SCEPTER.test(itemStack) && !containsEmptyScepter) {
                    containsEmptyScepter = true;
                } else {
                    return false;
                }
            }
        }
        return containsLapisLazuli && containsBrownMushroom && containsRedMushroom && containsEmptyScepter;
    }

    public ItemStack craft(CraftingRecipeInput craftingRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup) {
        RegistryEntryLookup<Scepter> scepterLookup = wrapperLookup.createRegistryLookup().getOrThrow(ModRegistryKeys.SCEPTER);
        return ScepterHelper.createScepter(scepterLookup.getOrThrow(Scepters.MAGICAL_KEY));
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return false;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        RegistryEntryLookup<Scepter> scepterLookup = registriesLookup.createRegistryLookup().getOrThrow(ModRegistryKeys.SCEPTER);
        return ScepterHelper.createScepter(scepterLookup.getOrThrow(Scepters.MAGICAL_KEY));
    }

    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> ingredients = DefaultedList.of();
        ingredients.add(Ingredient.ofItems(Items.RED_MUSHROOM));
        ingredients.add(Ingredient.ofItems(Items.BROWN_MUSHROOM));
        ingredients.add(Ingredient.ofItems(Items.LAPIS_LAZULI));
        ingredients.add(Ingredient.ofItems(ModItems.SCEPTER));
        return ingredients;
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 2 && height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
            return ModRecipeSerializer.MAGICAL_SCEPTER;
        }
}

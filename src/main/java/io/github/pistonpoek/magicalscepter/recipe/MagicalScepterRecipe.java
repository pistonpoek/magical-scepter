package io.github.pistonpoek.magicalscepter.recipe;

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
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;

public class MagicalScepterRecipe extends SpecialCraftingRecipe {
    public MagicalScepterRecipe(CraftingRecipeCategory craftingRecipeCategory) {
        super(craftingRecipeCategory);
    }

    public boolean matches(CraftingRecipeInput craftingRecipeInput, World world) {
        boolean containsLapisLazuli = false;
        boolean containsBrownMushroom = false;
        boolean containsScepter = false;

        for (int i = 0; i < craftingRecipeInput.getSize(); i++) {
            ItemStack itemStack = craftingRecipeInput.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                if (itemStack.isOf(ModItems.SCEPTER) && !containsScepter) {
                    containsScepter = true;
                } else if (itemStack.isOf(Blocks.BROWN_MUSHROOM.asItem()) && !containsBrownMushroom) {
                    containsBrownMushroom = true;
                } else if (itemStack.isOf(Items.LAPIS_LAZULI) && !containsLapisLazuli) {
                    containsLapisLazuli = true;
                } else {
                    return false;
                }
            }
        }
        return containsScepter && containsLapisLazuli && containsBrownMushroom;
    }

    public ItemStack craft(CraftingRecipeInput craftingRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup) {
        RegistryEntryLookup<Scepter> scepterLookup = wrapperLookup.createRegistryLookup().getOrThrow(ModRegistryKeys.SCEPTER);
        ItemStack craftedScepter = ModItems.MAGICAL_SCEPTER.getDefaultStack();
        for (int i = 0; i < craftingRecipeInput.getSize(); i++) {
            ItemStack itemStack = craftingRecipeInput.getStackInSlot(i);
            if (itemStack.isOf(ModItems.SCEPTER)) {
                craftedScepter = itemStack.copyComponentsToNewStack(ModItems.MAGICAL_SCEPTER, 1);
                break;
            }
        }
        return ScepterContentsComponent.setScepter(craftedScepter, scepterLookup.getOrThrow(Scepters.MAGICAL_KEY));
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
        ingredients.add(Ingredient.ofItems(ModItems.SCEPTER));
        ingredients.add(Ingredient.ofItems(Items.BROWN_MUSHROOM));
        ingredients.add(Ingredient.ofItems(Items.LAPIS_LAZULI));
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

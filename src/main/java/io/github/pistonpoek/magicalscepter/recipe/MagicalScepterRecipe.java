package io.github.pistonpoek.magicalscepter.recipe;

import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.IngredientPlacement;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;

import java.util.List;

public class MagicalScepterRecipe extends SpecialCraftingRecipe {
    public final RegistryEntry<Scepter> resultScepter;

    public MagicalScepterRecipe(RegistryEntry<Scepter> result, CraftingRecipeCategory craftingRecipeCategory) {
        super(craftingRecipeCategory);
        this.resultScepter = result;
    }

    public boolean matches(CraftingRecipeInput craftingRecipeInput, World world) {
        boolean containsLapisLazuli = false;
        boolean containsBrownMushroom = false;
        boolean containsScepter = false;

        for (int i = 0; i < craftingRecipeInput.size(); i++) {
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

    public ItemStack craft(CraftingRecipeInput craftingRecipeInput, RegistryWrapper.WrapperLookup registries) {
        ItemStack craftedScepter = ModItems.MAGICAL_SCEPTER.getDefaultStack();
        for (int i = 0; i < craftingRecipeInput.size(); i++) {
            ItemStack itemStack = craftingRecipeInput.getStackInSlot(i);
            if (itemStack.isOf(ModItems.SCEPTER)) {
                craftedScepter = itemStack.copyComponentsToNewStack(ModItems.MAGICAL_SCEPTER, 1);
                break;
            }
        }
        return ScepterContentsComponent.setScepter(craftedScepter, resultScepter);
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return false;
    }

    @Override
    public List<RecipeDisplay> getDisplays() {
        return List.of(
                new ShapelessCraftingRecipeDisplay(
                        getIngredientPlacement().getIngredients().stream().map(Ingredient::toDisplay).toList(),
                        new SlotDisplay.StackSlotDisplay(ScepterHelper.createMagicalScepter(resultScepter)),
                        new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)
                )
        );
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        DefaultedList<Ingredient> ingredients = DefaultedList.of();
        ingredients.add(Ingredient.ofItems(ModItems.SCEPTER));
        ingredients.add(Ingredient.ofItems(Items.BROWN_MUSHROOM));
        ingredients.add(Ingredient.ofItems(Items.LAPIS_LAZULI));
        return IngredientPlacement.forShapeless(ingredients);
    }

    @Override
    public RecipeSerializer<MagicalScepterRecipe> getSerializer() {
            return ModRecipeSerializer.MAGICAL_SCEPTER;
        }
}

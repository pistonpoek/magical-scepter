package io.github.pistonpoek.magicalscepter.recipe;

import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
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

import java.util.List;

public class MagicalScepterRecipe extends SpecialCraftingRecipe {
    public final RegistryEntry<Scepter> resultScepter;

    public MagicalScepterRecipe(RegistryEntry<Scepter> result, CraftingRecipeCategory category) {
        super(category);
        this.resultScepter = result;
    }

    public boolean matches(CraftingRecipeInput input, World world) {
        boolean containsLapisLazuli = false;
        boolean containsBrownMushroom = false;
        boolean containsScepter = false;

        for (int i = 0; i < input.size(); i++) {
            ItemStack itemStack = input.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                if (ScepterHelper.IS_SCEPTER.test(itemStack) && !containsScepter) {
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

    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        ItemStack craftedScepter = ModItems.MAGICAL_SCEPTER.getDefaultStack();
        for (int i = 0; i < input.size(); i++) {
            ItemStack itemStack = input.getStackInSlot(i);
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
    public IngredientPlacement getIngredientPlacement() {
        DefaultedList<Ingredient> ingredients = DefaultedList.of();
        ingredients.add(Ingredient.ofItems(ModItems.SCEPTER));
        ingredients.add(Ingredient.ofItems(Items.BROWN_MUSHROOM));
        ingredients.add(Ingredient.ofItems(Items.LAPIS_LAZULI));
        return IngredientPlacement.forShapeless(ingredients);
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
    public RecipeSerializer<MagicalScepterRecipe> getSerializer() {
        return ModRecipeSerializer.MAGICAL_SCEPTER;
    }
}

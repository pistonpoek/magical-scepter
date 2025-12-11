package io.github.pistonpoek.magicalscepter.recipe;

import io.github.pistonpoek.magicalscepter.component.ScepterExperienceComponent;
import io.github.pistonpoek.magicalscepter.item.ArcaneScepterItem;
import io.github.pistonpoek.magicalscepter.item.ModItems;
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
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Custom crafting recipe to craft experience bottles from scepter experience component.
 */
public class ExperienceBottleRecipe extends SpecialCraftingRecipe {
    @Nullable
    private IngredientPlacement ingredientPlacement;

    /**
     * Construct the experience bottle recipe for the specified crafting recipe category.
     *
     * @param category Crafting recipe category to create recipe with.
     */
    public ExperienceBottleRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        boolean containsChargedArcaneScepter = false;
        boolean containsGlassBottle = false;

        for (int i = 0; i < input.size(); i++) {
            ItemStack itemStack = input.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                if (itemStack.isOf(ModItems.CHARGED_ARCANE_SCEPTER) && !containsChargedArcaneScepter) {
                    containsChargedArcaneScepter = true;
                } else if (itemStack.isOf(Items.GLASS_BOTTLE) && !containsGlassBottle) {
                    containsGlassBottle = true;
                } else {
                    return false;
                }
            }
        }
        return containsChargedArcaneScepter && containsGlassBottle;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        return Items.EXPERIENCE_BOTTLE.getDefaultStack();
    }

    @Override
    public DefaultedList<ItemStack> getRecipeRemainders(CraftingRecipeInput input) {
        DefaultedList<ItemStack> remainders = DefaultedList.ofSize(input.size(), ItemStack.EMPTY);

        for (int i = 0; i < remainders.size(); i++) {
            ItemStack itemStack = input.getStackInSlot(i).copy();
            if (itemStack.isOf(ModItems.CHARGED_ARCANE_SCEPTER)) {
                ScepterExperienceComponent.add(itemStack, -ArcaneScepterItem.EXPERIENCE_STEP);
                ItemStack remainder = ArcaneScepterItem.getReplacementStack(itemStack);
                remainders.set(i, remainder.isEmpty() ? itemStack : remainder);
            } else {
                remainders.set(i, itemStack.getItem().getRecipeRemainder());
            }
        }

        return remainders;
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return false;
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        if (this.ingredientPlacement == null) {
            this.ingredientPlacement = IngredientPlacement.forShapeless(List.of(
                    Ingredient.ofItem(ModItems.CHARGED_ARCANE_SCEPTER),
                    Ingredient.ofItem(Items.GLASS_BOTTLE)
            ));
        }
        return this.ingredientPlacement;
    }

    @Override
    public RecipeSerializer<ExperienceBottleRecipe> getSerializer() {
        return ModRecipeSerializer.EXPERIENCE_BOTTLE;
    }

    @Override
    public List<RecipeDisplay> getDisplays() {
        return List.of(
                new ShapelessCraftingRecipeDisplay(
                        getIngredientPlacement().getIngredients().stream().map(Ingredient::toDisplay).toList(),
                        new SlotDisplay.ItemSlotDisplay(Items.EXPERIENCE_BOTTLE),
                        new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)
                )
        );
    }
}

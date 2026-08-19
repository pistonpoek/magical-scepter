package io.github.pistonpoek.magicalscepter.recipe;

import io.github.pistonpoek.magicalscepter.component.ScepterExperienceComponent;
import io.github.pistonpoek.magicalscepter.item.ArcaneScepterItem;
import io.github.pistonpoek.magicalscepter.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Custom crafting recipe to craft experience bottles from scepter experience component.
 */
public class ExperienceBottleRecipe extends CustomRecipe {
    @Nullable
    private PlacementInfo ingredientPlacement;

    /**
     * Construct the experience bottle recipe for the specified crafting recipe category.
     *
     * @param category Crafting recipe category to create recipe with.
     */
    public ExperienceBottleRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level world) {
        boolean containsChargedArcaneScepter = false;
        boolean containsGlassBottle = false;

        for (int i = 0; i < input.size(); i++) {
            ItemStack itemStack = input.getItem(i);
            if (!itemStack.isEmpty()) {
                if (itemStack.is(ModItems.CHARGED_ARCANE_SCEPTER) && !containsChargedArcaneScepter) {
                    containsChargedArcaneScepter = true;
                } else if (itemStack.is(Items.GLASS_BOTTLE) && !containsGlassBottle) {
                    containsGlassBottle = true;
                } else {
                    return false;
                }
            }
        }
        return containsChargedArcaneScepter && containsGlassBottle;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return Items.EXPERIENCE_BOTTLE.getDefaultInstance();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remainders = NonNullList.withSize(input.size(), ItemStack.EMPTY);

        for (int i = 0; i < remainders.size(); i++) {
            ItemStack itemStack = input.getItem(i).copy();
            if (itemStack.is(ModItems.CHARGED_ARCANE_SCEPTER)) {
                ScepterExperienceComponent.add(itemStack, -ArcaneScepterItem.EXPERIENCE_STEP);
                ItemStack remainder = ArcaneScepterItem.getReplacementStack(itemStack);
                remainders.set(i, remainder.isEmpty() ? itemStack : remainder);
            } else {
                remainders.set(i, itemStack.getItem().getCraftingRemainder());
            }
        }

        return remainders;
    }

    @Override
    public boolean isSpecial() {
        return false;
    }

    @Override
    public PlacementInfo placementInfo() {
        if (this.ingredientPlacement == null) {
            this.ingredientPlacement = PlacementInfo.create(List.of(
                    Ingredient.of(ModItems.CHARGED_ARCANE_SCEPTER),
                    Ingredient.of(Items.GLASS_BOTTLE)
            ));
        }
        return this.ingredientPlacement;
    }

    @Override
    public RecipeSerializer<ExperienceBottleRecipe> getSerializer() {
        return ModRecipeSerializer.EXPERIENCE_BOTTLE;
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(
                new ShapelessCraftingRecipeDisplay(
                        placementInfo().ingredients().stream().map(Ingredient::display).toList(),
                        new SlotDisplay.ItemSlotDisplay(Items.EXPERIENCE_BOTTLE),
                        new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)
                )
        );
    }
}

package io.github.pistonpoek.magicalscepter.recipe;

import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import io.github.pistonpoek.magicalscepter.component.ScepterExperienceComponent;
import io.github.pistonpoek.magicalscepter.item.ArcaneScepterItem;
import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import net.fabricmc.fabric.impl.recipe.ingredient.builtin.ComponentsIngredient;
import net.minecraft.component.ComponentChanges;
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

import java.util.List;

/**
 * Custom crafting recipe to craft experience bottles from scepter experience component.
 */
public class ExperienceBottleRecipe extends SpecialCraftingRecipe {
    private final static IngredientPlacement INGREDIENT_PLACEMENT;

    static {
        Ingredient ingredient = new ComponentsIngredient(
                Ingredient.ofItem(ModItems.ARCANE_SCEPTER),
                ComponentChanges.builder().add(
                        ModDataComponentTypes.SCEPTER_EXPERIENCE,
                        new ScepterExperienceComponent(ArcaneScepterItem.EXPERIENCE_STEP)
                ).build()
        ).toVanilla();
        INGREDIENT_PLACEMENT = IngredientPlacement.forShapeless(List.of(
                ingredient,
                Ingredient.ofItem(Items.GLASS_BOTTLE)
        ));
    }

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
        boolean containsFilledArcaneScepter = false;
        boolean containsGlassBottle = false;

        for (int i = 0; i < input.size(); i++) {
            ItemStack itemStack = input.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                if (ScepterHelper.CHARGED_SCEPTER.test(itemStack) && !containsFilledArcaneScepter) {
                    containsFilledArcaneScepter = true;
                } else if (itemStack.isOf(Items.GLASS_BOTTLE) && !containsGlassBottle) {
                    containsGlassBottle = true;
                } else {
                    return false;
                }
            }
        }
        return containsFilledArcaneScepter && containsGlassBottle;
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
            if (ScepterHelper.CHARGED_SCEPTER.test(itemStack)) {
                int experience = ScepterHelper.getExperience(itemStack) - ArcaneScepterItem.EXPERIENCE_STEP;
                itemStack.set(ModDataComponentTypes.SCEPTER_EXPERIENCE, new ScepterExperienceComponent(experience));
                remainders.set(i, itemStack);
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
        return INGREDIENT_PLACEMENT;
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

package io.github.pistonpoek.magicalscepter.recipe;

import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import io.github.pistonpoek.magicalscepter.component.ScepterExperienceComponent;
import io.github.pistonpoek.magicalscepter.item.ArcaneScepterItem;
import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class ExperienceBottleRecipe extends SpecialCraftingRecipe {
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
                if (ScepterHelper.IS_FILLED_ARCANE_SCEPTER.test(itemStack) && !containsFilledArcaneScepter) {
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
            if (ScepterHelper.IS_FILLED_ARCANE_SCEPTER.test(itemStack)) {
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
    public RecipeSerializer<ExperienceBottleRecipe> getSerializer() {
        return ModRecipeSerializer.EXPERIENCE_BOTTLE;
    }
}

package io.github.pistonpoek.magicalscepter.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Custom crafting recipe to craft a magical scepter from a scepter.
 */
public class MagicalScepterRecipe extends CustomRecipe {
    private static final MapCodec<MagicalScepterRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Scepter.ENTRY_CODEC.fieldOf("scepter")
                                    .forGetter(recipe -> recipe.resultScepter)
                    )
                    .apply(instance, MagicalScepterRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, MagicalScepterRecipe> STREAM_CODEC = StreamCodec.composite(
            Scepter.ENTRY_PACKET_CODEC,
            recipe -> recipe.resultScepter,
            MagicalScepterRecipe::new
    );
    public static final RecipeSerializer<MagicalScepterRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    public final Holder<Scepter> resultScepter;
    @Nullable
    private PlacementInfo ingredientPlacement;

    /**
     * Construct the magical scepter recipe for the specified crafting recipe category.
     *
     * @param result Result scepter for the magical scepter to contain.
     */
    public MagicalScepterRecipe(Holder<Scepter> result) {
        this.resultScepter = result;
    }

    /**
     * Check if the crafting recipe input matches the crafting recipe.
     *
     * @param input Crafting recipe input to check.
     * @param world World to use as context.
     * @return Truth assignment, if the input matches the crafting recipe.
     */
    public boolean matches(CraftingInput input, Level world) {
        boolean containsLapisLazuli = false;
        boolean containsBrownMushroom = false;
        boolean containsScepter = false;

        for (int i = 0; i < input.size(); i++) {
            ItemStack itemStack = input.getItem(i);
            if (!itemStack.isEmpty()) {
                if (ScepterHelper.SCEPTER.test(itemStack) && !containsScepter) {
                    containsScepter = true;
                } else if (itemStack.is(Blocks.BROWN_MUSHROOM.asItem()) && !containsBrownMushroom) {
                    containsBrownMushroom = true;
                } else if (itemStack.is(Items.LAPIS_LAZULI) && !containsLapisLazuli) {
                    containsLapisLazuli = true;
                } else {
                    return false;
                }
            }
        }
        return containsScepter && containsLapisLazuli && containsBrownMushroom;
    }

    /**
     * Craft the recipe using the specified input.
     *
     * @param input Crafting recipe input to use.
     * @return Item stack result from crafting the recipe.
     */
    @Override
    public ItemStack assemble(CraftingInput input) {
        ItemStack craftedScepter = ModItems.MAGICAL_SCEPTER.getDefaultInstance();
        for (int i = 0; i < input.size(); i++) {
            ItemStack itemStack = input.getItem(i);
            if (itemStack.is(ModItems.SCEPTER)) {
                craftedScepter = itemStack.transmuteCopy(ModItems.MAGICAL_SCEPTER, 1);
                break;
            }
        }
        return ScepterContentsComponent.setScepter(craftedScepter, resultScepter);
    }

    @Override
    public boolean isSpecial() {
        return false;
    }

    @Override
    public PlacementInfo placementInfo() {
        if (this.ingredientPlacement == null) {
            this.ingredientPlacement = PlacementInfo.create(List.of(
                    Ingredient.of(ModItems.SCEPTER),
                    Ingredient.of(Items.BROWN_MUSHROOM),
                    Ingredient.of(Items.LAPIS_LAZULI)
            ));
        }
        return this.ingredientPlacement;
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.EQUIPMENT;
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(
                new ShapelessCraftingRecipeDisplay(
                        placementInfo().ingredients().stream().map(Ingredient::display).toList(),
                        new SlotDisplay.ItemStackSlotDisplay(ItemStackTemplate.fromNonEmptyStack(
                                ScepterHelper.createMagicalScepter(resultScepter))),
                        new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)
                )
        );
    }

    @Override
    public RecipeSerializer<MagicalScepterRecipe> getSerializer() {
        return ModRecipeSerializer.MAGICAL_SCEPTER;
    }
}

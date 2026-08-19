package io.github.pistonpoek.magicalscepter.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * Serializer for the magical scepter recipe.
 */
public class MagicalScepterRecipeSerializer
        implements RecipeSerializer<MagicalScepterRecipe> {
    private static final MapCodec<MagicalScepterRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Scepter.ENTRY_CODEC.fieldOf("scepter")
                                    .forGetter(recipe -> recipe.resultScepter),
                            CraftingBookCategory.CODEC.fieldOf("category")
                                    .orElse(CraftingBookCategory.MISC).forGetter(MagicalScepterRecipe::category)
                    )
                    .apply(instance, MagicalScepterRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, MagicalScepterRecipe> PACKET_CODEC = StreamCodec.composite(
            Scepter.ENTRY_PACKET_CODEC,
            recipe -> recipe.resultScepter,
            CraftingBookCategory.STREAM_CODEC,
            MagicalScepterRecipe::category,
            MagicalScepterRecipe::new
    );

    @Override
    public MapCodec<MagicalScepterRecipe> codec() {
        return CODEC;
    }

    @Override
    @Deprecated
    public StreamCodec<RegistryFriendlyByteBuf, MagicalScepterRecipe> streamCodec() {
        return PACKET_CODEC;
    }
}

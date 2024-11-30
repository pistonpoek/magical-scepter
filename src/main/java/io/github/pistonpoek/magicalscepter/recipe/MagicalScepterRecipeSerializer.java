package io.github.pistonpoek.magicalscepter.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;

public class MagicalScepterRecipeSerializer
        implements RecipeSerializer<MagicalScepterRecipe> {
    private static final MapCodec<MagicalScepterRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Scepter.ENTRY_CODEC.fieldOf("scepter")
                                    .forGetter(recipe -> recipe.resultScepter),
                            CraftingRecipeCategory.CODEC.fieldOf("category")
                                    .orElse(CraftingRecipeCategory.MISC).forGetter(MagicalScepterRecipe::getCategory)
                    )
                    .apply(instance, MagicalScepterRecipe::new)
    );
    public static final PacketCodec<RegistryByteBuf, MagicalScepterRecipe> PACKET_CODEC = PacketCodec.tuple(
            Scepter.ENTRY_PACKET_CODEC,
            recipe -> recipe.resultScepter,
            CraftingRecipeCategory.PACKET_CODEC,
            MagicalScepterRecipe::getCategory,
            MagicalScepterRecipe::new
    );

    @Override
    public MapCodec<MagicalScepterRecipe> codec() {
        return CODEC;
    }

    @Override
    @Deprecated
    public PacketCodec<RegistryByteBuf, MagicalScepterRecipe> packetCodec() {
        return PACKET_CODEC;
    }
}

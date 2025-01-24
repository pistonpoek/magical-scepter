package io.github.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.loot.context.ModLootContextTypes;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public record FilterCastTransformer(LootContextPredicate filters) implements CastTransformer {
    public static final MapCodec<FilterCastTransformer> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    LootContextPredicate.CODEC.fieldOf("filters").forGetter(FilterCastTransformer::filters)
            ).apply(instance, FilterCastTransformer::new)
    );

    @Override
    public Collection<SpellCasting> transform(@NotNull SpellCasting casting) {
        SpellContext context = casting.getContext();
        ServerWorld serverWorld = context.getWorld();
        Vec3d position = context.position();
        ItemStack stack = context.caster().getActiveItem();
        BlockState blockState = serverWorld.getBlockState(BlockPos.ofFloored(position));
        LootWorldContext lootWorldContext = new LootWorldContext.Builder(serverWorld)
                .add(LootContextParameters.THIS_ENTITY, context.target())
                .add(LootContextParameters.ORIGIN, position)
                .add(LootContextParameters.TOOL, stack)
                .add(LootContextParameters.BLOCK_STATE, blockState)
                .build(ModLootContextTypes.SPELL_CAST);
        LootContext lootContext = new LootContext.Builder(lootWorldContext).build(Optional.empty());
        if (filters.test(lootContext)) {
            return List.of(casting);
        }

        return List.of();
    }

    @Override
    public MapCodec<FilterCastTransformer> getCodec() {
        return MAP_CODEC;
    }
}

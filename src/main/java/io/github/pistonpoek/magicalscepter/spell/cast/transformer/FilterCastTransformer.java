package io.github.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.loot.context.ModLootContextTypes;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.advancements.predicates.ContextAwarePredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public record FilterCastTransformer(ContextAwarePredicate filters) implements CastTransformer {
    public static final MapCodec<FilterCastTransformer> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ContextAwarePredicate.CODEC.fieldOf("filters").forGetter(FilterCastTransformer::filters)
            ).apply(instance, FilterCastTransformer::new)
    );

    @Override
    public Collection<SpellCasting> transform(@NotNull SpellCasting casting) {
        SpellContext context = casting.getContext();
        ServerLevel serverWorld = context.getWorld();
        Vec3 position = context.position();
        ItemStack stack = context.caster().getUseItem();
        BlockState blockState = serverWorld.getBlockState(BlockPos.containing(position));
        LootParams lootWorldContext = new LootParams.Builder(serverWorld)
                .withParameter(LootContextParams.THIS_ENTITY, context.target())
                .withParameter(LootContextParams.ORIGIN, position)
                .withParameter(LootContextParams.TOOL, stack)
                .withParameter(LootContextParams.BLOCK_STATE, blockState)
                .create(ModLootContextTypes.SPELL_CAST);
        LootContext lootContext = new LootContext.Builder(lootWorldContext).create(Optional.empty());
        if (filters.matches(lootContext)) {
            return List.of(casting);
        }

        return List.of();
    }

    @Override
    public MapCodec<FilterCastTransformer> getCodec() {
        return MAP_CODEC;
    }
}

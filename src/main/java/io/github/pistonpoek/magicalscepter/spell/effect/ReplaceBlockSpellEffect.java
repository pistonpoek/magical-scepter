package io.github.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

/**
 * TODO
 *
 * @param blockState
 */
public record ReplaceBlockSpellEffect(BlockStateProvider blockState) implements SpellEffect {
    public static final MapCodec<ReplaceBlockSpellEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            BlockStateProvider.TYPE_CODEC.fieldOf("block_state").forGetter(ReplaceBlockSpellEffect::blockState)
                    )
                    .apply(instance, ReplaceBlockSpellEffect::new)
    );

    @Override
    public void apply(SpellContext context) {
        BlockPos blockPos = BlockPos.ofFloored(context.position());
        context.getWorld().setBlockState(blockPos, this.blockState.get(context.getRandom(), blockPos));
    }

    @Override
    public MapCodec<? extends SpellEffect> getCodec() {
        return MAP_CODEC;
    }
}

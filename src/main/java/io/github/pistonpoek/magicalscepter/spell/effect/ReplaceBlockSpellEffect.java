package io.github.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record ReplaceBlockSpellEffect(BlockStateProvider blockState) implements SpellEffect {
    public static final MapCodec<ReplaceBlockSpellEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            BlockStateProvider.CODEC.fieldOf("block_state").forGetter(ReplaceBlockSpellEffect::blockState)
                    )
                    .apply(instance, ReplaceBlockSpellEffect::new)
    );

    @Override
    public void apply(SpellContext context) {
        BlockPos blockPos = BlockPos.containing(context.position());
        context.getWorld().setBlockAndUpdate(blockPos, this.blockState.getState(context.getRandom(), blockPos));
    }

    @Override
    public MapCodec<? extends SpellEffect> getCodec() {
        return MAP_CODEC;
    }
}

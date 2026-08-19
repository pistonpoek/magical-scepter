package io.github.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import io.github.pistonpoek.magicalscepter.spell.position.AbsolutePositionSource;
import io.github.pistonpoek.magicalscepter.spell.position.PositionSource;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public record SurfaceCastTransformer(float distance, boolean require, Optional<PositionSource> position)
        implements CastTransformer {
    public static final MapCodec<SurfaceCastTransformer> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.FLOAT.fieldOf("distance").forGetter(SurfaceCastTransformer::distance),
                    Codec.BOOL.optionalFieldOf("require", true).forGetter(SurfaceCastTransformer::require),
                    PositionSource.CODEC.optionalFieldOf("position").forGetter(SurfaceCastTransformer::position)
            ).apply(instance, SurfaceCastTransformer::new)
    );

    @Override
    public Collection<SpellCasting> transform(@NotNull SpellCasting casting) {
        Optional<Vec3> value = getSurfacePosition(casting);

        if (value.isEmpty()) {
            return require ? List.of() : List.of(casting);
        }

        // Update the context source to use the found position.
        casting.addContext(AbsolutePositionSource.builder(
                value.get()).build());

        return List.of(casting);
    }

    private Optional<Vec3> getSurfacePosition(@NotNull SpellCasting cast) {
        Level world = cast.getCaster().level();
        Vec3 castPosition = cast.getContext().position();

        // Compute the top and bottom search positions.
        double top, bottom;
        if (position.isPresent()) {
            Vec3 diffPosition = position.get().getPosition(cast.getContext());
            top = Math.max(castPosition.y, diffPosition.y);
            bottom = Math.min(castPosition.y, diffPosition.y);
        } else {
            top = castPosition.y;
            bottom = castPosition.y;
        }

        // Initialize the top block position to search down from.
        BlockPos blockPosition = BlockPos.containing(castPosition.x(), top, castPosition.z());

        boolean foundSurface = false;
        double blockHeight = 0.0;

        do {
            if (checkForSurface(world, blockPosition)) {
                foundSurface = true;
                break;
            }
        } while ((blockPosition = blockPosition.below()).getY() >= bottom);

        if (!foundSurface) {
            for (int i = 0; i <= distance(); i++) {
                BlockPos aboveTopPosition = BlockPos.containing(castPosition.x(), top + i, castPosition.z());
                if (checkForSurface(world, aboveTopPosition)) {
                    blockPosition = aboveTopPosition;
                    foundSurface = true;
                    break;
                }
                BlockPos belowBottomPosition = BlockPos.containing(castPosition.x(), bottom - i, castPosition.z());
                if (checkForSurface(world, belowBottomPosition)) {
                    blockPosition = belowBottomPosition;
                    foundSurface = true;
                    break;
                }
            }
        }

        if (foundSurface) {
            // Update block height
            if (!world.isEmptyBlock(blockPosition)) {
                BlockState blockState = world.getBlockState(blockPosition);
                VoxelShape voxelShape = blockState.getCollisionShape(world, blockPosition);
                if (!voxelShape.isEmpty()) {
                    blockHeight = voxelShape.max(Direction.Axis.Y);
                }
            }

            return Optional.of(new Vec3(castPosition.x(),
                    blockPosition.getY() + blockHeight, castPosition.z()));
        }
        return Optional.empty();
    }

    private boolean checkForSurface(Level world, BlockPos pos) {
        BlockPos LoweredBlockPos = pos.below();
        BlockState LoweredBlockState = world.getBlockState(LoweredBlockPos);
        return LoweredBlockState.isFaceSturdy(world, LoweredBlockPos, Direction.UP);
    }

    @Override
    public MapCodec<SurfaceCastTransformer> getCodec() {
        return MAP_CODEC;
    }

    public static Builder builder(float distance) {
        return new Builder(distance);
    }

    public static class Builder {
        private final float distance;
        private boolean require = true;
        private PositionSource position = null;

        public Builder(float distance) {
            this.distance = distance;
        }

        public Builder position(PositionSource position) {
            this.position = position;
            return this;
        }

        public Builder require(boolean require) {
            this.require = require;
            return this;
        }

        public SurfaceCastTransformer build() {
            return new SurfaceCastTransformer(distance, require, Optional.ofNullable(position));
        }
    }
}

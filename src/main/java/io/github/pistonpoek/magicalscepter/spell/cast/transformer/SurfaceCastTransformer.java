package io.github.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import io.github.pistonpoek.magicalscepter.spell.position.AbsolutePositionSource;
import io.github.pistonpoek.magicalscepter.spell.position.PositionSource;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
        Optional<Vec3d> value = getSurfacePosition(casting);

        if (value.isEmpty()) {
            return require ? List.of() : List.of(casting);
        }

        // Update the context source to use the found position.
        casting.addContext(AbsolutePositionSource.builder(
                value.get()).build());

        return List.of(casting);
    }

    private Optional<Vec3d> getSurfacePosition(@NotNull SpellCasting cast) {
        World world = cast.getCaster().getEntityWorld();
        Vec3d castPosition = cast.getContext().position();

        // Compute the top and bottom search positions.
        double top, bottom;
        if (position.isPresent()) {
            Vec3d diffPosition = position.get().getPosition(cast.getContext());
            top = Math.max(castPosition.y, diffPosition.y);
            bottom = Math.min(castPosition.y, diffPosition.y);
        } else {
            top = castPosition.y;
            bottom = castPosition.y;
        }

        // Initialize the top block position to search down from.
        BlockPos blockPosition = BlockPos.ofFloored(castPosition.getX(), top, castPosition.getZ());

        boolean foundSurface = false;
        double blockHeight = 0.0;

        do {
            if (checkForSurface(world, blockPosition)) {
                foundSurface = true;
                break;
            }
        } while ((blockPosition = blockPosition.down()).getY() >= bottom);

        if (!foundSurface) {
            for (int i = 0; i <= distance(); i++) {
                BlockPos aboveTopPosition = BlockPos.ofFloored(castPosition.getX(), top + i, castPosition.getZ());
                if (checkForSurface(world, aboveTopPosition)) {
                    blockPosition = aboveTopPosition;
                    foundSurface = true;
                    break;
                }
                BlockPos belowBottomPosition = BlockPos.ofFloored(castPosition.getX(), bottom - i, castPosition.getZ());
                if (checkForSurface(world, belowBottomPosition)) {
                    blockPosition = belowBottomPosition;
                    foundSurface = true;
                    break;
                }
            }
        }

        if (foundSurface) {
            // Update block height
            if (!world.isAir(blockPosition)) {
                BlockState blockState = world.getBlockState(blockPosition);
                VoxelShape voxelShape = blockState.getCollisionShape(world, blockPosition);
                if (!voxelShape.isEmpty()) {
                    blockHeight = voxelShape.getMax(Direction.Axis.Y);
                }
            }

            return Optional.of(new Vec3d(castPosition.getX(),
                    blockPosition.getY() + blockHeight, castPosition.getZ()));
        }
        return Optional.empty();
    }

    private boolean checkForSurface(World world, BlockPos pos) {
        BlockPos LoweredBlockPos = pos.down();
        BlockState LoweredBlockState = world.getBlockState(LoweredBlockPos);
        return LoweredBlockState.isSideSolidFullSquare(world, LoweredBlockPos, Direction.UP);
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

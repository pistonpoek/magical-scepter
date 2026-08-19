package io.github.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import io.github.pistonpoek.magicalscepter.spell.position.AbsolutePositionSource;
import io.github.pistonpoek.magicalscepter.spell.target.AbsoluteTargetSource;
import io.github.pistonpoek.magicalscepter.util.RotationVector;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public record RayCastTransformer(Target target, double range, boolean require) implements CastTransformer {
    public static final MapCodec<RayCastTransformer> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Target.CODEC.fieldOf("target").forGetter(RayCastTransformer::target),
                    Codec.DOUBLE.fieldOf("range").forGetter(RayCastTransformer::range),
                    Codec.BOOL.optionalFieldOf("require", true).forGetter(RayCastTransformer::require)
            ).apply(instance, RayCastTransformer::new)
    );

    public enum Target implements StringRepresentable {
        BLOCK("block"),
        ENTITY("entity");

        public final static Codec<Target> CODEC = StringRepresentable.fromValues(Target::values);
        private final String identifier;

        Target(String identifier) {
            this.identifier = identifier;
        }

        @Override
        public String getSerializedName() {
            return identifier;
        }
    }

    @Override
    public Collection<SpellCasting> transform(@NotNull SpellCasting casting) {
        SpellContext context = casting.getContext();
        Vec3 rotationVector = RotationVector.get(context.pitch(), context.yaw()).normalize();
        Vec3 endPosition = context.position().add(
                rotationVector.x * range,
                rotationVector.y * range,
                rotationVector.z * range
        );

        switch (target) {
            case BLOCK -> {
                BlockHitResult hitResult = blockRaycast(range, context.target(), context.position(), endPosition);

                if (hitResult.getType() == HitResult.Type.MISS && require) {
                    return List.of();
                }

                casting.addContext(AbsolutePositionSource.builder(hitResult.getLocation()).build());
            }
            case ENTITY -> {
                Optional<EntityHitResult> entityHitResult = entityRayCast(range, context.caster(), context.position(), endPosition);
                if (entityHitResult.isEmpty()) {
                    return List.of();
                }
                casting.addContext(new AbsoluteTargetSource(entityHitResult.get().getEntity().getUUID()));
            }
        }

        return List.of(casting);
    }

    private static BlockHitResult blockRaycast(double range, Entity target, Vec3 position, Vec3 endPosition) {
        BlockHitResult hitResult = target.level().clip(
                new ClipContext(position,
                        endPosition,
                        ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.NONE,
                        target
                )
        );

        Vec3 hitResultPos = hitResult.getLocation();
        if (!hitResultPos.closerThan(position, range)) {
            Direction direction = Direction.getApproximateNearest(
                    hitResultPos.x - position.x,
                    hitResultPos.y - position.y,
                    hitResultPos.z - position.z
            );
            return BlockHitResult.miss(hitResultPos, direction, BlockPos.containing(hitResultPos));
        } else {
            return hitResult;
        }
    }

    private static Optional<EntityHitResult> entityRayCast(double range, Entity target, Vec3 position, Vec3 endPosition) {
        HitResult hitResult = blockRaycast(range, target, position, endPosition);

        double distance = hitResult.getLocation().distanceTo(position);
        if (hitResult.getType() != HitResult.Type.MISS) {
            range = distance;
        }

        AABB box = AABB.encapsulatingFullBlocks(BlockPos.containing(position), BlockPos.containing(endPosition)).inflate(1.0);
        Predicate<Entity> entityPredicate = entity -> !entity.isSpectator() && EntitySelector.LIVING_ENTITY_STILL_ALIVE.test(entity);
        EntityHitResult entityHitResult =
                ProjectileUtil.getEntityHitResult(target, position, endPosition, box, entityPredicate, Math.pow(range, 2));

        return entityHitResult != null && entityHitResult.getLocation().distanceTo(position) < distance
                ? Optional.of(entityHitResult) : Optional.empty();
    }

    @Override
    public MapCodec<RayCastTransformer> getCodec() {
        return MAP_CODEC;
    }

    public static Builder builder(Target target, double range) {
        return new Builder(target, range);
    }

    public static class Builder {
        private final Target target;
        private final double range;
        private boolean require = true;

        public Builder(Target target, double range) {
            this.target = target;
            this.range = range;
        }

        public Builder require(boolean require) {
            this.require = require;
            return this;
        }

        public RayCastTransformer build() {
            return new RayCastTransformer(target, range, require);
        }
    }
}

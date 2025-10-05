package io.github.pistonpoek.magicalscepter.spell.cast.transformer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import io.github.pistonpoek.magicalscepter.spell.position.AbsolutePositionSource;
import io.github.pistonpoek.magicalscepter.spell.target.AbsoluteTargetSource;
import io.github.pistonpoek.magicalscepter.util.RotationVector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public record RayCastTransformer(Target target, double range, boolean require) implements CastTransformer {
    public static final MapCodec<RayCastTransformer> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Target.CODEC.fieldOf("target").forGetter(RayCastTransformer::target),
                    Codec.DOUBLE.fieldOf("range").forGetter(RayCastTransformer::range),
                    Codec.BOOL.optionalFieldOf("require", true).forGetter(RayCastTransformer::require)
            ).apply(instance, RayCastTransformer::new)
    );

    public enum Target implements StringIdentifiable {
        BLOCK("block"),
        ENTITY("entity");

        public final static Codec<Target> CODEC = StringIdentifiable.createBasicCodec(Target::values);
        private final String identifier;

        Target(String identifier) {
            this.identifier = identifier;
        }

        @Override
        public String asString() {
            return identifier;
        }
    }

    @Override
    public Collection<SpellCasting> transform(@NotNull SpellCasting casting) {
        SpellContext context = casting.getContext();
        Vec3d rotationVector = RotationVector.get(context.pitch(), context.yaw()).normalize();
        Vec3d endPosition = context.position().add(
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

                casting.addContext(AbsolutePositionSource.builder(hitResult.getPos()).build());
            }
            case ENTITY -> {
                Optional<EntityHitResult> entityHitResult = entityRayCast(range, context.caster(), context.position(), endPosition);
                if (entityHitResult.isEmpty()) {
                    return List.of();
                }
                casting.addContext(new AbsoluteTargetSource(entityHitResult.get().getEntity().getUuid()));
            }
        }

        return List.of(casting);
    }

    private static BlockHitResult blockRaycast(double range, Entity target, Vec3d position, Vec3d endPosition) {
        BlockHitResult hitResult = target.getWorld().raycast(
                new RaycastContext(position,
                        endPosition,
                        RaycastContext.ShapeType.COLLIDER,
                        RaycastContext.FluidHandling.NONE,
                        target
                )
        );

        Vec3d hitResultPos = hitResult.getPos();
        if (!hitResultPos.isInRange(position, range)) {
            Direction direction = Direction.getFacing(
                    hitResultPos.x - position.x,
                    hitResultPos.y - position.y,
                    hitResultPos.z - position.z
            );
            return BlockHitResult.createMissed(hitResultPos, direction, BlockPos.ofFloored(hitResultPos));
        } else {
            return hitResult;
        }
    }

    private static Optional<EntityHitResult> entityRayCast(double range, Entity target, Vec3d position, Vec3d endPosition) {
        HitResult hitResult = blockRaycast(range, target, position, endPosition);

        double distance = hitResult.getPos().distanceTo(position);
        if (hitResult.getType() != HitResult.Type.MISS) {
            range = distance;
        }

        Box box = Box.enclosing(BlockPos.ofFloored(position), BlockPos.ofFloored(endPosition)).expand(1.0);
        Predicate<Entity> entityPredicate = entity -> !entity.isSpectator() && EntityPredicates.VALID_LIVING_ENTITY.test(entity);
        EntityHitResult entityHitResult =
                ProjectileUtil.raycast(target, position, endPosition, box, entityPredicate, Math.pow(range, 2));

        return entityHitResult != null && entityHitResult.getPos().distanceTo(position) < distance
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

package io.github.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.MapCodec;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;

import java.util.Set;

public record TeleportSpellEffect()
        implements SpellEffect {
    public static final MapCodec<TeleportSpellEffect> CODEC = MapCodec.unit(new TeleportSpellEffect());

    @Override
    public void apply(SpellContext context) {
        Vec3d position = context.position();
        ServerWorld world = context.getWorld();
        Entity target = context.target();

        double positionX = position.getX();
        double positionY = MathHelper.clamp(
                position.getY(),
                world.getBottomY(),
                world.getBottomY() + world.getLogicalHeight() - 1
        );
        double positionZ = position.getZ();

        Vec3d targetPos = target.getPos();

        BlockPos blockPos = BlockPos.ofFloored(positionX, positionY, positionZ);
        target.teleport(world, blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, Set.of(), target.getYaw(), target.getPitch());

        if (!world.isSpaceEmpty(target) || world.containsFluid(target.getBoundingBox())) {
            target.teleport(world, targetPos.getX(), targetPos.getY(), targetPos.getZ(), Set.of(), target.getYaw(), target.getPitch());
            return;
        }

        if (target.hasVehicle()) {
            target.stopRiding();
        }

        world.emitGameEvent(GameEvent.TELEPORT, blockPos, GameEvent.Emitter.of(target));
        target.onLanding();
        if (target instanceof PathAwareEntity pathAwareEntity) {
            pathAwareEntity.getNavigation().stop();
        }
    }

    @Override
    public MapCodec<TeleportSpellEffect> getCodec() {
        return CODEC;
    }
}

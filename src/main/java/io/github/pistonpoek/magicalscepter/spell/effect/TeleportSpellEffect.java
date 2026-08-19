package io.github.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.MapCodec;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public record TeleportSpellEffect() implements SpellEffect {
    public static final MapCodec<TeleportSpellEffect> MAP_CODEC = MapCodec.unit(new TeleportSpellEffect());

    @Override
    public void apply(SpellContext context) {
        Vec3 position = context.position();
        ServerLevel world = context.getWorld();
        Entity target = context.target();

        double positionX = position.x();
        double positionY = Mth.clamp(
                position.y(),
                world.getMinY(),
                world.getMinY() + world.getLogicalHeight() - 1
        );
        double positionZ = position.z();

        Vec3 targetPos = target.position();

        BlockPos blockPos = BlockPos.containing(positionX, positionY, positionZ);
        target.teleportTo(world, blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, Set.of(), target.getYRot(), target.getXRot(), true);

        if (!world.noCollision(target) || world.containsAnyLiquid(target.getBoundingBox())) {
            target.teleportTo(world, targetPos.x(), targetPos.y(), targetPos.z(), Set.of(), target.getYRot(), target.getXRot(), true);
            return;
        }

        if (target.isPassenger()) {
            target.stopRiding();
        }

        world.gameEvent(GameEvent.TELEPORT, blockPos, GameEvent.Context.of(target));
        target.resetFallDistance();
        if (target instanceof PathfinderMob pathAwareEntity) {
            pathAwareEntity.getNavigation().stop();
        }
    }

    @Override
    public MapCodec<TeleportSpellEffect> getCodec() {
        return MAP_CODEC;
    }
}

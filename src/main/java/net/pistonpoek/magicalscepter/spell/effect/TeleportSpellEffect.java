package net.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.MapCodec;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import net.pistonpoek.magicalscepter.spell.cast.SpellContext;

import java.util.Set;

public record TeleportSpellEffect()
        implements SpellEffect {
    public static final MapCodec<TeleportSpellEffect> CODEC = MapCodec.unit(new TeleportSpellEffect());

    @Override
    public void apply(SpellContext context) {
        Vec3d position = context.position();
        ServerWorld world = context.getWorld();
        Entity target = context.target();

        double x = position.getX();
        double y = MathHelper.clamp(
                position.getY(),
                world.getBottomY(),
                world.getBottomY() + world.getLogicalHeight() - 1
        );
        double z = position.getZ();

        if (target.hasVehicle()) {
            target.stopRiding();
        }

        Vec3d vec3d = target.getPos();
        target.teleport(world, x, y, z, Set.of(), context.pitch(), context.yaw());
        world.emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(target));
        target.onLanding();
    }

    @Override
    public MapCodec<? extends SpellEffect> getCodec() {
        return CODEC;
    }
}

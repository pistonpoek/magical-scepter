package net.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;

public record RandomTeleportSpellEffect(boolean spawnParticles, double bound)
        implements SpellEffect {
    public static final MapCodec<RandomTeleportSpellEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.BOOL.fieldOf("spawn_particles").forGetter(RandomTeleportSpellEffect::spawnParticles),
                    Codec.DOUBLE.optionalFieldOf("bound", 16.0).forGetter(RandomTeleportSpellEffect::bound)
                    ).apply(instance, RandomTeleportSpellEffect::new)
    );
    @Override
    public void apply(ServerWorld world, LivingEntity entity, Vec3d position, float pitch, float yaw) {
        for (int i = 0; i < 16; i++) {
            double d = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * bound;
            double e = MathHelper.clamp(
                    entity.getY() + (double) (entity.getRandom().nextInt((int)bound) - 8),
                    (double) world.getBottomY(),
                    (double) (world.getBottomY() + world.getLogicalHeight() - 1)
            );
            double f = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * bound;
            if (entity.hasVehicle()) {
                entity.stopRiding();
            }

            Vec3d vec3d = entity.getPos();
            if (((LivingEntity)entity).teleport(d, e, f, true)) {
                world.emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(entity));
                entity.onLanding();
            }
        }
    }

    @Override
    public MapCodec<? extends SpellEffect> getCodec() {
        return CODEC;
    }
}

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
    public void apply(ServerWorld world, Entity caster, Vec3d pos) {
        if (!(caster instanceof LivingEntity)) {
            return;
        }
        for (int i = 0; i < 16; i++) {
            double d = caster.getX() + (caster.getRandom().nextDouble() - 0.5) * bound;
            double e = MathHelper.clamp(
                    caster.getY() + (double) (caster.getRandom().nextInt((int)bound) - 8),
                    (double) world.getBottomY(),
                    (double) (world.getBottomY() + ((ServerWorld) world).getLogicalHeight() - 1)
            );
            double f = caster.getZ() + (caster.getRandom().nextDouble() - 0.5) * bound;
            if (caster.hasVehicle()) {
                caster.stopRiding();
            }

            Vec3d vec3d = caster.getPos();
            if (((LivingEntity)caster).teleport(d, e, f, true)) {
                world.emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(caster));
                caster.onLanding();
            }
        }
    }

    @Override
    public MapCodec<? extends SpellEffect> getCodec() {
        return CODEC;
    }
}

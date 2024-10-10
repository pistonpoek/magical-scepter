package net.pistonpoek.magicalscepter.spell.cast;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.pistonpoek.magicalscepter.util.RotationVector;

import java.util.Optional;

public record SpellContext(LivingEntity caster, Entity target, Vec3d position, float pitch, float yaw) implements Position {
    SpellContext(Cast cast, SpellContext spellContext) {
        this(cast.getCaster(),
             cast.getTarget(),
             cast.getPositionSource().getPosition(spellContext),
             cast.getRotationSource().getPitch(spellContext),
             cast.getRotationSource().getYaw(spellContext));
    }
// TODO replace caster usage by target where it makes sense in transformers.
    @Override
    public double getX() {
        return position.getX();
    }

    @Override
    public double getY() {
        return position.getY();
    }

    @Override
    public double getZ() {
        return position.getZ();
    }

    public ServerWorld getWorld() {
        return (ServerWorld) caster.getWorld();
    }

    public Random getRandom() {
        return caster.getRandom();
    }

    public Optional<LivingEntity> getLivingTarget() {
        if (target instanceof LivingEntity) {
            return Optional.of((LivingEntity)target);
        }
        return Optional.empty();
    }

    public Vec3d getRotationVector() {
        return RotationVector.get(pitch, yaw);
    }
}

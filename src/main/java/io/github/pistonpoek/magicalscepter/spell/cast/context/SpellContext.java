package io.github.pistonpoek.magicalscepter.spell.cast.context;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import io.github.pistonpoek.magicalscepter.spell.effect.SpellEffect;
import io.github.pistonpoek.magicalscepter.util.RotationVector;

import java.util.List;
import java.util.Optional;

public record SpellContext(LivingEntity caster, Entity target, Vec3d position, float pitch, float yaw) implements Position {

    public SpellContext(LivingEntity caster) {
        this(caster, caster, caster.getEyePos(), caster.getPitch(), caster.getYaw());
    }

    public SpellContext(SpellContext context, Vec3d position) {
        this(context.caster, context.target, position, context.pitch, context.yaw);
    }

    public SpellContext(SpellContext context, float pitch, float yaw) {
        this(context.caster, context.target, context.position, pitch, yaw);
    }

    public SpellContext(SpellContext context, Entity target) {
        this(context.caster, target, context.position, context.pitch, context.yaw);
    }

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
        if (target instanceof LivingEntity livingTarget) {
            return Optional.of(livingTarget);
        }
        return Optional.empty();
    }

    public Vec3d getRotationVector() {
        return RotationVector.get(pitch, yaw);
    }

    public void apply(List<SpellEffect> effects) {
        for (SpellEffect effect: effects) {
            effect.apply(this);
        }
    }
}

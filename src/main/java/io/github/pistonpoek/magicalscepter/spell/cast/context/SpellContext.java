package io.github.pistonpoek.magicalscepter.spell.cast.context;

import io.github.pistonpoek.magicalscepter.spell.effect.SpellEffect;
import io.github.pistonpoek.magicalscepter.util.RotationVector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.Optional;

/**
 * TODO
 *
 * @param caster
 * @param target
 * @param position
 * @param pitch
 * @param yaw
 */
public record SpellContext(LivingEntity caster, Entity target, Vec3d position, float pitch,
                           float yaw) implements Position {
    /**
     * TODO
     *
     * @param caster
     */
    public SpellContext(LivingEntity caster) {
        this(caster, caster, caster.getEyePos(), caster.getPitch(), caster.getYaw());
    }

    /**
     * TODO
     *
     * @param context
     * @param position
     */
    public SpellContext(SpellContext context, Vec3d position) {
        this(context.caster, context.target, position, context.pitch, context.yaw);
    }

    /**
     * TODO
     *
     * @param context
     * @param pitch
     * @param yaw
     */
    public SpellContext(SpellContext context, float pitch, float yaw) {
        this(context.caster, context.target, context.position, pitch, yaw);
    }

    /**
     * TODO
     *
     * @param context
     * @param target
     */
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

    /**
     * TODO
     *
     * @return
     */
    public ServerWorld getWorld() {
        return (ServerWorld) caster.getEntityWorld();
    }

    /**
     * TODO
     *
     * @return
     */
    public Random getRandom() {
        return caster.getRandom();
    }

    /**
     * TODO
     *
     * @return
     */
    public Optional<LivingEntity> getLivingTarget() {
        if (target instanceof LivingEntity livingTarget) {
            return Optional.of(livingTarget);
        }
        return Optional.empty();
    }

    /**
     * TODO
     *
     * @return
     */
    public Vec3d getRotationVector() {
        return RotationVector.get(pitch, yaw);
    }

    /**
     * TODO
     *
     * @param effects
     */
    public void apply(List<SpellEffect> effects) {
        for (SpellEffect effect : effects) {
            effect.apply(this);
        }
    }
}

package io.github.pistonpoek.magicalscepter.spell.cast.context;

import io.github.pistonpoek.magicalscepter.spell.effect.SpellEffect;
import io.github.pistonpoek.magicalscepter.util.RotationVector;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public record SpellContext(LivingEntity caster, Entity target, Vec3 position, float pitch,
                           float yaw) implements Position {

    public SpellContext(LivingEntity caster) {
        this(caster, caster, caster.getEyePosition(), caster.getXRot(), caster.getYRot());
    }

    public SpellContext(SpellContext context, Vec3 position) {
        this(context.caster, context.target, position, context.pitch, context.yaw);
    }

    public SpellContext(SpellContext context, float pitch, float yaw) {
        this(context.caster, context.target, context.position, pitch, yaw);
    }

    public SpellContext(SpellContext context, Entity target) {
        this(context.caster, target, context.position, context.pitch, context.yaw);
    }

    @Override
    public double x() {
        return position.x();
    }

    @Override
    public double y() {
        return position.y();
    }

    @Override
    public double z() {
        return position.z();
    }

    public ServerLevel getWorld() {
        return (ServerLevel) caster.level();
    }

    public RandomSource getRandom() {
        return caster.getRandom();
    }

    public Optional<LivingEntity> getLivingTarget() {
        if (target instanceof LivingEntity livingTarget) {
            return Optional.of(livingTarget);
        }
        return Optional.empty();
    }

    public Vec3 getRotationVector() {
        return RotationVector.get(pitch, yaw);
    }

    public void apply(List<SpellEffect> effects) {
        for (SpellEffect effect : effects) {
            effect.apply(this);
        }
    }
}

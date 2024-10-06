package net.pistonpoek.magicalscepter.spell.cast;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;

public record SpellContext(LivingEntity caster, Vec3d position, float pitch, float yaw) implements Position {
    SpellContext(Cast cast, SpellContext spellContext) {
        this(cast.getCaster(),
             cast.getPositionSource().getPosition(spellContext),
             cast.getRotationSource().getPitch(spellContext),
             cast.getRotationSource().getYaw(spellContext));
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
}

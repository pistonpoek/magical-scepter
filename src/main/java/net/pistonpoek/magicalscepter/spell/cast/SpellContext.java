package net.pistonpoek.magicalscepter.spell.cast;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

public record SpellContext(LivingEntity caster, Vec3d position, float pitch, float yaw) {
    SpellContext(Cast cast, SpellContext spellContext) {
        this(cast.getCaster(),
             cast.getPosition().getPosition(spellContext),
             cast.getRotation().getPitch(spellContext),
             cast.getRotation().getYaw(spellContext));
    }
}

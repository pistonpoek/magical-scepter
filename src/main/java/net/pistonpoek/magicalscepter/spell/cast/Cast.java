package net.pistonpoek.magicalscepter.spell.cast;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.pistonpoek.magicalscepter.spell.effect.SpellEffect;
import net.pistonpoek.magicalscepter.spell.position.EntityPositionSource;
import net.pistonpoek.magicalscepter.spell.position.PositionSource;
import net.pistonpoek.magicalscepter.spell.rotation.RelativeRotationSource;
import net.pistonpoek.magicalscepter.spell.rotation.RotationSource;

import java.util.List;


public class Cast implements Cloneable {
    private PositionSource position = new EntityPositionSource(EntityPositionSource.Anchor.EYES);
    private RotationSource rotation = new RelativeRotationSource(0, 0);
    private LivingEntity caster;
    private int delay = 0;
    private SpellContext context;

    public Cast(LivingEntity caster) {
        this(caster, caster.getEyePos(), caster.getPitch(), caster.getYaw());
    }

    public Cast(LivingEntity caster, Vec3d position, float pitch, float yaw) {
        this.caster = caster;
        this.context = new SpellContext(caster, position, pitch, yaw);
    }

    public SpellContext getContext() {
        context = new SpellContext(this, context);
        return context;
    }

    public int getDelay() {
        return delay;
    }

    public Cast setDelay(int delay) {
        this.delay = delay;
        return this;
    }

    public LivingEntity getCaster() {
        return caster;
    }

    public Cast setCaster(LivingEntity caster) {
        this.caster = caster;
        return this;
    }

    public PositionSource getPosition() {
        return position;
    }

    public Cast setPosition(PositionSource position) {
        this.position = position;
        return this;
    }

    public RotationSource getRotation() {
        return rotation;
    }

    public Cast setRotation(RotationSource rotation) {
        this.rotation = rotation;
        return this;
    }

    public void apply(List<SpellEffect> effects) {
        ServerWorld serverWorld = (ServerWorld) getCaster().getWorld();
        for (SpellEffect spellEffect : effects) {
            spellEffect.apply(
                    serverWorld,
                    getCaster(),
                    position.getPosition(getContext()),
                    rotation.getPitch(getContext()),
                    rotation.getYaw(getContext())
            );
        }
    }

    @Override
    public Cast clone() {
        try {
            Cast clone = (Cast) super.clone();
            clone.setCaster(caster);
            clone.setDelay(delay);
            clone.setPosition(position);
            clone.setRotation(rotation);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

package net.pistonpoek.magical_scepter.item.scepter.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;

public class EffectScepterSpell
        extends InstantScepterSpell {
    private final StatusEffectInstance statusEffectInstance;
    public static final int DEFAULT_EFFECT_DURATION = 10 * 20; // One second is 20 ticks.

    protected EffectScepterSpell(int experienceCost, int castCooldown, RegistryEntry<StatusEffect> statusEffect) {
        this(experienceCost, castCooldown, statusEffect, DEFAULT_EFFECT_DURATION, 0);
    }

    protected EffectScepterSpell(int experienceCost, int castCooldown, RegistryEntry<StatusEffect> statusEffect, int amplifier) {
        this(experienceCost, castCooldown, statusEffect, DEFAULT_EFFECT_DURATION, amplifier);
    }

    protected EffectScepterSpell(int experienceCost, int castCooldown, RegistryEntry<StatusEffect> statusEffect, int effectDuration, int amplifier) {
        this(experienceCost, castCooldown,
                new StatusEffectInstance(statusEffect, effectDuration, amplifier,
                        false, true, true));
    }

    protected EffectScepterSpell(int experienceCost, int castCooldown, StatusEffectInstance statusEffectInstance) {
        super(experienceCost, castCooldown);
        this.statusEffectInstance = statusEffectInstance;
    }

    public void castSpell(LivingEntity caster) {
        caster.addStatusEffect(new StatusEffectInstance(statusEffectInstance));
    }

    @Override
    public void displaySpell(LivingEntity caster, int remainingCastTicks) {
        //statusEffectInstance.getEffectType().getColor();
        ScepterSpellUtil.addEffectParticles(caster, statusEffectInstance.getEffectType().value().getColor());
        //caster.getWorld().addParticle(ParticleTypes.EFFECT, caster.getX(), caster.getY(), caster.getZ(), caster.getRandom().nextGaussian(), caster.getRandom().nextGaussian(), caster.getRandom().nextGaussian());
    }
}

package net.pistonpoek.magical_scepter.item.scepter.spell;

import net.minecraft.entity.LivingEntity;

public abstract class InstantScepterSpell
        extends ScepterSpell {

    public InstantScepterSpell(int experienceCost, int castCooldown) {
        super(experienceCost, castCooldown);
    }

    public boolean isInstant() {
        return true;
    }

    public int getCastDuration() {
        return -1;
    }

    public abstract void castSpell(LivingEntity caster);

    public abstract void displaySpell(LivingEntity caster, int remainingCastTicks);

    public void updateSpell(LivingEntity caster, int remainingCastTicks) {

    }

    public void endSpell(LivingEntity caster, int remainingCastTicks) {

    }
}

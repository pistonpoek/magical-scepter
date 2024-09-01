package net.pistonpoek.magical_scepter.item.scepter.spell;

import net.minecraft.entity.LivingEntity;

public abstract class ScepterSpell {
    private final int experienceCost;
    private final int spellCooldown;

    public ScepterSpell(int experienceCost, int spellCooldown) {
        this.experienceCost = experienceCost;
        this.spellCooldown = spellCooldown;
    }

    public int getExperienceCost() {
        return experienceCost;
    }

    public int getSpellCooldown() {
        return spellCooldown;
    }

    public boolean isInstant() {
        return false;
    }

    public abstract int getCastDuration();

    public abstract void castSpell(LivingEntity caster);

    public abstract void displaySpell(LivingEntity caster, int remainingCastTicks);

    public abstract void updateSpell(LivingEntity caster, int remainingCastTicks);

    public abstract void endSpell(LivingEntity caster, int remainingCastTicks);
}

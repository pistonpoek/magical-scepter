package net.pistonpoek.magical_scepter.item.scepter.spell;

import net.minecraft.entity.LivingEntity;

public class GuardianBeamScepterSpell extends ScepterSpell {
    public GuardianBeamScepterSpell(int experienceCost, int spellCooldown) {
        super(experienceCost, spellCooldown);
    }

    @Override
    public int getCastDuration() {
        return 20;
    }

    @Override
    public void castSpell(LivingEntity caster) {

    }

    @Override
    public void displaySpell(LivingEntity caster, int remainingCastTicks) {

    }

    @Override
    public void updateSpell(LivingEntity caster, int remainingCastTicks) {

    }

    @Override
    public void endSpell(LivingEntity caster, int remainingCastTicks) {

    }
}

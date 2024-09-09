package net.pistonpoek.magicalscepter.spell;

import net.minecraft.entity.LivingEntity;
import net.pistonpoek.magicalscepter.MagicalScepter;

public record Spell(int experienceCost, int cooldown) {

    public boolean isInstant() {
        return true;
    }

    public int getCastDuration() {
        return 0;
    }

    public void castSpell(LivingEntity caster) {
        MagicalScepter.LOGGER.info("Casting spell");
    }

    public void displaySpell(LivingEntity caster, int remainingCastTicks) {
        MagicalScepter.LOGGER.info("Displaying spell");
    }

    public void updateSpell(LivingEntity caster, int remainingCastTicks) {

    }

    public void endSpell(LivingEntity caster, int remainingCastTicks) {
        MagicalScepter.LOGGER.info("End spell");
    }
}

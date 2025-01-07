package io.github.pistonpoek.magicalscepter.spell.cast.delay;

import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;

public class DelayedSpellCasting extends SpellCasting {
    private final int delay;

    /**
     * Constructs a delayed spell casting.
     *
     * @param casting Spell casting to delay.
     * @param delay Time to delay invoking the spell casting with in ticks.
     */
    public DelayedSpellCasting(SpellCasting casting, int delay) {
        super(casting.getSpellCast(), casting.getCaster(), casting.getContextSource());
        this.delay = delay;
    }

    @Override
    public void invoke() {
        SpellCastScheduler.schedule(getCaster(),
                new SpellCastTimerCallback(getSpellCast(), getCaster().getUuid(), getContextSource()),
                delay);
    }

    @Override
    public DelayedSpellCasting clone() {
        return (DelayedSpellCasting) super.clone();
    }
}

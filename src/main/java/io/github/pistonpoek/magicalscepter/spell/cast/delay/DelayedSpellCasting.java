package io.github.pistonpoek.magicalscepter.spell.cast.delay;

import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellCasting;
import net.minecraft.server.world.ServerWorld;

/**
 * TODO
 */
public class DelayedSpellCasting extends SpellCasting {
    private final int delay;
    private boolean scheduled = false;

    /**
     * Constructs a delayed spell casting.
     *
     * @param casting Spell casting to delay.
     * @param delay   Time to delay invoking the spell casting with in ticks.
     */
    public DelayedSpellCasting(SpellCasting casting, int delay) {
        super(casting.getSpellCast(), casting.getCaster(), casting.getContextSource());
        if (casting instanceof DelayedSpellCasting delayedSpellCasting) {
            delay += delayedSpellCasting.delay;
        }
        this.delay = delay;
    }

    @Override
    public void invoke() {
        if (scheduled) {
            super.invoke();
            return;
        }
        scheduled = true;

        if (getCaster().getEntityWorld() instanceof ServerWorld serverWorld) {
            SpellCastingManager.load(serverWorld.getServer()).schedule(serverWorld, this, delay);
        }
    }

    @Override
    public DelayedSpellCasting clone() {
        return (DelayedSpellCasting) super.clone();
    }
}

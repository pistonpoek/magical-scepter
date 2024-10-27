package net.pistonpoek.magicalscepter.spell.cast.context;

import net.minecraft.entity.LivingEntity;
import net.pistonpoek.magicalscepter.spell.effect.SpellEffect;

import java.util.ArrayList;
import java.util.List;

public class SpellCasting implements Cloneable {
    private ContextSourceList contextSourceList = new ContextSourceList(new ArrayList<>());
    private int delay = 0;
    private final LivingEntity caster;
    private SpellContext context;

    public SpellCasting(LivingEntity caster) {
        this.caster = caster;
        this.context = new SpellContext(caster);
    }

    public SpellContext getContext() {
        return context;
    }

    public int getDelay() {
        return delay;
    }

    public SpellCasting setDelay(int delay) {
        this.delay = delay;
        return this;
    }

    public LivingEntity getCaster() {
        return caster;
    }

    public SpellContextSource getContextSource() {
        return contextSourceList;
    }

    public SpellCasting addContextSource(SpellContextSource contextSource) {
        this.contextSourceList.append(contextSource);
        context = contextSource.getContext(context);
        return this;
    }

    public void apply(List<SpellEffect> effects) {
        getContext().apply(effects);
    }

    @Override
    public SpellCasting clone() {
        try {
            SpellCasting clone = (SpellCasting) super.clone();
            clone.contextSourceList = new ContextSourceList(new ArrayList<>(contextSourceList.contextSources()));
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

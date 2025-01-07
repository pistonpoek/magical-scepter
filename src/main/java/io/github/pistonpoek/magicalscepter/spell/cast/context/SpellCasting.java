package io.github.pistonpoek.magicalscepter.spell.cast.context;

import io.github.pistonpoek.magicalscepter.spell.cast.SpellCast;
import io.github.pistonpoek.magicalscepter.spell.cast.transformer.CastTransformer;
import net.minecraft.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class SpellCasting implements Cloneable {
    protected final SpellCast spellCast;
    protected final LivingEntity caster;
    private ContextSourceList contextSource = new ContextSourceList(new ArrayList<>());
    private SpellContext context;

    public SpellCasting(SpellCast spellCast, LivingEntity caster) {
        this.spellCast = spellCast;
        this.caster = caster;
        this.context = new SpellContext(caster);
    }

    public SpellCasting(SpellCast spellCast, LivingEntity caster, SpellContextSource contextSource) {
        this(spellCast, caster);
        addContext(contextSource);
    }

    public SpellCast getSpellCast() {
        return spellCast;
    }

    public LivingEntity getCaster() {
        return caster;
    }

    public SpellContext getContext() {
        return context;
    }

    public SpellContextSource getContextSource() {
        return contextSource;
    }

    public SpellCasting addContext(SpellContextSource contextSource) {
        this.contextSource.append(contextSource);
        context = contextSource.getContext(context);
        return this;
    }

    public void invoke() {
        SpellCast spellCast = getSpellCast();
        List<CastTransformer> transformers = spellCast.transformers();
        if (!transformers.isEmpty()) {
            transformers.getFirst().transform(new SpellCasting(
                    new SpellCast(spellCast.effects(), transformers.stream().skip(1).toList()),
                    getCaster(),
                    getContextSource())).forEach(SpellCasting::invoke);
        } else {
            getContext().apply(spellCast.effects());
        }
    }

    @Override
    public SpellCasting clone() {
        try {
            SpellCasting clone = (SpellCasting) super.clone();
            clone.contextSource = new ContextSourceList(new ArrayList<>(contextSource.sources()));
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

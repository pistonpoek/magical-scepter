package io.github.pistonpoek.magicalscepter.spell.cast.context;

import io.github.pistonpoek.magicalscepter.spell.cast.SpellCast;
import io.github.pistonpoek.magicalscepter.spell.cast.transformer.CastTransformer;
import net.minecraft.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class SpellCasting implements Cloneable {
    protected final SpellCast spellCast;
    protected final LivingEntity caster;
    private ContextSourceList contextSource = new ContextSourceList(new ArrayList<>());
    private SpellContext context;

    /**
     * TODO
     *
     * @param spellCast
     * @param caster
     */
    public SpellCasting(SpellCast spellCast, LivingEntity caster) {
        this.spellCast = spellCast;
        this.caster = caster;
        this.context = new SpellContext(caster);
    }

    /**
     * TODO
     *
     * @param spellCast
     * @param caster
     * @param contextSource
     */
    public SpellCasting(SpellCast spellCast, LivingEntity caster, SpellContextSource contextSource) {
        this(spellCast, caster);
        addContext(contextSource);
    }

    /**
     * TODO
     *
     * @return
     */
    public SpellCast getSpellCast() {
        return spellCast;
    }

    /**
     * TODO
     *
     * @return
     */
    public LivingEntity getCaster() {
        return caster;
    }

    /**
     * TODo
     *
     * @return
     */
    public SpellContext getContext() {
        return context;
    }

    /**
     * TODO
     *
     * @return
     */
    public SpellContextSource getContextSource() {
        return contextSource;
    }

    /**
     * TODO
     *
     * @param contextSource
     * @return
     */
    public SpellCasting addContext(SpellContextSource contextSource) {
        this.contextSource.append(contextSource);
        context = contextSource.getContext(context);
        return this;
    }

    /**
     * TODO
     */
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

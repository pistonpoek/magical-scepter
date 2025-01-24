package io.github.pistonpoek.magicalscepter.entity.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.entity.effect.StatusEffect
 */
public class ModStatusEffect extends StatusEffect {
    protected ModStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }
}

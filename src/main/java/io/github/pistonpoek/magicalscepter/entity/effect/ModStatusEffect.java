package io.github.pistonpoek.magicalscepter.entity.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.world.effect.MobEffect
 */
public class ModStatusEffect extends MobEffect {
    /**
     * Construct a mod status effect.
     *
     * @param category Status effect category to create an effect for.
     * @param color    Color of the status effect.
     */
    protected ModStatusEffect(MobEffectCategory category, int color) {
        super(category, color);
    }
}

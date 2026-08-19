package io.github.pistonpoek.magicalscepter.enchantment;

import net.minecraft.util.RandomSource;
import org.apache.commons.lang3.mutable.MutableFloat;

/**
 * Interface class to add method to the respective vanilla class.
 *
 * @see net.minecraft.world.item.enchantment.Enchantment
 */
public interface ModEnchantment {
    /**
     * Modify the specified experience step using the specified random and level.
     *
     * @param random Random to use to modify the specified experience step value.
     * @param level Integer level of this enchantment to determine how much to change the specified experience step.
     * @param experienceStep Mutable float that will be modified.
     *
     * @see ModEnchantmentEffectComponentTypes#EXPERIENCE_STEP
     */
    void magicalscepter$modifyExperienceStep(RandomSource random, int level, MutableFloat experienceStep);
}

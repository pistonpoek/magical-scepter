package io.github.pistonpoek.magicalscepter.enchantment;

import net.minecraft.util.math.random.Random;
import org.apache.commons.lang3.mutable.MutableFloat;

/**
 * Interface class to add method to the respective vanilla class.
 *
 * @see net.minecraft.enchantment.Enchantment
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
    void magicalscepter$modifyExperienceStep(Random random, int level, MutableFloat experienceStep);
}

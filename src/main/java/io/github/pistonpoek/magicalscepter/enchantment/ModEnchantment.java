package io.github.pistonpoek.magicalscepter.enchantment;

import net.minecraft.util.math.random.Random;
import org.apache.commons.lang3.mutable.MutableFloat;

public interface ModEnchantment {
    void magicalscepter$modifyExperienceStep(Random random, int level, MutableFloat experienceStep);
}

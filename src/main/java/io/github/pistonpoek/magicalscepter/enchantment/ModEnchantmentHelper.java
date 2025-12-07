package io.github.pistonpoek.magicalscepter.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableFloat;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.enchantment.EnchantmentHelper
 */
public class ModEnchantmentHelper {
    /**
     * Get a modified experience step value with the enchantments on the specified item stack and for a specified user.
     *
     * @param stack Item stack to get enchantments from that may modify the experience step value.
     * @param user Living entity to get the modified experience step value for.
     * @param baseExperienceStep Integer value to be modified based on the stack and user.
     * @return Integer that is a modified value from the specified base experience step.
     */
    public static int getExperienceStep(ItemStack stack, LivingEntity user, int baseExperienceStep) {
        MutableFloat mutableFloat = new MutableFloat(baseExperienceStep);
        EnchantmentHelper.forEachEnchantment(stack, (enchantment, level) ->
                ((ModEnchantment) (Object) enchantment.value()).magicalscepter$modifyExperienceStep(
                        user.getRandom(), level, mutableFloat));
        return Math.max(0, mutableFloat.intValue());
    }
}

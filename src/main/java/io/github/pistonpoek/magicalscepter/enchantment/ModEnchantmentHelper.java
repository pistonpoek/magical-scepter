package io.github.pistonpoek.magicalscepter.enchantment;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import org.apache.commons.lang3.mutable.MutableFloat;

public class ModEnchantmentHelper {
    public static int getExperienceStep(ItemStack stack, LivingEntity user, int baseExperienceStep) {
        MutableFloat mutableFloat = new MutableFloat(baseExperienceStep);
        forEachEnchantment(stack, (enchantment, level) ->
                ((ModEnchantment) (Object) enchantment.value()).magicalscepter$modifyExperienceStep(
                        user.getRandom(), level, mutableFloat));
        return Math.max(0, mutableFloat.intValue());
    }

    private static void forEachEnchantment(ItemStack stack, ModEnchantmentHelper.Consumer consumer) {
        ItemEnchantmentsComponent itemEnchantmentsComponent = stack.getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);

        for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : itemEnchantmentsComponent.getEnchantmentEntries()) {
            consumer.accept(entry.getKey(), entry.getIntValue());
        }
    }

    @FunctionalInterface
    interface Consumer {
        void accept(RegistryEntry<Enchantment> enchantment, int level);
    }
}

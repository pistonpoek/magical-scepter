package net.pistonpoek.magicalscepter.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import java.util.function.Predicate;

public class LivingEntityHand {
    public static Hand get(LivingEntity entity, Predicate<ItemStack> predicate) {
        return predicate.test(entity.getMainHandStack()) ? Hand.MAIN_HAND : Hand.OFF_HAND;
    }
}

package io.github.pistonpoek.magicalscepter.mixin;

import io.github.pistonpoek.magicalscepter.enchantment.ModEnchantment;
import io.github.pistonpoek.magicalscepter.enchantment.ModEnchantmentEffectComponentTypes;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin implements ModEnchantment {
    @Unique
    @Override
    public void magicalscepter$modifyExperienceStep(RandomSource random, int level, MutableFloat experienceStep) {
        this.invokeModifyValue(ModEnchantmentEffectComponentTypes.EXPERIENCE_STEP, random, level, experienceStep);
    }

    @Invoker("modifyUnfilteredValue")
    abstract void invokeModifyValue(DataComponentType<EnchantmentValueEffect> type, RandomSource random, int level, MutableFloat value);
}

package io.github.pistonpoek.magicalscepter.mixin;

import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.enchantment.ModEnchantmentEffectComponentTypes;
import io.github.pistonpoek.magicalscepter.enchantment.ModEnchantment;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.util.math.random.Random;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin implements ModEnchantment {
    @Unique
    @Override
    public void magicalscepter$modifyExperienceStep(Random random, int level, MutableFloat experienceStep) {
        MagicalScepter.LOGGER.info("Modfiying exprience step value: " + experienceStep.getValue());
        this.invokeModifyValue(ModEnchantmentEffectComponentTypes.EXPERIENCE_STEP, random, level, experienceStep);
    }

    @Invoker("modifyValue")
    abstract void invokeModifyValue(ComponentType<EnchantmentValueEffect> type, Random random, int level, MutableFloat value);
}

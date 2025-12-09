package io.github.pistonpoek.gametest.magicalscepter;

import io.github.pistonpoek.magicalscepter.enchantment.ModEnchantmentHelper;
import io.github.pistonpoek.magicalscepter.enchantment.ModEnchantments;
import io.github.pistonpoek.magicalscepter.item.ModItems;
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PiglinBruteEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

import java.lang.reflect.Method;

import static io.github.pistonpoek.gametest.magicalscepter.util.ContextUtil.getEnchantment;
import static io.github.pistonpoek.gametest.magicalscepter.util.ContextUtil.setMagicalScepterInMainHand;

public class EnchantmentTest implements CustomTestMethodInvoker {
    @Override
    public void invokeTestMethod(TestContext context, Method method) throws ReflectiveOperationException {
        method.invoke(this, context);
    }

    @GameTest(structure="gametest:template/empty")
    public void checkExperienceStepModifier(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ItemStack itemStack = ModItems.ARCANE_SCEPTER.getDefaultStack();
        player.setStackInHand(Hand.MAIN_HAND, itemStack);

        int stepWithout = ModEnchantmentHelper.getExperienceStep(itemStack, player, 7);
        context.assertEquals(7, stepWithout, Text.of("experience step without enchantment"));

        addInsightEnchantment(context, itemStack, 1);
        int stepLevel1 = ModEnchantmentHelper.getExperienceStep(itemStack, player, 7);
        context.assertEquals(14, stepLevel1, Text.of("experience step insight level 1"));

        addInsightEnchantment(context, itemStack, 2);
        int stepLevel2 = ModEnchantmentHelper.getExperienceStep(itemStack, player, 7);
        context.assertEquals(28, stepLevel2, Text.of("experience step insight level 2"));

        addInsightEnchantment(context, itemStack, 3);
        int stepLevel3 = ModEnchantmentHelper.getExperienceStep(itemStack, player, 7);
        context.assertEquals(56, stepLevel3, Text.of("experience step insight level 3"));

        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void checkExperienceDrop(TestContext context) {
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ItemStack stack = setMagicalScepterInMainHand(context, player);
        ServerWorld world = context.getWorld();

        PiglinBruteEntity brute = new PiglinBruteEntity(EntityType.PIGLIN_BRUTE, world);

        int dropWithout = brute.getExperienceToDrop(world, player);
        context.assertEquals(20, dropWithout, Text.of("experience drop without enchantment"));

        addInsightEnchantment(context, stack, 1);
        int dropLevel1 = brute.getExperienceToDrop(world, player);
        context.assertEquals(26, dropLevel1, Text.of("experience drop insight level 1"));

        addInsightEnchantment(context, stack, 2);
        int dropLevel2 = brute.getExperienceToDrop(world, player);
        context.assertEquals(33, dropLevel2, Text.of("experience drop insight level 2"));

        addInsightEnchantment(context, stack, 3);
        int dropLevel3 = brute.getExperienceToDrop(world, player);
        context.assertEquals(40, dropLevel3, Text.of("experience drop insight level 3"));

        context.complete();
    }

    private static void addInsightEnchantment(TestContext context, ItemStack itemStack, int level) {
        EnchantmentHelper.apply(itemStack, (builder) ->
                builder.add(getEnchantment(context, ModEnchantments.INSIGHT_KEY), level));
    }
}
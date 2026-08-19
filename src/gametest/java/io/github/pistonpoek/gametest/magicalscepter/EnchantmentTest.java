package io.github.pistonpoek.gametest.magicalscepter;

import io.github.pistonpoek.magicalscepter.enchantment.ModEnchantmentHelper;
import io.github.pistonpoek.magicalscepter.enchantment.ModEnchantments;
import io.github.pistonpoek.magicalscepter.item.ModItems;
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameType;
import java.lang.reflect.Method;

import static io.github.pistonpoek.gametest.magicalscepter.util.ContextUtil.getEnchantment;
import static io.github.pistonpoek.gametest.magicalscepter.util.ContextUtil.setMagicalScepterInMainHand;

public class EnchantmentTest implements CustomTestMethodInvoker {
    @Override
    public void invokeTestMethod(GameTestHelper context, Method method) throws ReflectiveOperationException {
        method.invoke(this, context);
    }

    @GameTest(structure="gametest:template/empty")
    public void checkExperienceStepModifier(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = ModItems.ARCANE_SCEPTER.getDefaultInstance();
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        int stepWithout = ModEnchantmentHelper.getExperienceStep(itemStack, player, 7);
        context.assertValueEqual(7, stepWithout, Component.nullToEmpty("experience step without enchantment"));

        addInsightEnchantment(context, itemStack, 1);
        int stepLevel1 = ModEnchantmentHelper.getExperienceStep(itemStack, player, 7);
        context.assertValueEqual(14, stepLevel1, Component.nullToEmpty("experience step insight level 1"));

        addInsightEnchantment(context, itemStack, 2);
        int stepLevel2 = ModEnchantmentHelper.getExperienceStep(itemStack, player, 7);
        context.assertValueEqual(28, stepLevel2, Component.nullToEmpty("experience step insight level 2"));

        addInsightEnchantment(context, itemStack, 3);
        int stepLevel3 = ModEnchantmentHelper.getExperienceStep(itemStack, player, 7);
        context.assertValueEqual(56, stepLevel3, Component.nullToEmpty("experience step insight level 3"));

        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void checkExperienceDrop(GameTestHelper context) {
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ItemStack stack = setMagicalScepterInMainHand(context, player);
        ServerLevel world = context.getLevel();

        PiglinBrute brute = new PiglinBrute(EntityType.PIGLIN_BRUTE, world);

        int dropWithout = brute.getExperienceReward(world, player);
        context.assertValueEqual(20, dropWithout, Component.nullToEmpty("experience drop without enchantment"));

        addInsightEnchantment(context, stack, 1);
        int dropLevel1 = brute.getExperienceReward(world, player);
        context.assertValueEqual(26, dropLevel1, Component.nullToEmpty("experience drop insight level 1"));

        addInsightEnchantment(context, stack, 2);
        int dropLevel2 = brute.getExperienceReward(world, player);
        context.assertValueEqual(33, dropLevel2, Component.nullToEmpty("experience drop insight level 2"));

        addInsightEnchantment(context, stack, 3);
        int dropLevel3 = brute.getExperienceReward(world, player);
        context.assertValueEqual(40, dropLevel3, Component.nullToEmpty("experience drop insight level 3"));

        context.succeed();
    }

    private static void addInsightEnchantment(GameTestHelper context, ItemStack itemStack, int level) {
        EnchantmentHelper.updateEnchantments(itemStack, (builder) ->
                builder.upgrade(getEnchantment(context, ModEnchantments.INSIGHT_KEY), level));
    }
}
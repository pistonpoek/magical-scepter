package io.github.pistonpoek.gametest.magicalscepter;

import io.github.pistonpoek.gametest.magicalscepter.util.ContextUtil;
import io.github.pistonpoek.magicalscepter.component.ScepterExperienceComponent;
import io.github.pistonpoek.magicalscepter.enchantment.ModEnchantments;
import io.github.pistonpoek.magicalscepter.item.ArcaneScepterItem;
import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.util.PlayerExperience;
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

import java.lang.reflect.Method;

public class ArcaneScepterTest implements CustomTestMethodInvoker {
    @Override
    public void invokeTestMethod(TestContext context, Method method) throws ReflectiveOperationException {
        method.invoke(this, context);
    }

    @GameTest(structure="gametest:template/empty")
    public void chargeArcaneScepter(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, ModItems.ARCANE_SCEPTER.getDefaultStack());
        player.addExperience(14);

        ActionResult result = player.getMainHandStack().use(world, player, Hand.MAIN_HAND);

        context.assertTrue(result.isAccepted(), Text.of("Action result of charging scepter is not accepted"));
        context.assertEquals(ActionResult.SUCCESS.withNewHandStack(ModItems.CHARGED_ARCANE_SCEPTER.getDefaultStack()).toString(),
                result.toString(), Text.of("action result of charging an arcane scepter"));
        context.assertEquals(7, PlayerExperience.getTotalExperience(player),
                Text.of("player experience after charging scepter"));

        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void chargeArcaneScepterWithoutExperience(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.setStackInHand(Hand.MAIN_HAND, ModItems.ARCANE_SCEPTER.getDefaultStack());

        ActionResult result = player.getMainHandStack().use(world, player, Hand.MAIN_HAND);

        context.assertEquals(ActionResult.PASS, result, Text.of("action result"));
        context.assertEquals(ModItems.ARCANE_SCEPTER.getDefaultStack().getItem(),
                player.getMainHandStack().getItem(),
                Text.of("player main hand item"));

        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void chargeArcaneScepterWithoutExperienceInCreative(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.CREATIVE);
        GameMode.CREATIVE.setAbilities(player.getAbilities());
        player.setStackInHand(Hand.MAIN_HAND, ModItems.ARCANE_SCEPTER.getDefaultStack());

        ActionResult result = player.getMainHandStack().use(world, player, Hand.MAIN_HAND);

        context.assertTrue(result.isAccepted(), Text.of("Action result of charging scepter is not accepted"));
        context.assertEquals(ActionResult.SUCCESS.withNewHandStack(ModItems.CHARGED_ARCANE_SCEPTER.getDefaultStack()).toString(),
                result.toString(), Text.of("action result of charging an arcane scepter"));

        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void drainArcaneScepter(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ItemStack itemStack = ModItems.CHARGED_ARCANE_SCEPTER.getDefaultStack();
        player.setStackInHand(Hand.MAIN_HAND, itemStack);

        ActionResult result = player.getMainHandStack().use(world, player, Hand.MAIN_HAND);

        context.assertTrue(result.isAccepted(), Text.of("Action result of draining scepter is not accepted"));
        context.assertEquals(ActionResult.SUCCESS.withNewHandStack(ModItems.ARCANE_SCEPTER.getDefaultStack()).toString(),
                result.toString(), Text.of("action result of draining a charged scepter"));
        context.assertEquals(7, PlayerExperience.getTotalExperience(player),
                Text.of("player experience after draining scepter"));

        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void breakArcaneScepterCharging(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ItemStack itemStack = ModItems.ARCANE_SCEPTER.getDefaultStack();
        itemStack.setDamage(itemStack.getMaxDamage() - 1);
        player.setStackInHand(Hand.MAIN_HAND, itemStack);
        player.addExperience(100);

        ActionResult result = player.getMainHandStack().use(world, player, Hand.MAIN_HAND);

        context.assertTrue(result.isAccepted(), Text.of("Action result of charging scepter is not accepted"));
        context.assertEquals(ActionResult.SUCCESS.withNewHandStack(ModItems.SCEPTER.getDefaultStack()).toString(),
                result.toString(), Text.of("action result of breaking scepter by charging"));

        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void breakArcaneScepterDraining(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ItemStack itemStack = ModItems.ARCANE_SCEPTER.getDefaultStack();
        itemStack.setDamage(itemStack.getMaxDamage() - 1);
        ScepterExperienceComponent.add(itemStack, 70);
        player.setStackInHand(Hand.MAIN_HAND, itemStack);

        ActionResult result = player.getMainHandStack().use(world, player, Hand.MAIN_HAND);

        context.assertTrue(result.isAccepted(), Text.of("Action result of draining scepter is not accepted"));
        context.assertEquals(ActionResult.SUCCESS.withNewHandStack(ModItems.SCEPTER.getDefaultStack()).toString(),
                result.toString(), Text.of("action result of breaking scepter by draining"));

        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void checkDamagedAfterUse(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ItemStack itemStack = ModItems.ARCANE_SCEPTER.getDefaultStack();
        player.setStackInHand(Hand.MAIN_HAND, itemStack);
        player.addExperience(7);

        ActionResult result = player.getMainHandStack().use(world, player, Hand.MAIN_HAND);

        context.assertTrue(result.isAccepted(), Text.of("Action result of charging scepter is not accepted"));
        assert ((ActionResult.Success) result).getNewHandStack() != null;
        ItemStack damagedChargedScepter = ModItems.CHARGED_ARCANE_SCEPTER.getDefaultStack();
        damagedChargedScepter.setDamage(1);
        context.assertEquals(damagedChargedScepter.getItem(),
                ((ActionResult.Success)result).getNewHandStack().getItem(),
                Text.of("item after charging scepter"));
        context.assertEquals(damagedChargedScepter.getDamage(),
                ((ActionResult.Success)result).getNewHandStack().getDamage(),
                Text.of("item stack damage after charging scepter"));

        expectedCooldownMainHand(context, player, 10);

        result = player.getMainHandStack().use(world, player, Hand.MAIN_HAND);

        context.assertTrue(result.isAccepted(), Text.of("Action result of draining scepter is not accepted"));
        assert ((ActionResult.Success) result).getNewHandStack() != null;
        ItemStack damagedArcaneScepter = ModItems.ARCANE_SCEPTER.getDefaultStack();
        damagedArcaneScepter.setDamage(2);
        context.assertEquals(damagedArcaneScepter.getItem(),
                ((ActionResult.Success)result).getNewHandStack().getItem(),
                Text.of("item after draining scepter"));
        context.assertEquals(damagedArcaneScepter.getDamage(),
                ((ActionResult.Success)result).getNewHandStack().getDamage(),
                Text.of("item stack damage after draining scepter"));

        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void checkDamagedAfterUseCreative(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.CREATIVE);
        GameMode.CREATIVE.setAbilities(player.getAbilities());
        ItemStack itemStack = ModItems.ARCANE_SCEPTER.getDefaultStack();
        player.setStackInHand(Hand.MAIN_HAND, itemStack);
        player.addExperience(7);

        ActionResult result = player.getMainHandStack().use(world, player, Hand.MAIN_HAND);

        context.assertTrue(result.isAccepted(), Text.of("Action result of charging scepter is not accepted"));
        assert ((ActionResult.Success) result).getNewHandStack() != null;
        ItemStack damagedChargedScepter = ModItems.CHARGED_ARCANE_SCEPTER.getDefaultStack();
        context.assertEquals(damagedChargedScepter.toString(),
                ((ActionResult.Success)result).getNewHandStack().toString(),
                Text.of("item stack after charging scepter"));

        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void chargingArcaneScepterCooldown(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        player.addExperience(100);
        player.setStackInHand(Hand.MAIN_HAND, ModItems.ARCANE_SCEPTER.getDefaultStack());

        player.getMainHandStack().use(world, player, Hand.MAIN_HAND);

        expectedCooldownMainHand(context, player, 10);

        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void drainingArcaneScepterCooldown(TestContext context) {
        ServerWorld world = context.getWorld();
        PlayerEntity player = context.createMockPlayer(GameMode.SURVIVAL);
        ItemStack itemStack = ModItems.ARCANE_SCEPTER.getDefaultStack();
        ScepterExperienceComponent.add(itemStack, 70);
        player.setStackInHand(Hand.MAIN_HAND, itemStack);

        player.getMainHandStack().use(world, player, Hand.MAIN_HAND);

        expectedCooldownMainHand(context, player, 10);

        context.complete();
    }

    @GameTest(structure="gametest:template/empty")
    public void getReplacementStack(TestContext context) {
        {
            ItemStack itemStack = ModItems.ARCANE_SCEPTER.getDefaultStack();
            context.assertEquals(ItemStack.EMPTY.toString(),
                    ArcaneScepterItem.getReplacementStack(itemStack).toString(),
                    Text.of("updated arcane scepter"));
        }
        {
            ItemStack itemStack = ModItems.ARCANE_SCEPTER.getDefaultStack();
            ScepterExperienceComponent.add(itemStack, 7);
            context.assertEquals(ModItems.CHARGED_ARCANE_SCEPTER.getDefaultStack().getItem(),
                    ArcaneScepterItem.getReplacementStack(itemStack).getItem(),
                    Text.of("updated arcane scepter with experience"));
        }
        {
            ItemStack itemStack = ModItems.CHARGED_ARCANE_SCEPTER.getDefaultStack();
            context.assertEquals(ItemStack.EMPTY.getItem(),
                    ArcaneScepterItem.getReplacementStack(itemStack).getItem(),
                    Text.of("updated charged scepter"));
        }
        {
            ItemStack itemStack = ModItems.CHARGED_ARCANE_SCEPTER.getDefaultStack();
            ScepterExperienceComponent.add(itemStack, -7);
            context.assertEquals(ModItems.ARCANE_SCEPTER.getDefaultStack().getItem(),
                    ArcaneScepterItem.getReplacementStack(itemStack).getItem(),
                    Text.of("updated charged scepter without experience"));
        }
        {
            ItemStack itemStack = ModItems.CHARGED_ARCANE_SCEPTER.getDefaultStack();
            ScepterExperienceComponent.add(itemStack, -7);
            itemStack.addEnchantment(ContextUtil.getEnchantment(context, ModEnchantments.INSIGHT_KEY), 1);
            itemStack.setDamage(17);

            ItemStack expected = ModItems.ARCANE_SCEPTER.getDefaultStack();
            expected.addEnchantment(ContextUtil.getEnchantment(context, ModEnchantments.INSIGHT_KEY), 1);
            expected.setDamage(17);

            context.assertEquals(expected.getItem(),
                    ArcaneScepterItem.getReplacementStack(itemStack).getItem(),
                    Text.of("updated charged scepter without experience keeping components"));
        }

        context.complete();
    }

    private void expectedCooldownMainHand(TestContext context, PlayerEntity player, int expected) {
        ItemCooldownManager cooldownManager = player.getItemCooldownManager();
        ItemStack itemStack = player.getMainHandStack();

        for (int i = 0; i < expected - 1; i++) {
            cooldownManager.update();
        }

        context.assertTrue(cooldownManager.isCoolingDown(itemStack),
                Text.of("Charged scepter is not cooling down for %s ticks".formatted(expected)));

        cooldownManager.update();

        context.assertFalse(cooldownManager.isCoolingDown(itemStack),
                Text.of("Charged scepter is cooling down for more than %s ticks".formatted(expected)));
    }
}

package io.github.pistonpoek.gametest.magicalscepter;

import io.github.pistonpoek.gametest.magicalscepter.util.ContextUtil;
import io.github.pistonpoek.magicalscepter.component.ScepterExperienceComponent;
import io.github.pistonpoek.magicalscepter.enchantment.ModEnchantments;
import io.github.pistonpoek.magicalscepter.item.ArcaneScepterItem;
import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.util.PlayerExperience;
import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import java.lang.reflect.Method;

public class ArcaneScepterTest implements CustomTestMethodInvoker {
    @Override
    public void invokeTestMethod(GameTestHelper context, Method method) throws ReflectiveOperationException {
        method.invoke(this, context);
    }

    @GameTest(structure="gametest:template/empty")
    public void chargeArcaneScepter(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, ModItems.ARCANE_SCEPTER.getDefaultInstance());
        player.giveExperiencePoints(14);

        InteractionResult result = player.getMainHandItem().use(world, player, InteractionHand.MAIN_HAND);

        context.assertTrue(result.consumesAction(), Component.nullToEmpty("Action result of charging scepter is not accepted"));
        context.assertValueEqual(InteractionResult.SUCCESS.heldItemTransformedTo(ModItems.CHARGED_ARCANE_SCEPTER.getDefaultInstance()).toString(),
                result.toString(), Component.nullToEmpty("action result of charging an arcane scepter"));
        context.assertValueEqual(7, PlayerExperience.getTotalExperience(player),
                Component.nullToEmpty("player experience after charging scepter"));

        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void chargeArcaneScepterWithoutExperience(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, ModItems.ARCANE_SCEPTER.getDefaultInstance());

        InteractionResult result = player.getMainHandItem().use(world, player, InteractionHand.MAIN_HAND);

        context.assertValueEqual(InteractionResult.PASS, result, Component.nullToEmpty("action result"));
        context.assertValueEqual(ModItems.ARCANE_SCEPTER.getDefaultInstance().getItem(),
                player.getMainHandItem().getItem(),
                Component.nullToEmpty("player main hand item"));

        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void chargeArcaneScepterWithoutExperienceInCreative(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.CREATIVE);
        GameType.CREATIVE.updatePlayerAbilities(player.getAbilities());
        player.setItemInHand(InteractionHand.MAIN_HAND, ModItems.ARCANE_SCEPTER.getDefaultInstance());

        InteractionResult result = player.getMainHandItem().use(world, player, InteractionHand.MAIN_HAND);

        context.assertTrue(result.consumesAction(), Component.nullToEmpty("Action result of charging scepter is not accepted"));
        context.assertValueEqual(InteractionResult.SUCCESS.heldItemTransformedTo(ModItems.CHARGED_ARCANE_SCEPTER.getDefaultInstance()).toString(),
                result.toString(), Component.nullToEmpty("action result of charging an arcane scepter"));

        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void drainArcaneScepter(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = ModItems.CHARGED_ARCANE_SCEPTER.getDefaultInstance();
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        InteractionResult result = player.getMainHandItem().use(world, player, InteractionHand.MAIN_HAND);

        context.assertTrue(result.consumesAction(), Component.nullToEmpty("Action result of draining scepter is not accepted"));
        context.assertValueEqual(InteractionResult.SUCCESS.heldItemTransformedTo(ModItems.ARCANE_SCEPTER.getDefaultInstance()).toString(),
                result.toString(), Component.nullToEmpty("action result of draining a charged scepter"));
        context.assertValueEqual(7, PlayerExperience.getTotalExperience(player),
                Component.nullToEmpty("player experience after draining scepter"));

        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void breakArcaneScepterCharging(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = ModItems.ARCANE_SCEPTER.getDefaultInstance();
        itemStack.setDamageValue(itemStack.getMaxDamage() - 1);
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        player.giveExperiencePoints(100);

        InteractionResult result = player.getMainHandItem().use(world, player, InteractionHand.MAIN_HAND);

        context.assertTrue(result.consumesAction(), Component.nullToEmpty("Action result of charging scepter is not accepted"));
        context.assertValueEqual(InteractionResult.SUCCESS.heldItemTransformedTo(ModItems.SCEPTER.getDefaultInstance()).toString(),
                result.toString(), Component.nullToEmpty("action result of breaking scepter by charging"));

        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void breakArcaneScepterDraining(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = ModItems.ARCANE_SCEPTER.getDefaultInstance();
        itemStack.setDamageValue(itemStack.getMaxDamage() - 1);
        ScepterExperienceComponent.add(itemStack, 70);
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        InteractionResult result = player.getMainHandItem().use(world, player, InteractionHand.MAIN_HAND);

        context.assertTrue(result.consumesAction(), Component.nullToEmpty("Action result of draining scepter is not accepted"));
        context.assertValueEqual(InteractionResult.SUCCESS.heldItemTransformedTo(ModItems.SCEPTER.getDefaultInstance()).toString(),
                result.toString(), Component.nullToEmpty("action result of breaking scepter by draining"));

        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void checkDamagedAfterUse(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = ModItems.ARCANE_SCEPTER.getDefaultInstance();
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        player.giveExperiencePoints(7);

        InteractionResult result = player.getMainHandItem().use(world, player, InteractionHand.MAIN_HAND);

        context.assertTrue(result.consumesAction(), Component.nullToEmpty("Action result of charging scepter is not accepted"));
        assert ((InteractionResult.Success) result).heldItemTransformedTo() != null;
        ItemStack damagedChargedScepter = ModItems.CHARGED_ARCANE_SCEPTER.getDefaultInstance();
        damagedChargedScepter.setDamageValue(1);
        context.assertValueEqual(damagedChargedScepter.getItem(),
                ((InteractionResult.Success)result).heldItemTransformedTo().getItem(),
                Component.nullToEmpty("item after charging scepter"));
        context.assertValueEqual(damagedChargedScepter.getDamageValue(),
                ((InteractionResult.Success)result).heldItemTransformedTo().getDamageValue(),
                Component.nullToEmpty("item stack damage after charging scepter"));

        expectedCooldownMainHand(context, player, 10);

        result = player.getMainHandItem().use(world, player, InteractionHand.MAIN_HAND);

        context.assertTrue(result.consumesAction(), Component.nullToEmpty("Action result of draining scepter is not accepted"));
        assert ((InteractionResult.Success) result).heldItemTransformedTo() != null;
        ItemStack damagedArcaneScepter = ModItems.ARCANE_SCEPTER.getDefaultInstance();
        damagedArcaneScepter.setDamageValue(2);
        context.assertValueEqual(damagedArcaneScepter.getItem(),
                ((InteractionResult.Success)result).heldItemTransformedTo().getItem(),
                Component.nullToEmpty("item after draining scepter"));
        context.assertValueEqual(damagedArcaneScepter.getDamageValue(),
                ((InteractionResult.Success)result).heldItemTransformedTo().getDamageValue(),
                Component.nullToEmpty("item stack damage after draining scepter"));

        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void checkDamagedAfterUseCreative(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.CREATIVE);
        GameType.CREATIVE.updatePlayerAbilities(player.getAbilities());
        ItemStack itemStack = ModItems.ARCANE_SCEPTER.getDefaultInstance();
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        player.giveExperiencePoints(7);

        InteractionResult result = player.getMainHandItem().use(world, player, InteractionHand.MAIN_HAND);

        context.assertTrue(result.consumesAction(), Component.nullToEmpty("Action result of charging scepter is not accepted"));
        assert ((InteractionResult.Success) result).heldItemTransformedTo() != null;
        ItemStack damagedChargedScepter = ModItems.CHARGED_ARCANE_SCEPTER.getDefaultInstance();
        context.assertValueEqual(damagedChargedScepter.toString(),
                ((InteractionResult.Success)result).heldItemTransformedTo().toString(),
                Component.nullToEmpty("item stack after charging scepter"));

        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void chargingArcaneScepterCooldown(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        player.giveExperiencePoints(100);
        player.setItemInHand(InteractionHand.MAIN_HAND, ModItems.ARCANE_SCEPTER.getDefaultInstance());

        player.getMainHandItem().use(world, player, InteractionHand.MAIN_HAND);

        expectedCooldownMainHand(context, player, 10);

        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void drainingArcaneScepterCooldown(GameTestHelper context) {
        ServerLevel world = context.getLevel();
        Player player = context.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = ModItems.ARCANE_SCEPTER.getDefaultInstance();
        ScepterExperienceComponent.add(itemStack, 70);
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);

        player.getMainHandItem().use(world, player, InteractionHand.MAIN_HAND);

        expectedCooldownMainHand(context, player, 10);

        context.succeed();
    }

    @GameTest(structure="gametest:template/empty")
    public void getReplacementStack(GameTestHelper context) {
        {
            ItemStack itemStack = ModItems.ARCANE_SCEPTER.getDefaultInstance();
            context.assertValueEqual(ItemStack.EMPTY.toString(),
                    ArcaneScepterItem.getReplacementStack(itemStack).toString(),
                    Component.nullToEmpty("updated arcane scepter"));
        }
        {
            ItemStack itemStack = ModItems.ARCANE_SCEPTER.getDefaultInstance();
            ScepterExperienceComponent.add(itemStack, 7);
            context.assertValueEqual(ModItems.CHARGED_ARCANE_SCEPTER.getDefaultInstance().getItem(),
                    ArcaneScepterItem.getReplacementStack(itemStack).getItem(),
                    Component.nullToEmpty("updated arcane scepter with experience"));
        }
        {
            ItemStack itemStack = ModItems.CHARGED_ARCANE_SCEPTER.getDefaultInstance();
            context.assertValueEqual(ItemStack.EMPTY.getItem(),
                    ArcaneScepterItem.getReplacementStack(itemStack).getItem(),
                    Component.nullToEmpty("updated charged scepter"));
        }
        {
            ItemStack itemStack = ModItems.CHARGED_ARCANE_SCEPTER.getDefaultInstance();
            ScepterExperienceComponent.add(itemStack, -7);
            context.assertValueEqual(ModItems.ARCANE_SCEPTER.getDefaultInstance().getItem(),
                    ArcaneScepterItem.getReplacementStack(itemStack).getItem(),
                    Component.nullToEmpty("updated charged scepter without experience"));
        }
        {
            ItemStack itemStack = ModItems.CHARGED_ARCANE_SCEPTER.getDefaultInstance();
            ScepterExperienceComponent.add(itemStack, -7);
            itemStack.enchant(ContextUtil.getEnchantment(context, ModEnchantments.INSIGHT_KEY), 1);
            itemStack.setDamageValue(17);

            ItemStack expected = ModItems.ARCANE_SCEPTER.getDefaultInstance();
            expected.enchant(ContextUtil.getEnchantment(context, ModEnchantments.INSIGHT_KEY), 1);
            expected.setDamageValue(17);

            context.assertValueEqual(expected.getItem(),
                    ArcaneScepterItem.getReplacementStack(itemStack).getItem(),
                    Component.nullToEmpty("updated charged scepter without experience keeping components"));
        }

        context.succeed();
    }

    private void expectedCooldownMainHand(GameTestHelper context, Player player, int expected) {
        ItemCooldowns cooldownManager = player.getCooldowns();
        ItemStack itemStack = player.getMainHandItem();

        for (int i = 0; i < expected - 1; i++) {
            cooldownManager.tick();
        }

        context.assertTrue(cooldownManager.isOnCooldown(itemStack),
                Component.nullToEmpty("Charged scepter is not cooling down for %s ticks".formatted(expected)));

        cooldownManager.tick();

        context.assertFalse(cooldownManager.isOnCooldown(itemStack),
                Component.nullToEmpty("Charged scepter is cooling down for more than %s ticks".formatted(expected)));
    }
}

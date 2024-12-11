package io.github.pistonpoek.magicalscepter.network.handler;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import io.github.pistonpoek.magicalscepter.item.AttackItem;
import io.github.pistonpoek.magicalscepter.network.packet.AttackItemPayload;

public class AttackItemHandler implements ServerPlayNetworking.PlayPayloadHandler<AttackItemPayload> {
    @Override
    public void receive(AttackItemPayload payload, ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();

        // Filter for attack items.
        Item item = player.getMainHandStack().getItem();
        if (!(item instanceof AttackItem)) {
            return;
        }

        player.updateLastActionTime();
        player.resetLastAttackedTicks();

        // Update player rotation before attacking with item.
        float yaw = MathHelper.wrapDegrees(payload.yaw());
        float pitch = MathHelper.wrapDegrees(payload.pitch());
        if (yaw != player.getYaw() || pitch != player.getPitch()) {
            player.setAngles(yaw, pitch);
        }

        ActionResult actionResult = attackWithItem(player);
        if (actionResult == ActionResult.SUCCESS || actionResult == ActionResult.SUCCESS_SERVER) {
            player.swingHand(Hand.MAIN_HAND, true);
        }
    }

    private ActionResult attackWithItem(ServerPlayerEntity player) {
        ItemStack stack = player.getMainHandStack();
        Hand hand = Hand.MAIN_HAND;

        if (player.isSpectator()) {
            return ActionResult.PASS;
        } else if (player.getItemCooldownManager().isCoolingDown(stack)) {
            return ActionResult.PASS;
        } else if (!(stack.getItem() instanceof AttackItem)) {
            return ActionResult.PASS;
        }

        int stackCount = stack.getCount();
        int stackDamage = stack.getDamage();

        ActionResult result =
                ((AttackItem)stack.getItem()).attack(player.getWorld(), player);
        ItemStack resultStack = result instanceof ActionResult.Success success ? success.getNewHandStack() : stack;

        if (resultStack == stack && resultStack.getCount() == stackCount &&
                resultStack.getMaxUseTime(player) <= 0 && resultStack.getDamage() == stackDamage) {
            return result;
        } else if (result == ActionResult.FAIL &&
                resultStack.getMaxUseTime(player) > 0 && !player.isUsingItem()) {
            return result;
        }

        if (stack != resultStack) {
            player.setStackInHand(hand, resultStack);
        }

        if (resultStack.isEmpty()) {
            player.setStackInHand(hand, ItemStack.EMPTY);
        }

        if (!player.isUsingItem()) {
            player.playerScreenHandler.syncState();
        }

        return result;
    }
}

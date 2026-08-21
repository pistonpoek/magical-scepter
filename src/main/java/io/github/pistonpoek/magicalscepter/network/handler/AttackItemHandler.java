package io.github.pistonpoek.magicalscepter.network.handler;

import io.github.pistonpoek.magicalscepter.item.AttackItem;
import io.github.pistonpoek.magicalscepter.network.packet.AttackItemPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SwingAnimation;

/**
 * Packet handler for triggering the attack of attack items.
 *
 * @see AttackItem
 */
public class AttackItemHandler implements ServerPlayNetworking.PlayPayloadHandler<AttackItemPayload> {
    @Override
    public void receive(AttackItemPayload payload, ServerPlayNetworking.Context context) {
        ServerPlayer player = context.player();

        // Filter for attack items.
        Item item = player.getMainHandItem().getItem();
        if (!(item instanceof AttackItem)) {
            return;
        }

        player.resetLastActionTime();

        // Update player rotation before attacking with item.
        float yaw = Mth.wrapDegrees(payload.yaw());
        float pitch = Mth.wrapDegrees(payload.pitch());
        if (yaw != player.getYRot() || pitch != player.getXRot()) {
            player.absSnapRotationTo(yaw, pitch);
        }

        InteractionResult actionResult = attackWithItem(player);
        if (actionResult == InteractionResult.SUCCESS || actionResult == InteractionResult.SUCCESS_SERVER) {
            // TODO Update code to use swing animation instead of ignoring the new swing animation class.
            player.swing(InteractionHand.MAIN_HAND, SwingAnimation.DEFAULT, true);
        }
    }

    /**
     * Try to attack with the current item of the specified player.
     *
     * @param player Server player entity that should try to attack.
     * @return Action result of the attack.
     */
    private InteractionResult attackWithItem(ServerPlayer player) {
        ItemStack stack = player.getMainHandItem();
        InteractionHand hand = InteractionHand.MAIN_HAND;

        if (player.isSpectator()) {
            return InteractionResult.PASS;
        } else if (player.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.PASS;
        } else if (!(stack.getItem() instanceof AttackItem)) {
            return InteractionResult.PASS;
        }

        int stackCount = stack.getCount();
        int stackDamage = stack.getDamageValue();

        InteractionResult result =
                ((AttackItem) stack.getItem()).attack(player.level(), player);
        ItemStack resultStack = result instanceof InteractionResult.Success success ? success.heldItemTransformedTo() : stack;

        if (resultStack == stack && resultStack.getCount() == stackCount &&
                resultStack.getUseDuration(player) <= 0 && resultStack.getDamageValue() == stackDamage) {
            return result;
        } else if (result == InteractionResult.FAIL &&
                resultStack.getUseDuration(player) > 0 && !player.isUsingItem()) {
            return result;
        }

        if (stack != resultStack) {
            player.setItemInHand(hand, resultStack);
        }

        if (resultStack.isEmpty()) {
            player.setItemInHand(hand, ItemStack.EMPTY);
        }

        if (!player.isUsingItem()) {
            player.inventoryMenu.sendAllDataToRemote();
        }

        return result;
    }
}

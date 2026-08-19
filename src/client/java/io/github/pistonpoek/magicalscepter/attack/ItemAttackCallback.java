package io.github.pistonpoek.magicalscepter.attack;

import io.github.pistonpoek.magicalscepter.item.AttackItem;
import io.github.pistonpoek.magicalscepter.network.packet.AttackItemPayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class ItemAttackCallback implements ClientPreAttackCallback {
    @Override
    public boolean onClientPlayerPreAttack(Minecraft client, LocalPlayer player, int clickCount) {
        // Return false when the player is spectator.
        if (player.isSpectator()) {
            return false;
        }

        // Filter for attack items.
        ItemStack stack = player.getMainHandItem();
        Item item = stack.getItem();
        if (!(item instanceof AttackItem)) {
            return false;
        }

        // Not allowed to use the item if the item is cooling down.
        if (player.getCooldowns().isOnCooldown(stack)) {
            return false; // Allow other attack actions to happen.
        }

        // Process attack item when clicked and not when held.
        if (clickCount != 0) {
            InteractionResult result = processAttackItem(client, player, item);

            // Cancel other attack actions if result is not pass.
            return result != InteractionResult.PASS;
        }

        // Return false to allow other attack actions from happening.
        return false;
    }

    /**
     * Process the attack of the specified player with the specified item.
     *
     * @param client Client to process the attack on.
     * @param player Player that performs the attack.
     * @param item   Item that is being attacked with.
     * @return Result of the attack action for the player.
     */
    private InteractionResult processAttackItem(Minecraft client, LocalPlayer player, Item item) {
        // Check the result for the attack item.
        InteractionResult result = ((AttackItem) item)
                .attack(player.level(), player);

        // Render attack use if attack item use is accepted.
        if (result.consumesAction()) {
            renderAttackUse(client, player, result == InteractionResult.SUCCESS);

            // Send an attack item packet to invoke the server for it.
            ClientPlayNetworking.send(new AttackItemPayload(player.getYRot(), player.getXRot()));
        }
        return result;
    }

    /**
     * Render the attack use of the specified player.
     *
     * @param client          Client to render the attack with.
     * @param player          Player that performs the attack.
     * @param shouldSwingHand Truth assignment, if the players hand should swing.
     */
    private void renderAttackUse(Minecraft client, LocalPlayer player, boolean shouldSwingHand) {
        if (shouldSwingHand) {
            player.swing(InteractionHand.MAIN_HAND);
        }

        client.gameRenderer.itemInHandRenderer.itemUsed(InteractionHand.MAIN_HAND);
    }
}

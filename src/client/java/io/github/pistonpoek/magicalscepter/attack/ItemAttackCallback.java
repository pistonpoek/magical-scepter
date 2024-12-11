package io.github.pistonpoek.magicalscepter.attack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import io.github.pistonpoek.magicalscepter.item.AttackItem;
import io.github.pistonpoek.magicalscepter.network.packet.AttackItemPayload;

@Environment(EnvType.CLIENT)
public class ItemAttackCallback implements ClientPreAttackCallback {
    @Override
    public boolean onClientPlayerPreAttack(MinecraftClient client, ClientPlayerEntity player, int clickCount) {
        // Return false when the player is spectator.
        if (player.isSpectator()) {
            return false;
        }

        // Filter for attack items.
        ItemStack stack = player.getMainHandStack();
        Item item = stack.getItem();
        if (!(item instanceof AttackItem)) {
            return false;
        }

        // Not allowed to use the item if the item is cooling down.
        if (player.getItemCooldownManager().isCoolingDown(stack)) {
            return false; // Allow other attack actions to happen.
        }

        // Process attack item when clicked and not when held.
        if (clickCount != 0) {
            ActionResult result = processAttackItem(client, player, item);

            // Cancel other attack actions if result is not pass.
            return result != ActionResult.PASS;
        }

        // Return false to allow other attack actions from happening.
        return false;
    }

    private ActionResult processAttackItem(MinecraftClient client, ClientPlayerEntity player, Item item) {
        // Check the result for the attack item.
        ActionResult result = ((AttackItem) item)
                .attack(player.getWorld(), player);

        // Render attack use if attack item use is accepted.
        if (result.isAccepted()) {
            renderAttackUse(client, player, result == ActionResult.SUCCESS);

            // Send an attack item packet to invoke the server for it.
            ClientPlayNetworking.send(new AttackItemPayload(player.getYaw(), player.getPitch()));
        }
        return result;
    }

    private void renderAttackUse(MinecraftClient client, ClientPlayerEntity player, boolean shouldSwingHand) {
        if (shouldSwingHand) {
            player.swingHand(Hand.MAIN_HAND);
        }

        client.gameRenderer.firstPersonRenderer.resetEquipProgress(Hand.MAIN_HAND);
    }
}

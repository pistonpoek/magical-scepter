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
        if (!(item instanceof AttackItem attackItem)) {
            return false;
        }

        // Not allowed to use the item if the item is cooling down.
        if (player.getItemCooldownManager().isCoolingDown(stack)) {
            return false;
        }

        // Process attack item when clicked and not when held.
        if (clickCount != 0) {
            processAttackItem(client, player, item);
        }

        // Return true to prevent other attack actions from happening.
        return true;
    }

    private void processAttackItem(MinecraftClient client, ClientPlayerEntity player, Item item) {
        // Check the result for the attack item.
        ActionResult result = ((AttackItem) item)
                .attack(player.getWorld(), player);

        // TODO improve method see ItemStack use() and ServerPlayerInteractionManager interactItem().

        // Render attack use if attack item use is accepted.
        if (result.isAccepted()) {
            renderAttackUse(client, player, true);

            // Send an attack item packet to invoke the server for it.
            ClientPlayNetworking.send(new AttackItemPayload(player.getYaw(), player.getPitch()));
        }
    }

    private void renderAttackUse(MinecraftClient client, ClientPlayerEntity player, boolean shouldSwingHand) {
        if (shouldSwingHand) {
            player.swingHand(Hand.MAIN_HAND);
        }

        client.gameRenderer.firstPersonRenderer.resetEquipProgress(Hand.MAIN_HAND);
    }
}

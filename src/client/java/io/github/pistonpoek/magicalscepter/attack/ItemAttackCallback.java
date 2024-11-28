package io.github.pistonpoek.magicalscepter.attack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
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
        Item item = player.getMainHandStack().getItem();
        if (!(item instanceof AttackItem attackItem)) {
            return false;
        }

        // Not allowed to use the item if the item is cooling down.
        if (player.getItemCooldownManager().isCoolingDown(item)) {
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
        TypedActionResult<ItemStack> result = ((AttackItem) item)
                .attack(player.getWorld(), player);

        // Render attack use if attack item use is accepted.
        if (result.getResult().isAccepted()) {
            renderAttackUse(client, player, result.getResult().shouldSwingHand());
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

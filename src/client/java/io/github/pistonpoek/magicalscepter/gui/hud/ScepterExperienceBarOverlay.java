package io.github.pistonpoek.magicalscepter.gui.hud;

import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import io.github.pistonpoek.magicalscepter.enchantment.ModEnchantmentHelper;
import io.github.pistonpoek.magicalscepter.item.ArcaneScepterItem;
import io.github.pistonpoek.magicalscepter.util.PlayerExperience;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;

public class ScepterExperienceBarOverlay {
    /**
     * Render spell experience bar for a player holding a scepter.
     *
     * @param context Draw context to use for rendering.
     * @param player Player to reference the experience data for.
     * @param item Item to get scepter contents from for experience values.
     * @param x Horizontal position of the experience bar on the screen.
     * @param y Vertical position of the experience bar on the screen.
     *
     * @return Truth assignment, if overlay was added excluding cost indication.
     */
    public static boolean render(DrawContext context, ItemStack item, ClientPlayerEntity player, int x, int y) {
        int playerExperience = PlayerExperience.getTotalExperience(player);
        int scepterExperience = item.getOrDefault(ModDataComponentTypes.EXPERIENCE, 0);
        int step = ModEnchantmentHelper.getExperienceStep(item, player, ArcaneScepterItem.EXPERIENCE_STEP);

        // Check if the player and scepter has sufficient experience to transfer to determine what indication to render.
        if (playerExperience >= step) {
            ScepterUseIndicationBar.render(context, player, step, x, y);
        } else if (scepterExperience >= step) {
            ScepterGainIndicationBar.render(context, player, step, x, y);
        } else {
            ScepterCostIndicationBar.render(context, player, step, x, y);
            return false;
        }
        return true;
    }
}

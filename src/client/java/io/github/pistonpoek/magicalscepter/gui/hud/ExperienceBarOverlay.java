package io.github.pistonpoek.magicalscepter.gui.hud;

import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;

public class ExperienceBarOverlay {
    /**
     * Render spell experience bar for a player holding a scepter.
     *
     * @param context Draw context to use for rendering.
     * @param player Player to reference the experience data for.
     * @param item Item to get overlay contents from for experience values.
     * @param x Horizontal position of the experience bar on the screen.
     *
     * @return Truth assignment, if overlay was added.
     */
    public static boolean render(DrawContext context, ItemStack item, ClientPlayerEntity player, int x) {
        // Compute the y position of the experience bar.
        int y = context.getScaledWindowHeight() - 32 + 3;

        if (ScepterHelper.IS_MAGICAL_SCEPTER.test(item)) {
            return SpellExperienceBarOverlay.render(context, item, player, x, y);
        } else if (ScepterHelper.IS_ARCANE_SCEPTER.test(item)) {
            return ScepterExperienceBarOverlay.render(context, item, player, x, y);
        }
        return false;
    }
}

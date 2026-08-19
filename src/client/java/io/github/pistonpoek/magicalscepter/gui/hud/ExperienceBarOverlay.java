package io.github.pistonpoek.magicalscepter.gui.hud;

import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;

public class ExperienceBarOverlay {
    /**
     * Render spell experience bar for a player holding a scepter.
     *
     * @param context Draw context to use for rendering.
     * @param player  Player to reference the experience data for.
     * @param item    Item to get overlay contents from for experience values.
     * @param x       Horizontal position of the experience bar on the screen.
     * @param y       Vertical position of the experience bar on the screen.
     * @return Truth assignment, if overlay was added.
     */
    public static boolean render(GuiGraphicsExtractor context, ItemStack item, LocalPlayer player, int x, int y) {
        if (ScepterHelper.MAGICAL_SCEPTER.test(item)) {
            return SpellExperienceBarOverlay.render(context, item, player, x, y);
        } else if (ScepterHelper.ARCANE_SCEPTER.test(item)) {
            return ScepterExperienceBarOverlay.render(context, item, player, x, y);
        }
        return false;
    }
}

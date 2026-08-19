package io.github.pistonpoek.magicalscepter.gui.hud;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.contextualbar.ContextualBarRenderer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;

public class ScepterUseIndicationBar {
    private static final Identifier TEXTURE = ModIdentifier.of("hud/experience_bar_use");

    /**
     * Render the scepter use indication bar.
     *
     * @param context        Draw context to use for rendering.
     * @param player         Player to reference the experience data for.
     * @param experienceStep Experience step to determine bar size for.
     * @param x              Horizontal position of the experience bar on the screen.
     * @param y              Vertical position of the experience bar on the screen.
     */
    public static void render(GuiGraphicsExtractor context, LocalPlayer player,
                              int experienceStep, int x, int y) {
        int progress = ExperienceBar.getPixelProgress(player);
        float use_progress = experienceStep / (float) player.getXpNeededForNextLevel();
        int use_part = Math.round(use_progress * (ContextualBarRenderer.WIDTH + 1.0F));
        ExperienceBar.renderSection(context, TEXTURE, x, y, progress - use_part, progress);
    }
}

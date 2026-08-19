package io.github.pistonpoek.magicalscepter.gui.hud;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.contextualbar.ContextualBarRenderer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public class ExperienceBar {
    /**
     * Render an experience bar section with the specified texture and positions.
     *
     * @param context Draw context to use for rendering.
     * @param texture Identifier of the texture to use.
     * @param x       Horizontal position of the experience bar on the screen.
     * @param y       Vertical position of the experience bar on the screen.
     * @param start   Horizontal position to start the section to render at.
     * @param end     Horizontal position to end the section to render at.
     */
    static void renderSection(GuiGraphicsExtractor context, Identifier texture, int x, int y, int start, int end) {
        int startX = Math.clamp(start, 0, ContextualBarRenderer.WIDTH);
        int width = Math.clamp(end - startX, 0, ContextualBarRenderer.WIDTH - startX);
        if (width <= 0) {
            return;
        }
        context.blitSprite(RenderPipelines.GUI_TEXTURED, texture, ContextualBarRenderer.WIDTH, ContextualBarRenderer.HEIGHT,
                startX, 0, x + startX, y, width, ContextualBarRenderer.HEIGHT);
    }

    /**
     * Get the current pixel progress of the experience bar for the specified player.
     *
     * @param player Player to get the experience bar progress for.
     * @return Amount of pixels that make up the experience progress in pixels for the player.
     */
    static int getPixelProgress(LocalPlayer player) {
        return (int) (player.experienceProgress * 183.0F);
    }
}

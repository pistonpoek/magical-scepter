package io.github.pistonpoek.magicalscepter.gui.hud;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.bar.Bar;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;

public class ExperienceBar {
    /**
     * Render an experience bar section with the specified texture and positions.
     *
     * @param context Draw context to use for rendering.
     * @param texture Identifier of the texture to use.
     * @param x Horizontal position of the experience bar on the screen.
     * @param y Vertical position of the experience bar on the screen.
     * @param start Horizontal position to start the section to render at.
     * @param end Horizontal position to end the section to render at.
     */
    static void renderSection(DrawContext context, Identifier texture, int x, int y, int start, int end) {
        int startX = Math.clamp(start, 0, Bar.WIDTH);
        int width = Math.clamp(end - startX, 0, Bar.WIDTH - startX);
        if (width <= 0) {
            return;
        }
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, texture, Bar.WIDTH, Bar.HEIGHT,
                startX, 0, x + startX, y, width, Bar.HEIGHT);
    }

    /**
     * Get the current pixel progress of the experience bar for the specified player.
     *
     * @param player Player to get the experience bar progress for.
     * @return Amount of pixels that make up the experience progress in pixels for the player.
     */
    static int getPixelProgress(ClientPlayerEntity player) {
        return (int)(player.experienceProgress * 183.0F);
    }
}

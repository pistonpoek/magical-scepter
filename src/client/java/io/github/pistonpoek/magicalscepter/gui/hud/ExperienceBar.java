package io.github.pistonpoek.magicalscepter.gui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class ExperienceBar {
    /**
     * Pixel width of the experience bar rendering.
     */
    static final int WIDTH = 182;
    /**
     * Pixel height of the experience bar rendering.
     */
    static final int HEIGHT = 5;

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
        int startX = Math.clamp(start, 0, WIDTH);
        int width = Math.clamp(end - startX, 0, WIDTH - startX);
        if (width <= 0) {
            return;
        }
        RenderSystem.enableBlend();
        context.drawGuiTexture(RenderLayer::getGuiTextured, texture, WIDTH, HEIGHT,
                startX, 0, x + startX, y, width, HEIGHT);
        RenderSystem.disableBlend();
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

package io.github.pistonpoek.magicalscepter.gui.hud;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;

public class ScepterGainIndicationBar {
    private static final Identifier TEXTURE = ModIdentifier.of("hud/experience_bar_gain");

    /**
     * Render the scepter gain indication bar.
     *
     * @param context Draw context to use for rendering.
     * @param player Player to reference the experience data for.
     * @param experienceStep Experience step to determine bar size for.
     * @param x Horizontal position of the experience bar on the screen.
     * @param y Vertical position of the experience bar on the screen.
     */
    public static void render(DrawContext context, ClientPlayerEntity player,
                              int experienceStep, int x, int y) {
        int progress = ExperienceBar.getPixelProgress(player);
        float gain_progress = experienceStep / (float)player.getNextLevelExperience();
        int gain_part = Math.round(gain_progress * (ExperienceBar.WIDTH + 1.0F));
        ExperienceBar.renderSection(context, TEXTURE, x, y, progress, progress + gain_part);
    }
}

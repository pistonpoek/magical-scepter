package io.github.pistonpoek.magicalscepter.gui.hud;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import io.github.pistonpoek.magicalscepter.util.PlayerExperience;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.bar.Bar;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;

/**
 * TODO
 */
public class ScepterCostIndicationBar {
    private static final Identifier TEXTURE = ModIdentifier.of("hud/experience_bar_cost");

    /**
     * Render the scepter cost indication bar.
     *
     * @param context        Draw context to use for rendering.
     * @param player         Player to reference the experience data for.
     * @param experienceStep Experience step to determine bar size for.
     * @param x              Horizontal position of the experience bar on the screen.
     * @param y              Vertical position of the experience bar on the screen.
     */
    public static void render(DrawContext context, ClientPlayerEntity player,
                              int experienceStep, int x, int y) {
        int progress = ExperienceBar.getPixelProgress(player);
        float cost_progress = (experienceStep - PlayerExperience.getTotalExperience(player))
                / (float) player.getNextLevelExperience();
        int cost_part = Math.round(cost_progress * (Bar.WIDTH + 1.0F));
        ExperienceBar.renderSection(context, TEXTURE, x, y, progress, progress + cost_part);
    }
}

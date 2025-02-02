package io.github.pistonpoek.magicalscepter.gui.hud;

import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import io.github.pistonpoek.magicalscepter.util.PlayerExperience;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;

public class SpellCostIndicationBar {
    private static final Identifier TEXTURE = ModIdentifier.of("hud/experience_bar_cost");

    /**
     * Render the spell cost indication bar.
     *
     * @param context Draw context to use for rendering.
     * @param player Player to reference the experience data for.
     * @param scepterContents Scepter contents to get the experience casting price for.
     * @param x Horizontal position of the experience bar on the screen.
     * @param y Vertical position of the experience bar on the screen.
     */
    public static void render(DrawContext context, ClientPlayerEntity player,
                               ScepterContentsComponent scepterContents, int x, int y) {
        int progress = ExperienceBar.getPixelProgress(player);
        float cost_progress = (scepterContents.getExperienceCost() - PlayerExperience.getTotalExperience(player))
                / (float)player.getNextLevelExperience();
        int cost_part = Math.round(cost_progress * (ExperienceBar.WIDTH + 1.0F));
        ExperienceBar.renderSection(context, TEXTURE, x, y, progress, progress + cost_part);
    }
}

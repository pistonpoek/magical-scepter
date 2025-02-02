package io.github.pistonpoek.magicalscepter.gui.hud;

import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;

public class SpellUseIndicationBar {
    private static final Identifier TEXTURE = ModIdentifier.of("hud/experience_bar_use");

    /**
     * Render the spell use indication bar.
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
        float use_progress = scepterContents.getExperienceCost() / (float)player.getNextLevelExperience();
        int use_part = Math.round(use_progress * (ExperienceBar.WIDTH + 1.0F));
        ExperienceBar.renderSection(context, TEXTURE, x, y, progress - use_part, progress);
    }
}

package io.github.pistonpoek.magicalscepter.gui.hud;

import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.Optional;

public class SpellExperienceBarOverlay {
    /**
     * Render spell experience bar for a player holding a scepter.
     *
     * @param context Draw context to use for rendering.
     * @param player Player to reference the experience data for.
     * @param item Item to get scepter contents from for experience values.
     * @param x Horizontal position of the experience bar on the screen.
     * @param y Vertical position of the experience bar on the screen.
     *
     * @return Truth assignment, if overlay was added excluding cost indication.
     */
    public static boolean render(DrawContext context, ItemStack item, ClientPlayerEntity player, int x, int y) {
        // Only when the scepter has spells to cast do we render the overlay.
        Optional<ScepterContentsComponent> optionalScepterContents = ScepterContentsComponent.get(item);

        if (optionalScepterContents.isEmpty()) {
            return false;
        }
        ScepterContentsComponent scepterContents = optionalScepterContents.get();
        if (!scepterContents.hasSpell()) {
            return false;
        }

        // Check if the player has sufficient experience to cast a spell to determine what indication to render.
        if (scepterContents.hasEnoughExperience(player)) {
            SpellUseIndicationBar.render(context, player, scepterContents, x, y);
        } else {
            SpellCostIndicationBar.render(context, player, scepterContents, x, y);
            return false;
        }
        return true;
    }
}

package io.github.pistonpoek.magicalscepter.mixin.client.gui.hud.bar;

import io.github.pistonpoek.magicalscepter.gui.hud.ExperienceBarOverlay;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.contextualbar.ContextualBarRenderer;
import net.minecraft.client.gui.contextualbar.ExperienceBarRenderer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceBarRenderer.class)
public abstract class ExperienceBarMixin implements ContextualBarRenderer {
    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(at = @At("TAIL"), method = "extractBackground")
    public void renderExperienceBarOverlay(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo callback) {
        LocalPlayer player = this.minecraft.player;
        int x = this.left(this.minecraft.getWindow());
        int y = this.top(this.minecraft.getWindow());

        assert player != null;
        ItemStack mainHandStack = player.getMainHandItem();
        ItemStack offHandStack = player.getOffhandItem();
        boolean mainHandCooldown = player.getCooldowns().isOnCooldown(mainHandStack);
        boolean offHandCooldown = player.getCooldowns().isOnCooldown(offHandStack);

        if (!offHandCooldown && mainHandCooldown) {
            if (ExperienceBarOverlay.render(graphics, offHandStack, player, x, y)) return;
        }

        if (ExperienceBarOverlay.render(graphics, mainHandStack, player, x, y)) return;

        ExperienceBarOverlay.render(graphics, offHandStack, player, x, y);
    }
}

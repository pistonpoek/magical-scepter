package io.github.pistonpoek.magicalscepter.mixin.client.gui.hud.bar;

import io.github.pistonpoek.magicalscepter.gui.hud.ExperienceBarOverlay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.bar.Bar;
import net.minecraft.client.gui.hud.bar.ExperienceBar;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceBar.class)
public abstract class ExperienceBarMixin implements Bar {
    @Final
    @Shadow
    private MinecraftClient client;

    @Inject(at = @At("TAIL"), method = "renderBar")
    public void renderExperienceBarOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo callback) {
        ClientPlayerEntity player = this.client.player;
        int x = this.getCenterX(this.client.getWindow());
        int y = this.getCenterY(this.client.getWindow());

        assert player != null;
        ItemStack mainHandStack = player.getMainHandStack();
        ItemStack offHandStack = player.getOffHandStack();
        boolean mainHandCooldown = player.getItemCooldownManager().isCoolingDown(mainHandStack);
        boolean offHandCooldown = player.getItemCooldownManager().isCoolingDown(offHandStack);

        if (!offHandCooldown && mainHandCooldown) {
            if (ExperienceBarOverlay.render(context, offHandStack, player, x, y)) return;
        }

        if (ExperienceBarOverlay.render(context, mainHandStack, player, x, y)) return;

        ExperienceBarOverlay.render(context, offHandStack, player, x, y);
    }
}

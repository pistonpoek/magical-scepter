package io.github.pistonpoek.magicalscepter.mixin.client.gui.hud;

import io.github.pistonpoek.magicalscepter.gui.hud.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Final
    @Shadow private MinecraftClient client;

    /**
     * Render experience bar overlays.
     *
     * @param context Draw context to use for rendering.
     * @param x Current x position of the experience bar.
     * @param callbackInfo Callback info of the method injection.
     */
    @Inject(method = "renderExperienceBar(Lnet/minecraft/client/gui/DrawContext;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/profiler/Profiler;pop()V"
            )
    )
    private void renderExperienceBarOverlay(DrawContext context, int x, CallbackInfo callbackInfo) {
        // Get the player client for getting data.
        assert this.client.player != null;
        ClientPlayerEntity player = this.client.player;

        ItemStack mainHandStack = player.getMainHandStack();
        ItemStack offHandStack = player.getOffHandStack();
        boolean mainHandCooldown = player.getItemCooldownManager().isCoolingDown(mainHandStack);
        boolean offHandCooldown = player.getItemCooldownManager().isCoolingDown(offHandStack);

        if (!offHandCooldown && mainHandCooldown) {
            if (ExperienceBarOverlay.render(context, offHandStack, player, x)) return;
        }

        if (ExperienceBarOverlay.render(context, mainHandStack, player, x)) return;

        ExperienceBarOverlay.render(context, offHandStack, player, x);
    }
}

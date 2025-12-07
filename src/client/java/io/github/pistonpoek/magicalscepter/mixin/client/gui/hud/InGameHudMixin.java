package io.github.pistonpoek.magicalscepter.mixin.client.gui.hud;

import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * TODO
 */
@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Final
    @Shadow
    private MinecraftClient client;

    /**
     * TODO
     *
     * @param callbackReturnable
     */
    @Inject(at = @At("RETURN"), method = "shouldShowExperienceBar", cancellable = true)
    public void shouldShowExperienceBarOverlay(CallbackInfoReturnable<Boolean> callbackReturnable) {
        boolean show = callbackReturnable.getReturnValue();
        ClientPlayerEntity player = this.client.player;

        assert player != null;
        boolean renderOverlay = player.isHolding(ScepterHelper.ARCANE_SCEPTER) ||
                player.isHolding(ScepterHelper.MAGICAL_SCEPTER);

        callbackReturnable.setReturnValue(show || renderOverlay);
    }


}

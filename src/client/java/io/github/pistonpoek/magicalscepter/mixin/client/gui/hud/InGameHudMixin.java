package io.github.pistonpoek.magicalscepter.mixin.client.gui.hud;

import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(Hud.class)
public abstract class InGameHudMixin {
    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(at = @At("RETURN"), method = "willPrioritizeExperienceInfo", cancellable = true)
    public void shouldShowExperienceBarOverlay(CallbackInfoReturnable<Boolean> callbackReturnable) {
        boolean show = callbackReturnable.getReturnValue();
        LocalPlayer player = this.minecraft.player;

        assert player != null;
        boolean renderOverlay = player.isHolding(ScepterHelper.ARCANE_SCEPTER) ||
                player.isHolding(ScepterHelper.MAGICAL_SCEPTER);

        callbackReturnable.setReturnValue(show || renderOverlay);
    }


}

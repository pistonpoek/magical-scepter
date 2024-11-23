package io.github.pistonpoek.magicalscepter.mixin.client.gui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;
import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import io.github.pistonpoek.magicalscepter.util.PlayerExperience;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow private MinecraftClient client;

    private static final Identifier EXPERIENCE_BAR_COST_TEXTURE = ModIdentifier.of("hud/experience_bar_cost");
    private static final Identifier EXPERIENCE_BAR_USE_TEXTURE = ModIdentifier.of("hud/experience_bar_use");
    private static final int EXPERIENCE_BAR_WIDTH = 182;
    private static final int EXPERIENCE_BAR_HEIGHT = 5;

    @Inject(method = "renderExperienceBar(Lnet/minecraft/client/gui/DrawContext;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/profiler/Profiler;pop()V"
            )
    )
    private void injectRenderExperienceBar(DrawContext context, int x, CallbackInfo callbackInfo) {
        assert this.client.player != null;
        if (!this.client.player.isHolding(ScepterHelper.IS_MAGICAL_SCEPTER)) {
            return;
        }

        Optional<ScepterContentsComponent> optionalScepterContents =
                ScepterHelper.getScepterContentsComponent(this.client.player);
        if (optionalScepterContents.isEmpty()) {
            return;
        }
        ScepterContentsComponent scepterContents = optionalScepterContents.get();

        if (!scepterContents.hasSpell()) {
            return;
        }

        int y = context.getScaledWindowHeight() - 32 + 3;
        int progress = (int)(this.client.player.experienceProgress * 183.0F);

        if (scepterContents.hasEnoughExperience(this.client.player)) {
            float use_progress = scepterContents.getExperienceCost() / (float)this.client.player.getNextLevelExperience();
            int use_part = Math.round(use_progress * (EXPERIENCE_BAR_WIDTH + 1));
            renderExperienceBarSection(context, EXPERIENCE_BAR_USE_TEXTURE, x, y, progress - use_part, progress);
        } else {
            float cost_progress = (scepterContents.getExperienceCost() - PlayerExperience.getTotalExperience(this.client.player))
                    / (float)this.client.player.getNextLevelExperience();
            int cost_part = Math.round(cost_progress * (EXPERIENCE_BAR_WIDTH + 1));
            renderExperienceBarSection(context, EXPERIENCE_BAR_COST_TEXTURE, x, y, progress, progress + cost_part);
        }
    }

    private void renderExperienceBarSection(DrawContext context, Identifier texture,
                                            int x, int y, int start, int end) {
        int startX = Math.clamp(start, 0, EXPERIENCE_BAR_WIDTH);
        int width = Math.clamp(end - startX, 0, EXPERIENCE_BAR_WIDTH);
        RenderSystem.enableBlend();
        context.drawGuiTexture(texture, EXPERIENCE_BAR_WIDTH, EXPERIENCE_BAR_HEIGHT,
                startX, 0, x + startX, y, width, EXPERIENCE_BAR_HEIGHT);
        RenderSystem.disableBlend();
    }
}

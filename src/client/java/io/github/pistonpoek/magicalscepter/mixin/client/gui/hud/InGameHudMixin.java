package io.github.pistonpoek.magicalscepter.mixin.client.gui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import io.github.pistonpoek.magicalscepter.util.PlayerExperience;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Final
    @Shadow private MinecraftClient client;

    @Unique
    private static final Identifier magicalscepter$EXPERIENCE_BAR_COST_TEXTURE = ModIdentifier.of("hud/experience_bar_cost");
    @Unique
    private static final Identifier magicalscepter$EXPERIENCE_BAR_USE_TEXTURE = ModIdentifier.of("hud/experience_bar_use");
    @Unique
    private static final int magicalscepter$EXPERIENCE_BAR_WIDTH = 182;
    @Unique
    private static final int magicalscepter$EXPERIENCE_BAR_HEIGHT = 5;

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
            int use_part = Math.round(use_progress * (magicalscepter$EXPERIENCE_BAR_WIDTH + 1.0F));
            magicalscepter$renderExperienceBarSection(context, magicalscepter$EXPERIENCE_BAR_USE_TEXTURE, x, y, progress - use_part, progress);
        } else {
            float cost_progress = (scepterContents.getExperienceCost() - PlayerExperience.getTotalExperience(this.client.player))
                    / (float)this.client.player.getNextLevelExperience();
            int cost_part = Math.round(cost_progress * (magicalscepter$EXPERIENCE_BAR_WIDTH + 1.0F));
            magicalscepter$renderExperienceBarSection(context, magicalscepter$EXPERIENCE_BAR_COST_TEXTURE, x, y, progress, progress + cost_part);
        }
    }

    @Unique
    private void magicalscepter$renderExperienceBarSection(DrawContext context, Identifier texture,
                                                           int x, int y, int start, int end) {
        int startX = Math.clamp(start, 0, magicalscepter$EXPERIENCE_BAR_WIDTH);
        int width = Math.clamp(end - startX, 0, magicalscepter$EXPERIENCE_BAR_WIDTH - startX);
        RenderSystem.enableBlend();
        context.drawGuiTexture(RenderLayer::getGuiTextured, texture, magicalscepter$EXPERIENCE_BAR_WIDTH, magicalscepter$EXPERIENCE_BAR_HEIGHT,
                startX, 0, x + startX, y, width, magicalscepter$EXPERIENCE_BAR_HEIGHT);
        RenderSystem.disableBlend();
    }
}

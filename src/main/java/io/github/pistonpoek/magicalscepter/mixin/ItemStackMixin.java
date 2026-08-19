package io.github.pistonpoek.magicalscepter.mixin;

import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.TooltipProvider;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract <T extends TooltipProvider> void addToTooltip(
            DataComponentType<T> componentType,
            Item.TooltipContext context, TooltipDisplay displayComponent,
            Consumer<Component> textConsumer, TooltipFlag type
    );

    /**
     * Add the tooltip for scepter contents.
     *
     * @param context      Context to create tooltip with.
     * @param player       Player to create tooltip for.
     * @param type         Type of tooltip to create.
     * @param callbackInfo Mixin callback info.
     * @param consumer     Local text consumer value to append tooltip to.
     */
    @Inject(
            method = "addDetailsToTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;addAttributeTooltips(Ljava/util/function/Consumer;Lnet/minecraft/world/item/component/TooltipDisplay;Lnet/minecraft/world/entity/player/Player;)V"
            )
    )
    private void addScepterContentsTooltip(
            Item.TooltipContext context,
            TooltipDisplay displayComponent,
            @Nullable Player player,
            TooltipFlag type,
            Consumer<Component> consumer,
            CallbackInfo callbackInfo
    ) {
        this.addToTooltip(ModDataComponentTypes.SCEPTER_CONTENTS, context,
                TooltipDisplay.DEFAULT, consumer, type);
        this.addToTooltip(ModDataComponentTypes.SCEPTER_EXPERIENCE, context,
                TooltipDisplay.DEFAULT, consumer, type);
    }
}

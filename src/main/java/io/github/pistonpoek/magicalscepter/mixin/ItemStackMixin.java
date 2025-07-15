package io.github.pistonpoek.magicalscepter.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    protected abstract <T extends TooltipAppender> void appendTooltip(
            ComponentType<T> componentType, Item.TooltipContext context,
            Consumer<Text> textConsumer, TooltipType type);

    /**
     * Add the tooltip for scepter contents.
     *
     * @param context Context to create tooltip with.
     * @param player Player to create tooltip for.
     * @param type Type of tooltip to create.
     * @param callbackInfoReturnable Mixin callback info returnable.
     * @param consumer Local text consumer value to append tooltip to.
     */
    @Inject(
            method = "getTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;appendAttributeModifiersTooltip(Ljava/util/function/Consumer;Lnet/minecraft/entity/player/PlayerEntity;)V"
            )
    )
    private void addScepterContentsTooltip(
            Item.TooltipContext context,
            @Nullable PlayerEntity player,
            TooltipType type,
            CallbackInfoReturnable<List<Text>> callbackInfoReturnable,
            @Local Consumer<Text> consumer
    ) {
        this.appendTooltip(ModDataComponentTypes.SCEPTER_CONTENTS, context, consumer, type);
        this.appendTooltip(ModDataComponentTypes.SCEPTER_EXPERIENCE, context, consumer, type);
    }
}

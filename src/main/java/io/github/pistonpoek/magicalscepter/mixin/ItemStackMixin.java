package io.github.pistonpoek.magicalscepter.mixin;

import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.TooltipDisplayComponent;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract <T extends TooltipAppender> void appendComponentTooltip(ComponentType<T> componentType,
                           Item.TooltipContext context, TooltipDisplayComponent displayComponent,
                           Consumer<Text> textConsumer, TooltipType type);

    /**
     * Add the tooltip for scepter contents.
     *
     * @param context Context to create tooltip with.
     * @param player Player to create tooltip for.
     * @param type Type of tooltip to create.
     * @param callbackInfo Mixin callback info.
     * @param consumer Local text consumer value to append tooltip to.
     */
    @Inject(
            method = "appendTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;appendAttributeModifiersTooltip(Ljava/util/function/Consumer;Lnet/minecraft/component/type/TooltipDisplayComponent;Lnet/minecraft/entity/player/PlayerEntity;)V"
            )
    )
    private void addScepterContentsTooltip(
            Item.TooltipContext context,
            TooltipDisplayComponent displayComponent,
            @Nullable PlayerEntity player,
            TooltipType type,
            Consumer<Text> consumer,
            CallbackInfo callbackInfo
    ) {
        this.appendComponentTooltip(ModDataComponentTypes.SCEPTER_CONTENTS, context,
                TooltipDisplayComponent.DEFAULT, consumer, type);
        this.appendComponentTooltip(ModDataComponentTypes.SCEPTER_EXPERIENCE, context,
                TooltipDisplayComponent.DEFAULT, consumer, type);
    }
}

package io.github.pistonpoek.magicalscepter.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.item.ItemStack;

@Mixin(Allay.class)
public class AllayEntityMixin {
    /**
     * Modify are items equal return value to differentiate between scepters.
     *
     * @param original Original return value of the method.
     * @param stack    Item stack one to compare to the other, for example allay hand item.
     * @param stack2   Item stack two to compare to the other, for example item to pickup.
     * @return Truth assignment, if allay should treat items as equal.
     */
    @ModifyReturnValue(method = "allayConsidersItemEqual(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z", at = @At("RETURN"))
    private boolean modifyAreItemsEqualReturnValue(boolean original, ItemStack stack, ItemStack stack2) {
        return original && !magicalscepter$areDifferentScepters(stack, stack2);
    }

    /**
     * Check if two items are different scepters.
     *
     * @param stack  Item stack one to compare to the other.
     * @param stack2 Item stack two to compare to the other.
     * @return Truth assignment, if the two item stacks have different scepter contents.
     */
    @Unique
    private boolean magicalscepter$areDifferentScepters(ItemStack stack, ItemStack stack2) {
        ScepterContentsComponent scepterContentsComponent = stack.get(ModDataComponentTypes.SCEPTER_CONTENTS);
        ScepterContentsComponent scepterContentsComponent2 = stack2.get(ModDataComponentTypes.SCEPTER_CONTENTS);
        return !Objects.equals(scepterContentsComponent, scepterContentsComponent2);
    }
}

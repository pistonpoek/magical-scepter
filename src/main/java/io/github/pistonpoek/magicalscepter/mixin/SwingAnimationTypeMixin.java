package io.github.pistonpoek.magicalscepter.mixin;

import net.minecraft.world.item.SwingAnimationType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.MixinIntrinsics;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SwingAnimationType.class)
public enum SwingAnimationTypeMixin {
    MAGICALSCEPTER_SWIRL(MixinIntrinsics.currentEnumOrdinal(), "magicalscepter_swirl");

    @Shadow
    SwingAnimationTypeMixin(final int id, final String name) {
    }
}

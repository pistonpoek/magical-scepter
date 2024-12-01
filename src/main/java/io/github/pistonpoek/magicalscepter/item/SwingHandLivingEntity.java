package io.github.pistonpoek.magicalscepter.item;

import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Unique;

public interface SwingHandLivingEntity {
    @Unique void magical_scepter$swingHand(Hand hand, SwingType swingType);
    @Unique SwingType magical_scepter$getSwingType();
    @Unique void magical_scepter$setSwingType(SwingType swingType);
}

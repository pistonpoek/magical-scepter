package io.github.pistonpoek.magicalscepter.item;

import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Unique;

/**
 * Living entity that provides functionality for different hand swing type actions.
 */
public interface SwingHandLivingEntity {
    /**
     * Swing the specified hand of the living entity with the specified swing type.
     *
     * @param hand Hand of the entity to swing.
     * @param swingType Swing type to use for the entity.
     */
    @Unique void magical_scepter$swingHand(Hand hand, SwingType swingType);

    /**
     * Get the swing type.
     *
     * @return Current swing type of the entity.
     */
    @Unique SwingType magical_scepter$getSwingType();

    /**
     * Set the swing type of the entity.
     *
     * @param swingType Swing type to set.
     */
    @Unique void magical_scepter$setSwingType(SwingType swingType);
}

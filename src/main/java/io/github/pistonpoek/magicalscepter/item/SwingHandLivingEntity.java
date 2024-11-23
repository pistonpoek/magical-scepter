package io.github.pistonpoek.magicalscepter.item;

import net.minecraft.util.Hand;

public interface SwingHandLivingEntity {
    void swingHand(Hand hand, SwingType swingType);
    SwingType getSwingType();
    void setSwingType(SwingType swingType);
}

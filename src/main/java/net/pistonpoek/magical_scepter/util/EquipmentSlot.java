package net.pistonpoek.magical_scepter.util;

import net.minecraft.util.Hand;

public class EquipmentSlot {
    /**
     * Convert a Hand to an EquipmentSlot.
     *
     * @param hand Hand to convert.
     * @return Equipment slot.
     */
    public static net.minecraft.entity.EquipmentSlot fromHand(Hand hand) {
        return switch (hand) {
            case MAIN_HAND -> net.minecraft.entity.EquipmentSlot.MAINHAND;
            case OFF_HAND -> net.minecraft.entity.EquipmentSlot.OFFHAND;
        };
    }
}

package io.github.pistonpoek.magicalscepter.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.Hand;

import java.util.function.Predicate;

public class LivingEntityHand {
    /**
     * Get the hand of the specified entity that contains the specified predicate assuming it holds for at least one hand.
     *
     * @param entity    Entity to get find the appropriate hand for.
     * @param predicate Predicate to find the hand for.
     * @return Hand of entity for which the predicate holds.
     */
    public static Hand get(LivingEntity entity, Predicate<ItemStack> predicate) {
        return predicate.test(entity.getMainHandStack()) ? Hand.MAIN_HAND : Hand.OFF_HAND;
    }

    /**
     * Packet codec for a hand.
     */
    public static final PacketCodec<RegistryByteBuf, Hand> PACKET_CODEC = PacketCodec.of(
            (value, buf) -> buf.writeVarInt(value.ordinal()),
            buf -> Hand.values()[buf.readVarInt()]
    );
}

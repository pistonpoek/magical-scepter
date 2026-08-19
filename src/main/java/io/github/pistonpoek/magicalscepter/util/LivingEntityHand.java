package io.github.pistonpoek.magicalscepter.util;

import java.util.function.Predicate;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Util class related to the hand of a living entity.
 */
public class LivingEntityHand {
    /**
     * Get the hand of the specified entity that contains the specified predicate assuming it holds for at least one hand.
     *
     * @param entity    Entity to get find the appropriate hand for.
     * @param predicate Predicate to find the hand for.
     * @return Hand of entity for which the predicate holds.
     */
    public static InteractionHand get(LivingEntity entity, Predicate<ItemStack> predicate) {
        return predicate.test(entity.getMainHandItem()) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    /**
     * Packet codec for a hand.
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, InteractionHand> PACKET_CODEC = StreamCodec.ofMember(
            (value, buf) -> buf.writeVarInt(value.ordinal()),
            buf -> InteractionHand.values()[buf.readVarInt()]
    );
}

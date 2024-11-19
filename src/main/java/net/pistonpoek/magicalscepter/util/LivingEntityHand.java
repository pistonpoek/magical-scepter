package net.pistonpoek.magicalscepter.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.Hand;
import net.pistonpoek.magicalscepter.item.SwingHandLivingEntity;

import java.util.function.Predicate;

public class LivingEntityHand {
    public static Hand get(LivingEntity entity, Predicate<ItemStack> predicate) {
        return predicate.test(entity.getMainHandStack()) ? Hand.MAIN_HAND : Hand.OFF_HAND;
    }

    public static final PacketCodec<RegistryByteBuf, Hand> PACKET_CODEC = PacketCodec.of(
            (value, buf) -> buf.writeVarInt(value.ordinal()),
            buf -> Hand.values()[buf.readVarInt()]
    );
}

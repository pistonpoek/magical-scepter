package io.github.pistonpoek.gametest.magicalscepter.util;

import com.mojang.serialization.DynamicOps;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.GameType;

public class ContextUtil {

    public static RegistryAccess getRegistries(GameTestHelper context) {
        return context.getLevel().registryAccess();
    }

    public static <T> RegistryOps<T> getRegistryOps(GameTestHelper context, DynamicOps<T> ops) {
        return getRegistries(context).createSerializationContext(ops);
    }

    public static Holder<Spell> getSpell(GameTestHelper context, ResourceKey<Spell> key) {
        return getRegistries(context).getOrThrow(key);
    }

    public static Holder<Scepter> getScepter(GameTestHelper context, ResourceKey<Scepter> key) {
        return getRegistries(context).getOrThrow(key);
    }

    public static Holder<Enchantment> getEnchantment(GameTestHelper context, ResourceKey<Enchantment> key) {
        return getRegistries(context).getOrThrow(key);
    }

    public static ItemStack getMagicalScepter(GameTestHelper context) {
        return ScepterHelper.createMagicalScepter(getScepter(context, Scepters.MAGICAL_KEY));
    }

    public static ItemStack setMagicalScepterInMainHand(GameTestHelper context, Player player) {
        return setMagicalScepterInHand(context, player, InteractionHand.MAIN_HAND);
    }

    public static ItemStack setMagicalScepterInHand(GameTestHelper context, Player player, InteractionHand hand) {
        ItemStack stack = getMagicalScepter(context);
        player.setItemInHand(hand, stack);
        return stack;
    }

    public static ServerPlayer createMockServerPlayer(GameTestHelper context, GameType gameMode) {
        Player player = context.makeMockPlayer(gameMode);
        ServerPlayer serverPlayer = new ServerPlayer(context.getLevel().getServer(), context.getLevel(),
                player.getGameProfile(), ClientInformation.createDefault());

        // Set the player to be loaded and have a network handler to mock server expectation.
        serverPlayer.connection = new ServerGamePacketListenerImpl(context.getLevel().getServer(),
                new Connection(PacketFlow.CLIENTBOUND), serverPlayer,
                CommonListenerCookie.createInitial(player.getGameProfile(), false));

        return serverPlayer;
    }
}

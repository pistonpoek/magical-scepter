package io.github.pistonpoek.gametest.magicalscepter.util;

import com.mojang.serialization.DynamicOps;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;

public class ContextUtil {

    public static DynamicRegistryManager getRegistries(TestContext context) {
        return context.getWorld().getRegistryManager();
    }

    public static <T> RegistryOps<T> getRegistryOps(TestContext context, DynamicOps<T> ops) {
        return RegistryOps.of(ops, getRegistries(context));
    }

    public static RegistryEntry<Spell> getSpell(TestContext context, RegistryKey<Spell> key) {
        return getRegistries(context).getEntryOrThrow(key);
    }

    public static RegistryEntry<Scepter> getScepter(TestContext context, RegistryKey<Scepter> key) {
        return getRegistries(context).getEntryOrThrow(key);
    }

    public static RegistryEntry<Enchantment> getEnchantment(TestContext context, RegistryKey<Enchantment> key) {
        return getRegistries(context).getEntryOrThrow(key);
    }

    public static ItemStack getMagicalScepter(TestContext context) {
        return ScepterHelper.createMagicalScepter(getScepter(context, Scepters.MAGICAL_KEY));
    }

    public static ItemStack setMagicalScepterInMainHand(TestContext context, PlayerEntity player) {
        return setMagicalScepterInHand(context, player, Hand.MAIN_HAND);
    }

    public static ItemStack setMagicalScepterInHand(TestContext context, PlayerEntity player, Hand hand) {
        ItemStack stack = getMagicalScepter(context);
        player.setStackInHand(hand, stack);
        return stack;
    }

    public static ServerPlayerEntity createMockServerPlayer(TestContext context, GameMode gameMode) {
        PlayerEntity player = context.createMockPlayer(gameMode);
        ServerPlayerEntity serverPlayer = new ServerPlayerEntity(context.getWorld().getServer(), context.getWorld(),
                player.getGameProfile(), SyncedClientOptions.createDefault());

        // Set the player to be loaded and have a network handler to mock server expectation.
        serverPlayer.networkHandler = new ServerPlayNetworkHandler(context.getWorld().getServer(),
                new ClientConnection(NetworkSide.CLIENTBOUND), serverPlayer,
                ConnectedClientData.createDefault(player.getGameProfile(), false));

        return serverPlayer;
    }
}

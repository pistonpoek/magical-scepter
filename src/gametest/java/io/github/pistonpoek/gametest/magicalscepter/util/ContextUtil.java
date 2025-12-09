package io.github.pistonpoek.gametest.magicalscepter.util;

import com.mojang.serialization.DynamicOps;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;

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
}

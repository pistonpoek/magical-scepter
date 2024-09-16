package net.pistonpoek.magicalscepter.scepter;

import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import net.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import net.pistonpoek.magicalscepter.item.ModItems;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;

import java.util.function.Predicate;

public class ScepterHelper {
    /**
     * Predicate to check if an item stack is a scepter.
     */
    public static final Predicate<ItemStack> IS_SCEPTER = itemStack ->
            itemStack.isOf(ModItems.SCEPTER);
    /**
     * Predicate to check if an item stack is both a scepter and empty.
     */
    public static final Predicate<ItemStack> IS_EMPTY_SCEPTER = itemStack -> IS_SCEPTER.test(itemStack) &&
            ScepterContentsComponent.getScepter(itemStack).isEmpty();
    /**
     * Predicate to check if an item stack is both a scepter and infusable.
     */
    public static final Predicate<ItemStack> IS_INFUSABLE_SCEPTER = itemStack -> IS_SCEPTER.test(itemStack) &&
            ScepterContentsComponent.isInfusable(itemStack);

    public static ItemStack createScepter(RegistryEntry<Scepter> scepter) {
        return ScepterContentsComponent.setScepter(ModItems.SCEPTER.getDefaultStack(), scepter);
    }

    public static Registry<Scepter> getScepterRegistry(World world) {
        return world.getRegistryManager().get(ModRegistryKeys.SCEPTER);
    }
}

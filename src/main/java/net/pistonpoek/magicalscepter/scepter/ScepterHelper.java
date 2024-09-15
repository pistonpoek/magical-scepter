package net.pistonpoek.magicalscepter.scepter;

import net.minecraft.component.ComponentMap;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.World;
import net.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import net.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import net.pistonpoek.magicalscepter.item.ModItems;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;

import java.util.Optional;
import java.util.function.Predicate;

import static net.pistonpoek.magicalscepter.component.ModDataComponentTypes.SCEPTER_CONTENTS;

public class ScepterHelper {
    public static final Predicate<ItemStack> IS_SCEPTER = itemStack ->
            itemStack.isOf(ModItems.SCEPTER);
    public static final Predicate<ItemStack> IS_EMPTY_SCEPTER = itemStack -> IS_SCEPTER.test(itemStack) &&
            getScepter(itemStack).isEmpty();

    public static ItemStack createScepter(RegistryEntry<Scepter> scepter) {
        return ScepterHelper.setScepter(ModItems.SCEPTER.getDefaultStack(), scepter);
    }

    public static Optional<RegistryEntry<Scepter>> getScepter(ItemStack stack) {
        return ScepterHelper.getScepter(stack.getComponents());
    }

    private static Optional<RegistryEntry<Scepter>> getScepter(ComponentMap components) {
        return Optional.ofNullable(components.get(SCEPTER_CONTENTS)).flatMap(ScepterContentsComponent::scepter);
    }

    public static ItemStack setScepter(ItemStack stack, RegistryEntry<Scepter> scepter) {
        stack.set(SCEPTER_CONTENTS, ScepterContentsComponent.with(scepter));
        return stack;
    }

    public static int getColor(ItemStack stack) {
        return getColor(getScepter(stack).orElse(null));
    }

    public static int getColor(RegistryEntry<Scepter> scepter) {
        if (scepter == null) {
            return ColorHelper.Argb.getArgb(0, 0, 0, 0);
        }
        return ColorHelper.Argb.fullAlpha(scepter.value().getColor());
    }


    public static Registry<Scepter> getScepterRegistry(World world) {
        return world.getRegistryManager().get(ModRegistryKeys.SCEPTER);
    }

}

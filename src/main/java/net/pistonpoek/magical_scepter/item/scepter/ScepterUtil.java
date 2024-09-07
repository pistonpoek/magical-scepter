package net.pistonpoek.magical_scepter.item.scepter;

import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.pistonpoek.magical_scepter.MagicalScepter;
import net.pistonpoek.magical_scepter.ModDataComponentTypes;
import net.pistonpoek.magical_scepter.ModRegistries;
import net.pistonpoek.magical_scepter.item.ModItems;

import java.util.Optional;
import java.util.function.Predicate;

import static net.pistonpoek.magical_scepter.ModDataComponentTypes.INFUSABLE;
import static net.pistonpoek.magical_scepter.ModDataComponentTypes.SCEPTER;

public class ScepterUtil {
    public static final Predicate<ItemStack> IS_SCEPTER = (ItemStack itemStack) -> itemStack.isOf(ModItems.SCEPTER);
    public static final Predicate<ItemStack> IS_INFUSABLE_SCEPTER =
            (ItemStack itemStack) -> IS_SCEPTER.test(itemStack)
            && ScepterUtil.isInfusable(itemStack);

//    public static Scepter getScepter(ItemStack stack) {
//        return ScepterUtil.getScepter(stack.getComponents());
//    }

//    public static Scepter getScepter(ComponentMap components) {
//        return components.getOrDefault(SCEPTER, Scepters.MAGICAL).value();
//    }

    public static ItemStack setScepter(ItemStack stack, RegistryEntry<Scepter> scepter) {
        stack.set(SCEPTER, scepter);
        //stack.set(INFUSABLE, scepter.value().isInfusable());
        return stack;
    }

    public static Optional<ScepterItem> getScepterItem(ItemStack stack) {
        if (IS_SCEPTER.test(stack)) {
            return Optional.of((ScepterItem) stack.getItem());
        }
        return Optional.empty();
    }


    /**
     * Check if an item stack is infusable.
     *
     * @param itemStack Item stack to check for.
     * @return Truth assignment, if item stack is infusable.
     */
    public static boolean isInfusable(ItemStack itemStack) {
        return itemStack.getComponents().getOrDefault(ModDataComponentTypes.INFUSABLE, false);
    }
//
//    /**
//     * Get the infusion for the damage source
//     *
//     * @param source Damage source to get infusion scepter for.
//     * @return Optional scepter for the damage source infusion.
//     */
//    public static Optional<RegistryEntry<Scepter>> getInfusion(DamageSource source) {
//        MagicalScepter.LOGGER.info("Getting infusion for damage source:" + source.getName());
//        for (Scepter scepter: ModRegistries.SCEPTER) {
//            if (scepter.getInfusionPredicate().test(source)) {
//                MagicalScepter.LOGGER.info("Got infusion for scepter:" + scepter);
//                return Optional.of(ModRegistries.SCEPTER.getEntry(scepter));
//            }
//        }
//        MagicalScepter.LOGGER.info("Returning empty infusion for damage source:" + source.getName());
//        return Optional.empty();
//    }

    public static ItemStack createScepter(RegistryEntry<Scepter> scepter) {
        return ScepterUtil.setScepter(ModItems.SCEPTER.getDefaultStack(), scepter);
    }

    public enum UseAction {


    }

}

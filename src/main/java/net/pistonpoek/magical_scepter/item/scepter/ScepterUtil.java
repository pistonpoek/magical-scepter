package net.pistonpoek.magical_scepter.item.scepter;

import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.pistonpoek.magical_scepter.MagicalScepter;
import net.pistonpoek.magical_scepter.ModRegistries;
import net.pistonpoek.magical_scepter.item.ModItems;

import java.util.Optional;
import java.util.function.Predicate;

import static net.pistonpoek.magical_scepter.ModDataComponentTypes.SCEPTER;

public class ScepterUtil {
    public static final Predicate<ItemStack> IS_SCEPTER = (ItemStack itemStack) -> itemStack.isOf(ModItems.SCEPTER);
    public static final Predicate<ItemStack> IS_INFUSABLE_SCEPTER =
            (ItemStack itemStack) -> IS_SCEPTER.test(itemStack)
            && ScepterUtil.getScepter(itemStack).isInfusable();

    public static Scepter getScepter(ItemStack stack) {
        return ScepterUtil.getScepter(stack.getComponents());
    }

    public static Scepter getScepter(ComponentMap components) {
        return components.getOrDefault(SCEPTER, Scepters.MAGICAL).value();
    }

    public static ItemStack setScepter(ItemStack stack, RegistryEntry<Scepter> scepter) {
        stack.set(SCEPTER, scepter);
        return stack;
    }

    /**
     * Get the infusion for the damage source
     *
     * @param source Damage source to get infusion scepter for.
     * @return Optional scepter for the damage source infusion.
     */
    public static Optional<RegistryEntry<Scepter>> getInfusion(DamageSource source) {
        MagicalScepter.LOGGER.info("Getting infusion for damage source:" + source.getName());
        for (Scepter scepter: ModRegistries.SCEPTER) {
            if (scepter.getInfusionPredicate().test(source)) {
                MagicalScepter.LOGGER.info("Got infusion for scepter:" + scepter);
                return Optional.of(ModRegistries.SCEPTER.getEntry(scepter));
            }
        }
        MagicalScepter.LOGGER.info("Returning empty infusion for damage source:" + source.getName());
        return Optional.empty();
    }

    public static ItemStack createScepter(RegistryEntry<Scepter> scepter) {
        return ScepterUtil.setScepter(ModItems.SCEPTER.getDefaultStack(), scepter);
    }

    public enum UseAction {


    }

}

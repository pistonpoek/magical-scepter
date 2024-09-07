package net.pistonpoek.magical_scepter.item.scepter;

import net.minecraft.component.ComponentMap;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.World;
import net.pistonpoek.magical_scepter.MagicalScepter;
import net.pistonpoek.magical_scepter.ModDataComponentTypes;
import net.pistonpoek.magical_scepter.ModRegistryKeys;
import net.pistonpoek.magical_scepter.item.ModItems;
import net.pistonpoek.magical_scepter.util.ModIdentifier;

import java.util.Optional;

import static net.pistonpoek.magical_scepter.ModDataComponentTypes.SCEPTER;

public class ScepterHelper {

    public static ItemStack createScepter(RegistryEntry<Scepter> scepter) {
        return ScepterHelper.setScepter(ModItems.SCEPTER.getDefaultStack(), scepter);
    }

    public static Scepter getScepter(ItemStack stack) {
        return getScepterEntry(stack).map(RegistryEntry::value).orElse(Scepter.DEFAULT);
    }

    public static Optional<RegistryEntry<Scepter>> getScepterEntry(ItemStack stack) {
        return ScepterHelper.getScepterEntry(stack.getComponents());
    }

    public static Optional<RegistryEntry<Scepter>> getScepterEntry(ComponentMap components) {
        return Optional.ofNullable(components.get(SCEPTER));
    }

    public static ItemStack setScepter(ItemStack stack, RegistryEntry<Scepter> scepter) {
        stack.set(SCEPTER, scepter);
        return stack;
    }

    private static Optional<Boolean> getInfusable(ItemStack itemStack) {
        return Optional.ofNullable(itemStack.get(ModDataComponentTypes.INFUSABLE));
    }

    /**
     * Check if an item stack is infusable.
     *
     * @param itemStack Item stack to check for.
     * @return Truth assignment, if item stack is infusable.
     */
    public static boolean isInfusable(ItemStack itemStack) {
        return getInfusable(itemStack).orElse(isInfusable(getScepter(itemStack)));
    }

    public static boolean isInfusable(Scepter scepter) {
        if (scepter == null) return false;

        return scepter.isInfusable();
    }

    public static Registry<Scepter> getScepterRegistry(World world) {
        return world.getRegistryManager().get(ModRegistryKeys.SCEPTER);
    }

    /**
     * Get the infusion for the damage source
     *
     * @param source Damage source to get infusion scepter for.
     * @param lootContext Loot context to check infusion conditions with.
     *
     * @return Optional scepter for the damage source infusion.
     */
    public static Optional<RegistryEntry<Scepter>> getInfusion(Registry<Scepter> scepterRegistry, DamageSource source, LootContext lootContext) {
        MagicalScepter.LOGGER.info("Getting infusion for damage source:" + source.getName());

        for (RegistryEntry<Scepter> scepter: scepterRegistry.iterateEntries(
                TagKey.of(ModRegistryKeys.SCEPTER, ModIdentifier.of("infusable")))) {

            if (scepter.value().infuses(lootContext)) {
                MagicalScepter.LOGGER.info("Got infusion for scepter:" + scepter);
                return Optional.of(scepter);
            }
        }
        MagicalScepter.LOGGER.info("Returning empty infusion for damage source:" + source.getName());
        return Optional.empty();
    }

}

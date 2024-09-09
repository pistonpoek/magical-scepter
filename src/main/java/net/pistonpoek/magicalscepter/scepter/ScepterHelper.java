package net.pistonpoek.magicalscepter.scepter;

import net.minecraft.component.ComponentMap;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import net.pistonpoek.magicalscepter.MagicalScepter;
import net.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import net.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import net.pistonpoek.magicalscepter.item.ModItems;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.pistonpoek.magicalscepter.spell.Spell;

import java.util.Optional;
import java.util.function.Predicate;

import static net.pistonpoek.magicalscepter.component.ModDataComponentTypes.SCEPTER_CONTENTS;

public class ScepterHelper {
    public static final Predicate<ItemStack> IS_SCEPTER = (ItemStack itemStack) -> itemStack.isOf(ModItems.SCEPTER);
    public static final Predicate<ItemStack> IS_INFUSABLE_SCEPTER =
            (ItemStack itemStack) -> IS_SCEPTER.test(itemStack)
                    && ScepterHelper.isInfusable(itemStack);

    public static ItemStack createScepter(RegistryEntry<Scepter> scepter) {
        return ScepterHelper.setScepter(ModItems.SCEPTER.getDefaultStack(), scepter);
    }

    public static RegistryEntry<Scepter> getScepter(ItemStack stack) {
        return ScepterHelper.getScepter(stack.getComponents()).orElse(RegistryEntry.of(Scepters.DEFAULT));
    }

    private static Optional<RegistryEntry<Scepter>> getScepter(ComponentMap components) {
        return Optional.ofNullable(components.get(SCEPTER_CONTENTS)).flatMap(ScepterContentsComponent::scepter);
    }

    public static ItemStack setScepter(ItemStack stack, RegistryEntry<Scepter> scepter) {
        stack.set(SCEPTER_CONTENTS, ScepterContentsComponent.with(scepter));
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

    public static boolean isInfusable(RegistryEntry<Scepter> scepter) {
        if (scepter == null) return false;

        return scepter.value().isInfusable();
    }

    public static Spell getSpell(ItemStack itemStack) {
        return getScepter(itemStack).value().getSpell();
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
    public static Optional<RegistryEntry<Scepter>> getInfusion(Registry<Scepter> scepterRegistry,
                                                               DamageSource source, LootContext lootContext) {
        MagicalScepter.LOGGER.info("Getting infusion for damage source:" + source.getName());

        for (RegistryEntry<Scepter> scepter: scepterRegistry.streamEntries().toList()) {

            MagicalScepter.LOGGER.info("Testing for scepter registry entry:" + scepter.getIdAsString());
            if (scepter.value().infuses(lootContext)) {
                MagicalScepter.LOGGER.info("Got infusion for scepter:" + scepter);
                return Optional.of(scepter);
            }
        }
        MagicalScepter.LOGGER.info("Returning empty infusion for damage source:" + source.getName());
        return Optional.empty();
    }

}

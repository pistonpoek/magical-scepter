package io.github.pistonpoek.magicalscepter.scepter;

import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import io.github.pistonpoek.magicalscepter.component.ScepterExperienceComponent;
import io.github.pistonpoek.magicalscepter.item.ArcaneScepterItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;

import java.util.Optional;
import java.util.function.Predicate;

public class ScepterHelper {
    /**
     * Predicate to check if an item stack is a scepter.
     */
    public static final Predicate<ItemStack> IS_SCEPTER = itemStack ->
            itemStack.isOf(ModItems.SCEPTER);
    /**
     * Predicate to check if an item stack is an arcane scepter.
     */
    public static final Predicate<ItemStack> IS_ARCANE_SCEPTER = itemStack ->
            itemStack.isOf(ModItems.ARCANE_SCEPTER);
    /**
     * Predicate to check if an item stack is a filled arcane scepter.
     */
    public static final Predicate<ItemStack> IS_FILLED_ARCANE_SCEPTER = itemStack ->
            IS_ARCANE_SCEPTER.test(itemStack) && getExperience(itemStack) >= ArcaneScepterItem.EXPERIENCE_STEP;
    /**
     * Predicate to check if an item stack is a magical scepter.
     */
    public static final Predicate<ItemStack> IS_MAGICAL_SCEPTER = itemStack ->
            itemStack.isOf(ModItems.MAGICAL_SCEPTER);
    /**
     * Predicate to check if an item stack is both a scepter and infusable.
     */
    public static final Predicate<ItemStack> IS_INFUSABLE_SCEPTER = itemStack -> IS_MAGICAL_SCEPTER.test(itemStack) &&
            ScepterContentsComponent.isInfusable(itemStack);
    /**
     * Predicate to check if an item stack is a scepter with a spell.
     */
    public static final Predicate<ItemStack> IS_SCEPTER_WITH_SPELL = itemStack -> IS_MAGICAL_SCEPTER.test(itemStack) &&
            ScepterContentsComponent.hasSpell(itemStack);

    public static ItemStack createMagicalScepter(RegistryEntry<Scepter> scepter) {
        return createMagicalScepter(ItemStack.EMPTY, scepter);
    }

    public static ItemStack createMagicalScepter(ItemStack stack, RegistryEntry<Scepter> scepter) {
        ItemStack scepterStack = ModItems.MAGICAL_SCEPTER.getDefaultStack();
        scepterStack.applyChanges(stack.getComponentChanges());
        return ScepterContentsComponent.setScepter(scepterStack, scepter);
    }

    public static ItemStack createScepter(ItemStack stack) {
        ItemStack scepterStack = ModItems.SCEPTER.getDefaultStack();
        scepterStack.applyChanges(stack.getComponentChanges());
        scepterStack.remove(ModDataComponentTypes.SCEPTER_CONTENTS);
        return scepterStack;
    }

    public static Registry<Scepter> getScepterRegistry(World world) {
        return world.getRegistryManager().getOrThrow(ModRegistryKeys.SCEPTER);
    }

    public static Optional<ScepterContentsComponent> getScepterContentsComponent(PlayerEntity player) {
        if (IS_SCEPTER_WITH_SPELL.test(player.getMainHandStack())) {
            return ScepterContentsComponent.get(player.getMainHandStack());
        } else if (IS_SCEPTER_WITH_SPELL.test(player.getOffHandStack())) {
            return ScepterContentsComponent.get(player.getOffHandStack());
        } else {
            return ScepterContentsComponent.get(player.getMainHandStack())
                    .or(() -> ScepterContentsComponent.get(player.getOffHandStack()));
        }
    }

    public static int getExperience(ItemStack itemStack) {
        return itemStack.getOrDefault(ModDataComponentTypes.SCEPTER_EXPERIENCE,
                ScepterExperienceComponent.DEFAULT).experience();
    }
}

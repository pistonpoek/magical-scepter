package io.github.pistonpoek.magicalscepter.scepter;

import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.registry.tag.ModItemTags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Helper class for all scepter related applications.
 */
public class ScepterHelper {
    /**
     * Predicate to check if an item stack is a scepter.
     */
    public static final Predicate<ItemStack> SCEPTER = itemStack ->
            itemStack.isOf(ModItems.SCEPTER);
    /**
     * Predicate to check if an item stack is an arcane scepter.
     */
    public static final Predicate<ItemStack> ARCANE_SCEPTER = itemStack ->
            itemStack.isIn(ModItemTags.ARCANE_SCEPTERS);
    /**
     * Predicate to check if an item stack is a magical scepter.
     */
    public static final Predicate<ItemStack> MAGICAL_SCEPTER = itemStack ->
            itemStack.isOf(ModItems.MAGICAL_SCEPTER);
    /**
     * Predicate to check if an item stack is both a scepter and infusable.
     */
    public static final Predicate<ItemStack> INFUSABLE_SCEPTER = itemStack -> MAGICAL_SCEPTER.test(itemStack) &&
            ScepterContentsComponent.isInfusable(itemStack);
    /**
     * Predicate to check if an item stack is a scepter with a spell.
     */
    public static final Predicate<ItemStack> SCEPTER_WITH_SPELL = itemStack -> MAGICAL_SCEPTER.test(itemStack) &&
            ScepterContentsComponent.hasSpell(itemStack);

    /**
     * Create a magical scepter item stack from the specified scepter.
     *
     * @param scepter Scepter registry entry to create magical scepter with.
     * @return Item stack of a magical scepter with the specified scepter type.
     */
    public static ItemStack createMagicalScepter(RegistryEntry<Scepter> scepter) {
        return createMagicalScepter(ItemStack.EMPTY, scepter);
    }

    /**
     * Create a magical scepter item stack from the specified scepter and item stack.
     *
     * @param stack Item stack to use components from.
     * @param scepter Scepter registry entry to create magical scepter with.
     * @return Item stack of a magical scepter with the specified scepter type and item stack components.
     */
    public static ItemStack createMagicalScepter(ItemStack stack, RegistryEntry<Scepter> scepter) {
        ItemStack scepterStack = ModItems.MAGICAL_SCEPTER.getDefaultStack();
        scepterStack.applyChanges(stack.getComponentChanges());
        return ScepterContentsComponent.setScepter(scepterStack, scepter);
    }

    /**
     * Create a scepter item stack with components from the specified item stack.
     *
     * @param stack Item stack to use components from.
     * @return Item stack of a scepter with the specified item stack components.
     */
    public static ItemStack createScepter(ItemStack stack) {
        ItemStack scepterStack = ModItems.SCEPTER.getDefaultStack();
        scepterStack.applyChanges(stack.getComponentChanges());
        scepterStack.remove(ModDataComponentTypes.SCEPTER_CONTENTS);
        scepterStack.setDamage(0);
        scepterStack.remove(ModDataComponentTypes.SCEPTER_EXPERIENCE);
        return scepterStack;
    }

    /**
     * Get the scepter registry form the specified world.
     *
     * @param world World to get scepter registry from.
     * @return Scepter registry retrieved from the specified world.
     */
    public static Registry<Scepter> getScepterRegistry(World world) {
        return world.getRegistryManager().getOrThrow(ModRegistryKeys.SCEPTER);
    }

    /**
     * Get the scepter contents component from the specified player.
     *
     * @param player Player entity to get the scepter contents component from.
     * @return Optional Scepter contents component from the specified player.
     */
    public static Optional<ScepterContentsComponent> getScepterContentsComponent(PlayerEntity player) {
        if (SCEPTER_WITH_SPELL.test(player.getMainHandStack())) {
            return ScepterContentsComponent.get(player.getMainHandStack());
        } else if (SCEPTER_WITH_SPELL.test(player.getOffHandStack())) {
            return ScepterContentsComponent.get(player.getOffHandStack());
        } else {
            return ScepterContentsComponent.get(player.getMainHandStack())
                    .or(() -> ScepterContentsComponent.get(player.getOffHandStack()));
        }
    }
}

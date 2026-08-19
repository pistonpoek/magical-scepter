package io.github.pistonpoek.magicalscepter.scepter;

import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.registry.tag.ModItemTags;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

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
            itemStack.is(ModItems.SCEPTER);
    /**
     * Predicate to check if an item stack is an arcane scepter.
     */
    public static final Predicate<ItemStack> ARCANE_SCEPTER = itemStack ->
            itemStack.is(ModItemTags.ARCANE_SCEPTERS);
    /**
     * Predicate to check if an item stack is a magical scepter.
     */
    public static final Predicate<ItemStack> MAGICAL_SCEPTER = itemStack ->
            itemStack.is(ModItems.MAGICAL_SCEPTER);
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
    public static ItemStack createMagicalScepter(Holder<Scepter> scepter) {
        return createMagicalScepter(ItemStack.EMPTY, scepter);
    }

    /**
     * Create a magical scepter item stack from the specified scepter and item stack.
     *
     * @param stack Item stack to use components from.
     * @param scepter Scepter registry entry to create magical scepter with.
     * @return Item stack of a magical scepter with the specified scepter type and item stack components.
     */
    public static ItemStack createMagicalScepter(ItemStack stack, Holder<Scepter> scepter) {
        ItemStack scepterStack = ModItems.MAGICAL_SCEPTER.getDefaultInstance();
        scepterStack.applyComponentsAndValidate(stack.getComponentsPatch());
        return ScepterContentsComponent.setScepter(scepterStack, scepter);
    }

    /**
     * Get a data component patch for a specified scepter.
     *
     * @param scepter Scepter to get data component patch for.
     * @return Data component patch that applies contents for the specified scepter.
     */
    public static DataComponentPatch getScepterComponentPatch(Holder<Scepter> scepter) {
        return DataComponentPatch.builder().set(ModDataComponentTypes.SCEPTER_CONTENTS,
                ScepterContentsComponent.DEFAULT.with(scepter)).build();
    }

    /**
     * Create a scepter item stack with components from the specified item stack.
     *
     * @param stack Item stack to use components from.
     * @return Item stack of a scepter with the specified item stack components.
     */
    public static ItemStack createScepter(ItemStack stack) {
        ItemStack scepterStack = ModItems.SCEPTER.getDefaultInstance();
        scepterStack.applyComponentsAndValidate(stack.getComponentsPatch());
        scepterStack.remove(ModDataComponentTypes.SCEPTER_CONTENTS);
        scepterStack.setDamageValue(0);
        scepterStack.remove(ModDataComponentTypes.SCEPTER_EXPERIENCE);
        return scepterStack;
    }

    /**
     * Get the scepter registry form the specified world.
     *
     * @param world World to get scepter registry from.
     * @return Scepter registry retrieved from the specified world.
     */
    public static Registry<Scepter> getScepterRegistry(Level world) {
        return world.registryAccess().lookupOrThrow(ModRegistryKeys.SCEPTER);
    }

    /**
     * Get the scepter contents component from the specified player.
     *
     * @param player Player entity to get the scepter contents component from.
     * @return Optional Scepter contents component from the specified player.
     */
    public static Optional<ScepterContentsComponent> getScepterContentsComponent(Player player) {
        if (SCEPTER_WITH_SPELL.test(player.getMainHandItem())) {
            return ScepterContentsComponent.get(player.getMainHandItem());
        } else if (SCEPTER_WITH_SPELL.test(player.getOffhandItem())) {
            return ScepterContentsComponent.get(player.getOffhandItem());
        } else {
            return ScepterContentsComponent.get(player.getMainHandItem())
                    .or(() -> ScepterContentsComponent.get(player.getOffhandItem()));
        }
    }
}

package io.github.pistonpoek.magicalscepter.scepter;

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

    public static ItemStack createScepter(RegistryEntry<Scepter> scepter) {
        return ScepterContentsComponent.setScepter(ModItems.MAGICAL_SCEPTER.getDefaultStack(), scepter);
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
}

package net.pistonpoek.magicalscepter.scepter;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

import static net.pistonpoek.magicalscepter.scepter.ScepterHelper.IS_SCEPTER;

public class ScepterInfusion {
    public static void afterDamage(LivingEntity entity, DamageSource source,
                            float baseDamageTaken, float damageTaken, boolean blocked) {
        if (damageTaken > 0) {
            tryInfuseScepter(entity, source);
        }
    }

    /**
     * Get infusable component value for an item stack.
     *
     * @param itemStack Item stack to get infusable component value for.
     * @return Infusable component value from the item stack.
     */
    private static Optional<Boolean> getInfusable(ItemStack itemStack) {
        return Optional.ofNullable(itemStack.get(ModDataComponentTypes.INFUSABLE));
    }

    /**
     * Predicate to check if an item stack is both a scepter and infusable.
     */
    public static final Predicate<ItemStack> IS_INFUSABLE_SCEPTER = itemStack -> IS_SCEPTER.test(itemStack) &&
            isInfusable(itemStack);

    /**
     * Check if an item stack is infusable.
     *
     * @param itemStack Item stack to check for.
     * @return Truth assignment, if item stack is infusable.
     */
    public static boolean isInfusable(ItemStack itemStack) {
        return getInfusable(itemStack)
                .orElse(isInfusable(ScepterHelper.getScepter(itemStack).orElse(null)));
    }

    /**
     * Check if a scepter entry is infusable.
     *
     * @param scepter Registry entry of a scepter to check.
     * @return Truth assignment, if scepter is infusable.
     */
    public static boolean isInfusable(@Nullable RegistryEntry<Scepter> scepter) {
        if (scepter == null) return false;

        return scepter.value().isInfusable();
    }

    /**
     * Get the infusion for the damage source
     *
     * @param lootContext Loot context to check infusion conditions with.
     *
     * @return Optional scepter for the damage source infusion.
     */
    public static Optional<RegistryEntry<Scepter>> getInfusion(Registry<Scepter> scepterRegistry, LootContext lootContext) {
        for (RegistryEntry<Scepter> scepter: scepterRegistry.streamEntries().toList()) {
            if (scepter.value().infuses(lootContext)) {
                return Optional.of(scepter);
            }
        }
        return Optional.empty();
    }

    /***
     * Try to infuse a scepter for the living entity.
     *
     * @param damageSource Damage source to potentially infuse the scepter with.
     */
    public static void tryInfuseScepter(LivingEntity entity, DamageSource damageSource) {
        // Check if the living entity is holding an infusable scepter
        if (!entity.isHolding(IS_INFUSABLE_SCEPTER)) {
            return;
        }

        // Get the item stack that is the infusable scepter.
        Hand hand = Hand.MAIN_HAND;
        ItemStack itemStack = entity.getMainHandStack();
        if (!IS_INFUSABLE_SCEPTER.test(itemStack)) {
            hand = Hand.OFF_HAND;
            itemStack = entity.getOffHandStack();
        }

        // Get the infusion scepter for the damage source.
        Optional<RegistryEntry<Scepter>> scepter = getInfusion(
                ScepterHelper.getScepterRegistry(entity.getWorld()), getLootContext(entity, damageSource));

        // Check if there is an infusion scepter, if so infuse the held scepter.
        if (scepter.isPresent()) {
            ItemStack infusedScepter = ScepterHelper.setScepter(itemStack, scepter.get());
            entity.setStackInHand(hand, infusedScepter);
        }
    }

    /**
     * Create a loot context for the provided entity and damage source.
     *
     * @param entity Entity to create loot context with.
     * @param damageSource Damage source to create loot context with.
     * @return Loot context constructed with values from the entity and damage source.
     */
    private static LootContext getLootContext(LivingEntity entity, DamageSource damageSource) {
        LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder((ServerWorld)entity.getWorld())
                .add(LootContextParameters.THIS_ENTITY, entity)
                .add(LootContextParameters.ORIGIN, entity.getPos())
                .add(LootContextParameters.DAMAGE_SOURCE, damageSource)
                .addOptional(LootContextParameters.ATTACKING_ENTITY, damageSource.getAttacker())
                .addOptional(LootContextParameters.DIRECT_ATTACKING_ENTITY, damageSource.getSource());
        LootContextParameterSet lootContextParameterSet = builder.build(LootContextTypes.ENTITY);
        return new LootContext.Builder(lootContextParameterSet).build(Optional.empty());
    }
}

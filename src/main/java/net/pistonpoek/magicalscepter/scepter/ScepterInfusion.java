package net.pistonpoek.magicalscepter.scepter;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

import java.util.Optional;

public class ScepterInfusion {
    public static void afterDamage(LivingEntity entity, DamageSource source,
                            float baseDamageTaken, float damageTaken, boolean blocked) {
        if (damageTaken > 0) {
            tryInfuseScepter(entity, source);
        }
    }

    /***
     * Try to infuse a scepter for the living entity.
     *
     * @param damageSource Damage source to potentially infuse the scepter with.
     * @return Truth assignment, if scepter is infused.
     */
    public static boolean tryInfuseScepter(LivingEntity entity, DamageSource damageSource) {
        // Check if the living entity is holding an infusable scepter
        if (!entity.isHolding(ScepterHelper.IS_INFUSABLE_SCEPTER)) {
            return false;
        }

        // Get the item stack that is the infusable scepter.
        boolean foundInMainHand = true;
        ItemStack itemStack = entity.getMainHandStack();
        if (!ScepterHelper.isInfusable(itemStack)) {
            foundInMainHand = false;
            itemStack = entity.getOffHandStack();
        }

        // Get the infusion scepter for the damage source.
        Optional<RegistryEntry<Scepter>> scepter = ScepterHelper.getInfusion(
                ScepterHelper.getScepterRegistry(entity.getWorld()), getLootContext(entity, damageSource));

        // Check if there is an infusion scepter, if so infuse the held scepter.
        if (scepter.isPresent()) {
            ItemStack infusedScepter = ScepterHelper.setScepter(itemStack, scepter.get());

            if (foundInMainHand) {
                entity.setStackInHand(Hand.MAIN_HAND, infusedScepter);
            } else {
                entity.setStackInHand(Hand.OFF_HAND, infusedScepter);
            }
            return true;
        }

        return false;
    }

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

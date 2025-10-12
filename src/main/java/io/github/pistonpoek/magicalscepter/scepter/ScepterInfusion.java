package io.github.pistonpoek.magicalscepter.scepter;

import io.github.pistonpoek.magicalscepter.advancement.criterion.ModCriteria;
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.sound.ModSoundEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

import java.util.Optional;

import static io.github.pistonpoek.magicalscepter.scepter.ScepterHelper.IS_INFUSABLE_SCEPTER;

public class ScepterInfusion {

    /**
     * Get the infusion for the damage source
     *
     * @param lootContext Loot context to check infusion conditions with.
     * @return Optional scepter for the damage source infusion.
     */
    public static Optional<RegistryEntry<Scepter>> getInfusion(
            Registry<Scepter> scepterRegistry, LootContext lootContext) {

        for (RegistryEntry<Scepter> scepter : scepterRegistry.streamEntries().toList()) {
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
                ScepterHelper.getScepterRegistry(entity.getEntityWorld()), getLootContext(entity, damageSource));

        // Check if there is an infusion scepter, if so infuse the held scepter.
        if (scepter.isPresent()) {
            ItemStack infusedScepter = ScepterContentsComponent.setScepter(itemStack, scepter.get());
            entity.setStackInHand(hand, infusedScepter);
            if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
                ModCriteria.INFUSE_SCEPTER.trigger(serverPlayerEntity, scepter.get());
            }
            if (entity.getEntityWorld() instanceof ServerWorld serverWorld) {
                serverWorld.playSoundFromEntity(null, entity,
                        ModSoundEvents.ITEM_MAGICAL_SCEPTER_INFUSE, entity.getSoundCategory(),
                        1.0F, 1.0F);
            }
        }
    }

    /**
     * Create a loot context for the provided entity and damage source.
     *
     * @param entity       Entity to create loot context with.
     * @param damageSource Damage source to create loot context with.
     * @return Loot context constructed with values from the entity and damage source.
     */
    private static LootContext getLootContext(LivingEntity entity, DamageSource damageSource) {
        LootWorldContext.Builder builder = new LootWorldContext.Builder((ServerWorld) entity.getEntityWorld())
                .add(LootContextParameters.THIS_ENTITY, entity)
                .add(LootContextParameters.ORIGIN, entity.getEntityPos())
                .add(LootContextParameters.DAMAGE_SOURCE, damageSource)
                .addOptional(LootContextParameters.ATTACKING_ENTITY, damageSource.getAttacker())
                .addOptional(LootContextParameters.DIRECT_ATTACKING_ENTITY, damageSource.getSource());
        LootWorldContext lootContextParameterSet = builder.build(LootContextTypes.ENTITY);
        return new LootContext.Builder(lootContextParameterSet).build(Optional.empty());
    }

    /**
     * Try to infuse a scepter after damage was taken by a living entity.
     *
     * @see net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AfterDamage
     */
    public static void afterDamage(LivingEntity entity, DamageSource source,
                                   float baseDamageTaken, float damageTaken, boolean blocked) {
        if (damageTaken > 0) {
            tryInfuseScepter(entity, source);
        }
    }
}

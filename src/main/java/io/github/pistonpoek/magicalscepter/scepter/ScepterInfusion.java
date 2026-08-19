package io.github.pistonpoek.magicalscepter.scepter;

import io.github.pistonpoek.magicalscepter.advancement.criterion.ModCriteria;
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.sound.ModSoundEvents;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import static io.github.pistonpoek.magicalscepter.scepter.ScepterHelper.INFUSABLE_SCEPTER;

/**
 * Helper class for infusing a scepter.
 */
public class ScepterInfusion {

    /**
     * Get the infusion for the damage source
     *
     * @param lootContext Loot context to check infusion conditions with.
     * @return Optional scepter for the damage source infusion.
     */
    public static Optional<Holder<Scepter>> getInfusion(
            Registry<Scepter> scepterRegistry, LootContext lootContext) {

        for (Holder<Scepter> scepter : scepterRegistry.listElements().toList()) {
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
        if (!entity.isHolding(INFUSABLE_SCEPTER)) {
            return;
        }

        // Get the item stack that is the infusable scepter.
        InteractionHand hand = InteractionHand.MAIN_HAND;
        ItemStack itemStack = entity.getMainHandItem();
        if (!INFUSABLE_SCEPTER.test(itemStack)) {
            hand = InteractionHand.OFF_HAND;
            itemStack = entity.getOffhandItem();
        }

        // Get the infusion scepter for the damage source.
        Optional<Holder<Scepter>> scepter = getInfusion(
                ScepterHelper.getScepterRegistry(entity.level()), getLootContext(entity, damageSource));

        // Check if there is an infusion scepter, if so infuse the held scepter.
        if (scepter.isPresent()) {
            ItemStack infusedScepter = ScepterContentsComponent.setScepter(itemStack, scepter.get());
            entity.setItemInHand(hand, infusedScepter);
            if (entity instanceof ServerPlayer serverPlayerEntity) {
                ModCriteria.INFUSE_SCEPTER.trigger(serverPlayerEntity, scepter.get());
            }
            if (entity.level() instanceof ServerLevel serverWorld) {
                serverWorld.playSound(null, entity,
                        ModSoundEvents.ITEM_MAGICAL_SCEPTER_INFUSE, entity.getSoundSource(),
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
        LootParams.Builder builder = new LootParams.Builder((ServerLevel) entity.level())
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .withParameter(LootContextParams.ORIGIN, entity.position())
                .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
                .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, damageSource.getEntity())
                .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, damageSource.getDirectEntity());
        LootParams lootContextParameterSet = builder.create(LootContextParamSets.ENTITY);
        return new LootContext.Builder(lootContextParameterSet).create(Optional.empty());
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

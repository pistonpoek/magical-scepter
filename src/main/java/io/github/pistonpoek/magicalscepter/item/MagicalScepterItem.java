package io.github.pistonpoek.magicalscepter.item;

import io.github.pistonpoek.magicalscepter.advancement.criterion.ModCriteria;
import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import io.github.pistonpoek.magicalscepter.sound.ModSoundEvents;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import io.github.pistonpoek.magicalscepter.util.PlayerExperience;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * Magical scepter item that can cast spells.
 */
public class MagicalScepterItem extends Item implements AttackItem {
    /**
     * Construct a magical scepter item with the specified item settings.
     *
     * @param settings Item settings to create item with.
     */
    public MagicalScepterItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        return performAction(world, user, hand, false);
    }

    @Override
    public InteractionResult attack(Level world, Player user) {
        return performAction(world, user, InteractionHand.MAIN_HAND, true);
    }

    /**
     * Activate the magical scepter to potentially cast a spell.
     *
     * @param world    World that the user tries to perform the action in.
     * @param user     Player entity that is to perform the action.
     * @param hand     Hand that the user is performing the action with.
     * @param isAttack Truth assignment, if the action is an attack.
     * @return Action result of the performed action.
     */
    private InteractionResult performAction(Level world, Player user, InteractionHand hand, boolean isAttack) {
        ItemStack itemStack = user.getItemInHand(hand);
        ScepterContentsComponent scepterContent =
                ScepterContentsComponent.get(itemStack).orElse(ScepterContentsComponent.DEFAULT);

        Optional<Spell> optionalSpell = (isAttack ?
                scepterContent.getAttackSpell() :
                scepterContent.getProtectSpell()).map(Holder::value);

        if (optionalSpell.isEmpty()) {
            return InteractionResult.PASS;
        }

        Spell spell = optionalSpell.get();

        if (!user.hasInfiniteMaterials()) {
            if (!scepterContent.hasEnoughExperience(user)) {
                return InteractionResult.PASS;
            }

            int experienceCost = scepterContent.getExperienceCost();
            PlayerExperience.addOnlyExperience(user, -experienceCost);
        }

        user.startUsingItem(hand);
        user.getCooldowns().addCooldown(itemStack, spell.getCooldown());
        user.awardStat(Stats.ITEM_USED.get(this));

        ItemStack usedScepterStack = MagicalScepterItem.castSpell(spell, user, itemStack, isAttack, hand);

        return InteractionResult.CONSUME.heldItemTransformedTo(usedScepterStack);
    }

    /**
     * Cast a specified spell for a specific living entity.
     *
     * @param spell     Spell to cast.
     * @param caster    Living entity to cast the spell for.
     * @param itemStack Item stack that the spell is cast with.
     * @param isAttack  Truth assignment, if cast is an attack and not protect.
     * @param hand      Hand being used to cast the spell.
     * @return Damaged item stack that was used to cast the spell.
     */
    public static ItemStack castSpell(Spell spell, LivingEntity caster,
                                      ItemStack itemStack,
                                      boolean isAttack, InteractionHand hand) {
        caster.makeSound(isAttack ?
                ModSoundEvents.ITEM_MAGICAL_SCEPTER_CAST_ATTACK_SPELL :
                ModSoundEvents.ITEM_MAGICAL_SCEPTER_CAST_PROTECT_SPELL);

        SwingType swingType = isAttack ? SwingType.HIT : SwingType.PROTECT;
        // TODO swing hand update.
//        ((SwingHandLivingEntity) caster).magical_scepter$swingHand(hand, swingType);

        if (caster.level().isClientSide()) {
            return itemStack;
        }

        if (caster instanceof ServerPlayer serverPlayerEntity) {
            ModCriteria.CAST_SCEPTER.trigger(serverPlayerEntity, itemStack);
        }

        spell.castSpell(caster);

        ItemStack replacementStack = ItemStack.EMPTY;
        if (itemStack.nextDamageWillBreak() && itemStack.is(ModItems.MAGICAL_SCEPTER)) {
            replacementStack = ScepterHelper.createScepter(itemStack);
            replacementStack.setDamageValue(0);
        }
        itemStack.hurtAndBreak(1, caster, hand.asEquipmentSlot());

        return !itemStack.isEmpty() ? itemStack : replacementStack;
    }

    @Override
    public Component getName(ItemStack stack) {
        ScepterContentsComponent scepterContentsComponent = stack.get(ModDataComponentTypes.SCEPTER_CONTENTS);
        return scepterContentsComponent != null ?
                Component.translatable(this.getDescriptionId() + "." +
                        scepterContentsComponent.getTranslationKey()) :
                super.getName(stack);
    }
}

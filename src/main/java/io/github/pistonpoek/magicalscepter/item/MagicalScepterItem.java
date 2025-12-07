package io.github.pistonpoek.magicalscepter.item;

import io.github.pistonpoek.magicalscepter.advancement.criterion.ModCriteria;
import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import io.github.pistonpoek.magicalscepter.sound.ModSoundEvents;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import io.github.pistonpoek.magicalscepter.util.PlayerExperience;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

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
    public MagicalScepterItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        return performAction(world, user, hand, false);
    }

    @Override
    public ActionResult attack(World world, PlayerEntity user) {
        return performAction(world, user, Hand.MAIN_HAND, true);
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
    private ActionResult performAction(World world, PlayerEntity user, Hand hand, boolean isAttack) {
        ItemStack itemStack = user.getStackInHand(hand);
        ScepterContentsComponent scepterContent =
                ScepterContentsComponent.get(itemStack).orElse(ScepterContentsComponent.DEFAULT);

        Optional<Spell> optionalSpell = (isAttack ?
                scepterContent.getAttackSpell() :
                scepterContent.getProtectSpell()).map(RegistryEntry::value);

        if (optionalSpell.isEmpty()) {
            return ActionResult.PASS;
        }

        Spell spell = optionalSpell.get();

        if (!user.isInCreativeMode()) {
            if (!scepterContent.hasEnoughExperience(user)) {
                return ActionResult.PASS;
            }

            int experienceCost = scepterContent.getExperienceCost();
            PlayerExperience.addOnlyExperience(user, -experienceCost);
        }
        user.setCurrentHand(hand);
        user.getItemCooldownManager().set(itemStack,
                user.isInCreativeMode() ?
                        10 : spell.getCooldown()
        );
        user.incrementStat(Stats.USED.getOrCreateStat(this));

        ItemStack usedScepterStack = MagicalScepterItem.castSpell(spell, user, itemStack, isAttack, hand);

        return ActionResult.CONSUME.withNewHandStack(usedScepterStack);
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
                                      boolean isAttack, Hand hand) {
        caster.playSound(isAttack ?
                ModSoundEvents.ITEM_MAGICAL_SCEPTER_CAST_ATTACK_SPELL :
                ModSoundEvents.ITEM_MAGICAL_SCEPTER_CAST_PROTECT_SPELL);

        SwingType swingType = isAttack ? SwingType.HIT : SwingType.PROTECT;
        ((SwingHandLivingEntity) caster).magical_scepter$swingHand(hand, swingType);

        if (caster.getEntityWorld().isClient()) {
            return itemStack;
        }

        if (caster instanceof ServerPlayerEntity serverPlayerEntity) {
            ModCriteria.CAST_SCEPTER.trigger(serverPlayerEntity, itemStack);
        }

        spell.castSpell(caster);

        ItemStack replacementStack = ItemStack.EMPTY;
        if (itemStack.willBreakNextUse() && itemStack.isOf(ModItems.MAGICAL_SCEPTER)) {
            replacementStack = ScepterHelper.createScepter(itemStack);
            replacementStack.setDamage(0);
        }
        itemStack.damage(1, caster, hand.getEquipmentSlot());

        return !itemStack.isEmpty() ? itemStack : replacementStack;
    }

    @Override
    public Text getName(ItemStack stack) {
        ScepterContentsComponent scepterContentsComponent = stack.get(ModDataComponentTypes.SCEPTER_CONTENTS);
        return scepterContentsComponent != null ?
                Text.translatable(this.getTranslationKey() + "." +
                        scepterContentsComponent.getTranslationKey()) :
                super.getName(stack);
    }
}

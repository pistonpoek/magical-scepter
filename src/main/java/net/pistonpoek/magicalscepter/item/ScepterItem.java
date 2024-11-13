package net.pistonpoek.magicalscepter.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import net.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import net.pistonpoek.magicalscepter.spell.Spell;

import java.util.List;
import java.util.Optional;

public class ScepterItem extends Item {
    public ScepterItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        Optional<Spell> optionalSpell = (!user.isSneaking() ?
                ScepterContentsComponent.getAttackSpell(itemStack) :
                ScepterContentsComponent.getProtectSpell(itemStack));

        if (optionalSpell.isEmpty()) {
            return TypedActionResult.pass(itemStack);
        }

        Spell spell = optionalSpell.get();

        if (!user.getAbilities().creativeMode) {
            if (!spell.hasEnoughExperience(user)) {
                return TypedActionResult.fail(itemStack);
            }

            user.addExperience(-spell.getExperienceCost());
            user.addScore(spell.getExperienceCost()); // Compensating for lost score in adding experience cost.
        }
        user.getItemCooldownManager().set(this, spell.getCooldown());

        user.incrementStat(Stats.USED.getOrCreateStat(this));

        if (!world.isClient()) {
            int castDuration = spell.castSpell(user);

            ItemStack damagedItemStack = itemStack;
            itemStack.damage(1, user, LivingEntity.getSlotForHand(hand));
            if (itemStack.isEmpty()) {
                damagedItemStack = ModItems.SCEPTER.getDefaultStack();
                damagedItemStack.applyChanges(itemStack.getComponentChanges());
                damagedItemStack.remove(ModDataComponentTypes.SCEPTER_CONTENTS);
                if (itemStack.isDamageable()) {
                    itemStack.setDamage(0);
                }
            }

            // Correct spell duration cooldown, increase cooldown for non-creative and decrease for creative players.
            if (user.getAbilities().creativeMode ^ castDuration + 10 >= spell.getCooldown()) {
                user.getItemCooldownManager().set(this, castDuration + 10);
            }

            return TypedActionResult.success(damagedItemStack, !user.isSneaking());
        }
        return TypedActionResult.pass(itemStack); // TODO fix using scepter allows items in offhand to also be used.
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        Optional<ScepterContentsComponent> scepterContentsComponent = ScepterContentsComponent.get(stack);
        scepterContentsComponent.ifPresent(contentsComponent -> contentsComponent.buildTooltip(tooltip::add));
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        String key = ScepterContentsComponent.getTranslationKey(stack);
        if (key.equals("")) {
            return this.getTranslationKey();
        }
        return String.join(".", this.getTranslationKey(), key);
    }

}

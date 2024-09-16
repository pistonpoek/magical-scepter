package net.pistonpoek.magicalscepter.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import net.pistonpoek.magicalscepter.spell.Spell;

import java.util.Optional;

public class ScepterItem extends Item {
    public final static int MAX_SPELL_TIME = 100;
    public ScepterItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        Optional<RegistryEntry<Spell>> optionalSpell = (!user.isSneaking() ?
                ScepterContentsComponent.getAttackSpell(itemStack) :
                ScepterContentsComponent.getProtectSpell(itemStack));

        if (optionalSpell.isEmpty()) {
            return TypedActionResult.pass(itemStack);
        }

        Spell spell = optionalSpell.get().value();

        if (!user.getAbilities().creativeMode) {
            if (user.totalExperience < spell.experienceCost()) {
                return TypedActionResult.fail(itemStack);
            }

            user.addExperience(-spell.experienceCost());
            user.addScore(spell.experienceCost()); // Compensating for lost score in adding experience cost.
            user.getItemCooldownManager().set(this, spell.cooldown());
        } else {
            user.getItemCooldownManager().set(this, spell.getDuration() + 10);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));

        if (!world.isClient()) {
            spell.castSpell(user);
            ItemStack damagedItemStack = itemStack.damage(1, ModItems.SCEPTER, user, LivingEntity.getSlotForHand(hand));
            return TypedActionResult.success(damagedItemStack, !user.isSneaking());
        }
        return TypedActionResult.pass(itemStack);
    }

    @Override
    public void usageTick(World world, LivingEntity player, ItemStack stack, int remainingUseTicks) {
//        if (!world.isClient()) {
//            ScepterHelper.getSpell(stack).updateSpell(player, MAX_SPELL_TIME - remainingUseTicks);
//        } else {
//            ScepterHelper.getSpell(stack).displaySpell(world, player, MAX_SPELL_TIME - remainingUseTicks);
//        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return super.postHit(stack, target, attacker);

    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity player, int remainingUseTicks) {
//        if (!world.isClient()) {
//            ScepterHelper.getSpell(stack).endSpell(player, MAX_SPELL_TIME - remainingUseTicks);
//        }
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity player) {
        return stack;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity player) {
        return MAX_SPELL_TIME;
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

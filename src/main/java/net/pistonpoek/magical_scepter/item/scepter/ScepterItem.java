package net.pistonpoek.magical_scepter.item.scepter;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.pistonpoek.magical_scepter.item.ModItems;

import static net.pistonpoek.magical_scepter.item.scepter.ScepterUtil.getScepter;

public class ScepterItem extends Item {
    public final static int MAX_SPELL_TIME = 100;
    public ScepterItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        Scepter scepter = getScepter(itemStack);
        if (!user.getAbilities().creativeMode) {
            if (user.totalExperience < scepter.getExperienceCost()) {
                return TypedActionResult.fail(itemStack);
            }

            user.addExperience(-scepter.getExperienceCost());
            user.addScore(scepter.getExperienceCost()); // Compensating for lost score in adding experience cost.
            user.getItemCooldownManager().set(this, scepter.getSpellCooldown());
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));

        scepter.setCastingSpell(!user.isSneaking());
        if (!world.isClient()) {
            scepter.castSpell(user);
            ItemStack damagedItemStack = itemStack.damage(1, ModItems.EMPTY_SCEPTER, user, LivingEntity.getSlotForHand(hand));
            return TypedActionResult.success(damagedItemStack, false);
        } else {
            scepter.displaySpell(user, 0);
        }
        if (!scepter.isInstantSpell()) {
            user.setCurrentHand(hand);
        }
        return TypedActionResult.pass(itemStack);
    }

    @Override
    public void usageTick(World world, LivingEntity player, ItemStack stack, int remainingUseTicks) {
        Scepter scepter = getScepter(stack);
        if (!world.isClient()) {
            scepter.updateSpell(player, MAX_SPELL_TIME - remainingUseTicks);
        } else {
            scepter.displaySpell(player, MAX_SPELL_TIME - remainingUseTicks);
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return super.postHit(stack, target, attacker);

    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity player, int remainingUseTicks) {
        if (!world.isClient()) {
            Scepter scepter = getScepter(stack);
            scepter.endSpell(player, MAX_SPELL_TIME - remainingUseTicks);
        }
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
        return getScepter(stack).finishTranslationKey(this.getTranslationKey() + ".spell.");
    }

}

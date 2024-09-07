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
import net.pistonpoek.magical_scepter.ModRegistries;
import net.pistonpoek.magical_scepter.item.ModItems;

import static net.pistonpoek.magical_scepter.item.scepter.ScepterHelper.getScepter;

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
            if (user.totalExperience < scepter.getSpell().experienceCost()) {
                return TypedActionResult.fail(itemStack);
            }

            user.addExperience(-scepter.getSpell().experienceCost());
            user.addScore(scepter.getSpell().experienceCost()); // Compensating for lost score in adding experience cost.
            user.getItemCooldownManager().set(this, scepter.getSpell().cooldown());
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));

        //scepter.setCastingSpell(!user.isSneaking());
        if (!world.isClient()) {
            scepter.getSpell().castSpell(user);
            ItemStack damagedItemStack = itemStack.damage(1, ModItems.EMPTY_SCEPTER, user, LivingEntity.getSlotForHand(hand));
            return TypedActionResult.success(damagedItemStack, false);
        } else {
            scepter.getSpell().displaySpell(user, 0);
        }
        if (!scepter.getSpell().isInstant()) {
            user.setCurrentHand(hand);
        }
        return TypedActionResult.pass(itemStack);
    }

    @Override
    public void usageTick(World world, LivingEntity player, ItemStack stack, int remainingUseTicks) {
        Scepter scepter = getScepter(stack);
        if (!world.isClient()) {
            scepter.getSpell().updateSpell(player, MAX_SPELL_TIME - remainingUseTicks);
        } else {
            scepter.getSpell().displaySpell(player, MAX_SPELL_TIME - remainingUseTicks);
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
            scepter.getSpell().endSpell(player, MAX_SPELL_TIME - remainingUseTicks);
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
        return this.getTranslationKey() + "." + getScepter(stack).getId();
    }

}

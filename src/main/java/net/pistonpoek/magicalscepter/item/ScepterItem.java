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
import net.pistonpoek.magicalscepter.MagicalScepter;
import net.pistonpoek.magicalscepter.registry.ModTags;
import net.pistonpoek.magicalscepter.scepter.Scepter;
import net.pistonpoek.magicalscepter.scepter.ScepterHelper;
import net.pistonpoek.magicalscepter.scepter.Scepters;

import java.util.Optional;

public class ScepterItem extends Item {
    public final static int MAX_SPELL_TIME = 100;
    public ScepterItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        MagicalScepter.LOGGER.info("Checking infusable scepters");
        ScepterHelper.getScepterRegistry(world).iterateEntries(ModTags.Scepters.INFUSABLE).forEach(scepter -> {
            MagicalScepter.LOGGER.info("Got infusable scepter {}", scepter.getIdAsString());
        });

        ItemStack itemStack = user.getStackInHand(hand);
        Scepter scepter = ScepterHelper.getScepter(itemStack).value();

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
        if (!world.isClient()) {
            ScepterHelper.getSpell(stack).updateSpell(player, MAX_SPELL_TIME - remainingUseTicks);
        } else {
            ScepterHelper.getSpell(stack).displaySpell(player, MAX_SPELL_TIME - remainingUseTicks);
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return super.postHit(stack, target, attacker);

    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity player, int remainingUseTicks) {
        if (!world.isClient()) {
            ScepterHelper.getSpell(stack).endSpell(player, MAX_SPELL_TIME - remainingUseTicks);
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
        return this.getTranslationKey() + "." + ScepterHelper.getScepter(stack)
                .getKey().orElse(Scepters.DEFAULT_KEY).getValue().getPath();
    }

}

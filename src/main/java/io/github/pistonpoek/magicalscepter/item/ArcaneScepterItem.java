package io.github.pistonpoek.magicalscepter.item;

import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import io.github.pistonpoek.magicalscepter.enchantment.ModEnchantmentHelper;
import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import io.github.pistonpoek.magicalscepter.sound.ModSoundEvents;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import io.github.pistonpoek.magicalscepter.util.PlayerExperience;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.List;

public class ArcaneScepterItem extends Item {
    /**
     * Construct an arcane scepter itemStack with the specified itemStack settings.
     *
     * @param settings Item settings to create itemStack with.
     */
    public ArcaneScepterItem(Settings settings) {
        super(settings);
    }

    public static final int EXPERIENCE_STEP = 7;

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        int playerExperience = PlayerExperience.getTotalExperience(user);
        int scepterExperience = itemStack.getOrDefault(ModDataComponentTypes.EXPERIENCE, 0);
        int step = ModEnchantmentHelper.getExperienceStep(itemStack, user, EXPERIENCE_STEP);

        boolean collecting = false;
        if (playerExperience >= step) {
            collecting = true;
        } else if (scepterExperience < step) {
            return ActionResult.PASS;
        }

        scepterExperience = collectExperience(itemStack, user, collecting ? step : -step);
        user.playSound(collecting ?
                ModSoundEvents.ITEM_ARCANE_SCEPTER_COLLECT_EXPERIENCE :
                ModSoundEvents.ITEM_ARCANE_SCEPTER_RELEASE_EXPERIENCE);

        user.getItemCooldownManager().set(itemStack, user.getAbilities().creativeMode ? 3 : 10);
        user.incrementStat(Stats.USED.getOrCreateStat(this));

        ItemStack replacementStack = ItemStack.EMPTY;
        if (itemStack.willBreakNextUse() && itemStack.isOf(ModItems.ARCANE_SCEPTER)) {
            replacementStack = ScepterHelper.createScepter(itemStack);
            replacementStack.setDamage(0);
        }
        itemStack.damage(1, user, LivingEntity.getSlotForHand(hand));

        if (!itemStack.isEmpty()) {
            return ActionResult.SUCCESS;
        }

        if (!world.isClient()) {
            ExperienceOrbEntity.spawn((ServerWorld)world, user.getPos(), scepterExperience);
            replacementStack.remove(ModDataComponentTypes.EXPERIENCE);
        }

        return ActionResult.SUCCESS.withNewHandStack(replacementStack);
    }

    private int collectExperience(ItemStack itemStack, PlayerEntity player, int amount) {
        int experience = itemStack.getOrDefault(ModDataComponentTypes.EXPERIENCE, 0) + amount;
        itemStack.set(ModDataComponentTypes.EXPERIENCE, experience);
        PlayerExperience.addOnlyExperience(player, -amount);
        return experience;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        super.appendTooltip(stack, context, tooltip, options);
        int experience = stack.getOrDefault(ModDataComponentTypes.EXPERIENCE, 0);
        tooltip.add(ModIdentifier.translatable("scepter.experience", experience));
    }

}

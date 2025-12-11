package io.github.pistonpoek.magicalscepter.item;

import io.github.pistonpoek.magicalscepter.component.ScepterExperienceComponent;
import io.github.pistonpoek.magicalscepter.enchantment.ModEnchantmentHelper;
import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import io.github.pistonpoek.magicalscepter.sound.ModSoundEvents;
import io.github.pistonpoek.magicalscepter.util.PlayerExperience;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/**
 * Scepter item that can charge with experience.
 */
public class ArcaneScepterItem extends Item {
    public static final int EXPERIENCE_STEP = 7;

    /**
     * Construct an arcane scepter itemStack with the specified itemStack settings.
     *
     * @param settings Item settings to create itemStack with.
     */
    public ArcaneScepterItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        int playerExperience = PlayerExperience.getTotalExperience(user);
        int scepterExperience = ScepterExperienceComponent.getExperience(itemStack);
        int step = ModEnchantmentHelper.getExperienceStep(itemStack, user, EXPERIENCE_STEP);

        if (playerExperience >= step || user.isInCreativeMode()) chargeScepter(user, itemStack, step);
        else if (scepterExperience >= step) drainScepter(user, itemStack, step);
        else return ActionResult.PASS;

        user.incrementStat(Stats.USED.getOrCreateStat(this));

        ItemStack replacementStack = ItemStack.EMPTY;
        // Set replacement stack to scepter if the item will break.
        if (itemStack.willBreakNextUse() && ScepterHelper.ARCANE_SCEPTER.test(itemStack)) {
            replacementStack = ScepterHelper.createScepter(itemStack);
        }

        itemStack.damage(1, user, hand.getEquipmentSlot());

        // Get replacement stack scepter based on changed experience.
        if (replacementStack.isEmpty()) replacementStack = getReplacementStack(itemStack);

        // Drop all experience when the arcane scepter is used up.
        if (!world.isClient() && itemStack.isEmpty()) {
            ExperienceOrbEntity.spawn((ServerWorld) world, user.getEntityPos(), scepterExperience);
        }

        // Set success with potential replacement stack.
        return replacementStack == ItemStack.EMPTY ? ActionResult.SUCCESS :
                ActionResult.SUCCESS.withNewHandStack(replacementStack);
    }

    public static ItemStack getReplacementStack(ItemStack itemStack) {
        ItemStack replacementStack = ItemStack.EMPTY;
        int experience = ScepterExperienceComponent.getExperience(itemStack);
        // Set replacement stack to charged scepter if arcane scepter has sufficient experience to be charged.
        if (itemStack.isOf(ModItems.ARCANE_SCEPTER) && experience >= EXPERIENCE_STEP) {
            replacementStack = itemStack.copyComponentsToNewStack(ModItems.CHARGED_ARCANE_SCEPTER, 1);
        }

        // Set replacement stack to arcane scepter if charged scepter has insufficient experience to be charged.
        if (itemStack.isOf(ModItems.CHARGED_ARCANE_SCEPTER) && experience < EXPERIENCE_STEP) {
            replacementStack = itemStack.copyComponentsToNewStack(ModItems.ARCANE_SCEPTER, 1);
        }
        return replacementStack;
    }

    private static void chargeScepter(PlayerEntity user, ItemStack itemStack, int change) {
        transferExperience(user, itemStack, change);
        user.playSound(ModSoundEvents.ITEM_ARCANE_SCEPTER_COLLECT_EXPERIENCE);
    }

    private static void drainScepter(PlayerEntity user, ItemStack itemStack, int change) {
        transferExperience(user, itemStack, -change);
        user.playSound(ModSoundEvents.ITEM_ARCANE_SCEPTER_RELEASE_EXPERIENCE);
    }

    private static void transferExperience(PlayerEntity user, ItemStack itemStack, int change) {
        if (!user.isInCreativeMode()) {
            PlayerExperience.addOnlyExperience(user, -change);
        }
        ScepterExperienceComponent.add(itemStack, change);
    }
}

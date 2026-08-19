package io.github.pistonpoek.magicalscepter.item;

import io.github.pistonpoek.magicalscepter.component.ScepterExperienceComponent;
import io.github.pistonpoek.magicalscepter.enchantment.ModEnchantmentHelper;
import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import io.github.pistonpoek.magicalscepter.sound.ModSoundEvents;
import io.github.pistonpoek.magicalscepter.util.PlayerExperience;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

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
    public ArcaneScepterItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        int playerExperience = PlayerExperience.getTotalExperience(user);
        int scepterExperience = ScepterExperienceComponent.getExperience(itemStack);
        int step = ModEnchantmentHelper.getExperienceStep(itemStack, user, EXPERIENCE_STEP);

        if (playerExperience >= step || user.hasInfiniteMaterials()) chargeScepter(user, itemStack, step);
        else if (scepterExperience >= step) drainScepter(user, itemStack, step);
        else return InteractionResult.PASS;

        user.awardStat(Stats.ITEM_USED.get(this));

        ItemStack replacementStack = ItemStack.EMPTY;
        // Set replacement stack to scepter if the item will break.
        if (itemStack.nextDamageWillBreak() && ScepterHelper.ARCANE_SCEPTER.test(itemStack)) {
            replacementStack = ScepterHelper.createScepter(itemStack);
        }

        itemStack.hurtAndBreak(1, user, hand.asEquipmentSlot());

        // Get replacement stack scepter based on changed experience.
        if (replacementStack.isEmpty()) replacementStack = getReplacementStack(itemStack);

        // Drop all experience when the arcane scepter is used up.
        if (!world.isClientSide() && itemStack.isEmpty()) {
            ExperienceOrb.award((ServerLevel) world, user.position(), scepterExperience);
        }

        // Set success with potential replacement stack.
        return replacementStack == ItemStack.EMPTY ? InteractionResult.SUCCESS :
                InteractionResult.SUCCESS.heldItemTransformedTo(replacementStack);
    }

    public static ItemStack getReplacementStack(ItemStack itemStack) {
        ItemStack replacementStack = ItemStack.EMPTY;
        int experience = ScepterExperienceComponent.getExperience(itemStack);
        // Set replacement stack to charged scepter if arcane scepter has sufficient experience to be charged.
        if (itemStack.is(ModItems.ARCANE_SCEPTER) && experience >= EXPERIENCE_STEP) {
            replacementStack = itemStack.transmuteCopy(ModItems.CHARGED_ARCANE_SCEPTER, 1);
        }

        // Set replacement stack to arcane scepter if charged scepter has insufficient experience to be charged.
        if (itemStack.is(ModItems.CHARGED_ARCANE_SCEPTER) && experience < EXPERIENCE_STEP) {
            replacementStack = itemStack.transmuteCopy(ModItems.ARCANE_SCEPTER, 1);
        }
        return replacementStack;
    }

    private static void chargeScepter(Player user, ItemStack itemStack, int change) {
        transferExperience(user, itemStack, change);
        user.makeSound(ModSoundEvents.ITEM_ARCANE_SCEPTER_COLLECT_EXPERIENCE);
    }

    private static void drainScepter(Player user, ItemStack itemStack, int change) {
        transferExperience(user, itemStack, -change);
        user.makeSound(ModSoundEvents.ITEM_ARCANE_SCEPTER_RELEASE_EXPERIENCE);
    }

    private static void transferExperience(Player user, ItemStack itemStack, int change) {
        if (!user.hasInfiniteMaterials()) {
            PlayerExperience.addOnlyExperience(user, -change);
        }
        ScepterExperienceComponent.add(itemStack, change);
    }
}

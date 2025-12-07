package io.github.pistonpoek.magicalscepter.item;

import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
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
        int scepterExperience = ScepterHelper.getExperience(itemStack);
        int step = ModEnchantmentHelper.getExperienceStep(itemStack, user, EXPERIENCE_STEP);

        boolean collecting = true;
        if (playerExperience < step && !user.isInCreativeMode()) {
            collecting = false;
            if (scepterExperience < step) {
                return ActionResult.PASS;
            }
        }

        int change = collecting ? step : -step;
        scepterExperience = ScepterHelper.getExperience(itemStack) + change;

        if (!user.isInCreativeMode()) {
                PlayerExperience.addOnlyExperience(user, -change);
        }

        itemStack.set(ModDataComponentTypes.SCEPTER_EXPERIENCE, new ScepterExperienceComponent(scepterExperience));

        user.playSound(collecting ?
                ModSoundEvents.ITEM_ARCANE_SCEPTER_COLLECT_EXPERIENCE :
                ModSoundEvents.ITEM_ARCANE_SCEPTER_RELEASE_EXPERIENCE);

        user.getItemCooldownManager().set(itemStack, user.isInCreativeMode() ? 3 : 10);

        user.incrementStat(Stats.USED.getOrCreateStat(this));

        ItemStack replacementStack = ItemStack.EMPTY;
        if (itemStack.willBreakNextUse() && itemStack.isOf(ModItems.ARCANE_SCEPTER)) {
            replacementStack = ScepterHelper.createScepter(itemStack);
            replacementStack.setDamage(0);
            replacementStack.remove(ModDataComponentTypes.SCEPTER_EXPERIENCE);
        }
        itemStack.damage(1, user, hand.getEquipmentSlot());

        if (!itemStack.isEmpty()) {
            return ActionResult.SUCCESS;
        }

        if (!world.isClient()) {
            ExperienceOrbEntity.spawn((ServerWorld) world, user.getEntityPos(), scepterExperience);
        }

        return ActionResult.SUCCESS.withNewHandStack(replacementStack);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        int experience = stack.getOrDefault(ModDataComponentTypes.SCEPTER_EXPERIENCE,
                ScepterExperienceComponent.DEFAULT).experience();
        return experience > 0 || super.hasGlint(stack);
    }
}

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

public class ScepterItem extends Item implements AttackItem {
    public ScepterItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return use(world, user, hand, false);
    }

    @Override
    public TypedActionResult<ItemStack> attack(World world, PlayerEntity user) {
        return use(world, user, Hand.MAIN_HAND, true);
    }

    @Override
    public boolean preventAttack(PlayerEntity user) {
        ItemStack itemStack = user.getStackInHand(Hand.MAIN_HAND);
        return ScepterContentsComponent.getScepter(itemStack).isPresent();
    }

    private TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand, boolean attack) {
        ItemStack itemStack = user.getStackInHand(hand);

        Optional<Spell> optionalSpell = (attack ?
                ScepterContentsComponent.getAttackSpell(itemStack) :
                ScepterContentsComponent.getProtectSpell(itemStack));

        if (optionalSpell.isEmpty()) {
            return TypedActionResult.pass(itemStack);
        }

        Spell spell = optionalSpell.get();

        if (!user.getAbilities().creativeMode) {
            if (!spell.hasEnoughExperience(user)) {
                // TODO maybe play sound effect that there is not enough xp to cast the spell.
                return TypedActionResult.fail(itemStack);
            }

            user.addExperience(-spell.getExperienceCost());
            user.addScore(spell.getExperienceCost()); // Compensating for lost score in adding experience cost.
        }
        user.getItemCooldownManager().set(this, spell.getCooldown());

        user.incrementStat(Stats.USED.getOrCreateStat(this));

        if (!world.isClient()) {
            int castDuration = spell.castSpell(user);
            // Correct spell duration cooldown, increase cooldown for non-creative and decrease for creative players.
            if (user.getAbilities().creativeMode ^ castDuration + 10 >= spell.getCooldown()) {
                user.getItemCooldownManager().set(this, castDuration + 10);
            }

            itemStack.damage(1, user, LivingEntity.getSlotForHand(hand));
            if (itemStack.isEmpty()) {
                itemStack = createEmptyScepter(itemStack);
            }
        }

        SwingType swingType = attack ? SwingType.HIT : SwingType.PROTECT;
        ((SwingHandLivingEntity)user).swingHand(Hand.MAIN_HAND, swingType);

        return TypedActionResult.success(itemStack, false);
    }

    public ItemStack createEmptyScepter(ItemStack stack) {
        ItemStack scepterStack = ModItems.SCEPTER.getDefaultStack();
        scepterStack.applyChanges(stack.getComponentChanges());
        scepterStack.remove(ModDataComponentTypes.SCEPTER_CONTENTS);
        if (scepterStack.isDamageable()) {
            scepterStack.setDamage(0);
        }
        return scepterStack;
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

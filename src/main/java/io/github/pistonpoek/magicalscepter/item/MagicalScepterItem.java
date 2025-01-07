package io.github.pistonpoek.magicalscepter.item;

import io.github.pistonpoek.magicalscepter.advancement.criteria.ModCriteria;
import io.github.pistonpoek.magicalscepter.sound.ModSoundEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import io.github.pistonpoek.magicalscepter.component.ModDataComponentTypes;
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class MagicalScepterItem extends Item implements AttackItem {
    public MagicalScepterItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        return use(world, user, hand, false);
    }

    @Override
    public ActionResult attack(World world, PlayerEntity user) {
        return use(world, user, Hand.MAIN_HAND, true);
    }

    private ActionResult use(World world, PlayerEntity user, Hand hand, boolean attackCast) {
        ItemStack itemStack = user.getStackInHand(hand);
        ScepterContentsComponent scepterContent =
                ScepterContentsComponent.get(itemStack).orElse(ScepterContentsComponent.DEFAULT);

        Optional<Spell> optionalSpell = (attackCast ?
                scepterContent.getAttackSpell() :
                scepterContent.getProtectSpell()).map(RegistryEntry::value);

        if (optionalSpell.isEmpty()) {
            return ActionResult.PASS;
        }

        Spell spell = optionalSpell.get();

        if (!user.getAbilities().creativeMode) {
            if (!scepterContent.hasEnoughExperience(user)) {
                return ActionResult.FAIL;
            }

            int experienceCost = scepterContent.getExperienceCost();
            user.addExperience(-experienceCost);
            user.addScore(experienceCost); // Compensating for lost score in adding experience cost.
        }
        user.setCurrentHand(hand);
        user.getItemCooldownManager().set(itemStack,
                user.getAbilities().creativeMode ?
                    10 : spell.getCooldown()
        );
        user.incrementStat(Stats.USED.getOrCreateStat(this));

        MagicalScepterItem.castSpell(spell, user, itemStack, attackCast, hand);

        if (!world.isClient()) {
            itemStack.damage(1, user, LivingEntity.getSlotForHand(hand));
            if (itemStack.isEmpty()) {
                itemStack = createScepter(itemStack);
            }
        }

        return ActionResult.CONSUME.withNewHandStack(itemStack);
    }

    /**
     * Cast a specified spell for a specific living entity.
     *
     * @param spell Spell to cast.
     * @param caster Living entity to cast the spell for.
     * @param itemStack Item stack that the spell is cast with.
     * @param attackCast Truth assignment, if cast is an attack and not protect.
     * @param hand Hand being used to cast the spell.
     */
    public static void castSpell(@NotNull Spell spell, @NotNull LivingEntity caster,
                                @Nullable ItemStack itemStack,
                                boolean attackCast, Hand hand) {
        caster.playSound(attackCast ?
                ModSoundEvents.ITEM_MAGICAL_SCEPTER_CAST_ATTACK_SPELL :
                ModSoundEvents.ITEM_MAGICAL_SCEPTER_CAST_PROTECT_SPELL);

        SwingType swingType = attackCast ? SwingType.HIT : SwingType.PROTECT;
        ((SwingHandLivingEntity)caster).magical_scepter$swingHand(hand, swingType);

        if (caster.getWorld().isClient()) {
            return;
        }

        if (caster instanceof ServerPlayerEntity serverPlayerEntity && itemStack != null) {
            ModCriteria.CAST_SCEPTER.trigger(serverPlayerEntity, itemStack);
        }

        spell.castSpell(caster);
    }

    public ItemStack createScepter(ItemStack stack) {
        ItemStack scepterStack = ModItems.SCEPTER.getDefaultStack();
        scepterStack.applyChanges(stack.getComponentChanges());
        scepterStack.remove(ModDataComponentTypes.SCEPTER_CONTENTS);
        return scepterStack;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        ScepterContentsComponent.buildTooltip(tooltip::add, stack);
    }

    @Override
    public Text getName(ItemStack stack) {
        ScepterContentsComponent scepterContentsComponent = stack.get(ModDataComponentTypes.SCEPTER_CONTENTS);
        return scepterContentsComponent != null ?
                scepterContentsComponent.getName(this.translationKey + ".") : super.getName(stack);
    }
}

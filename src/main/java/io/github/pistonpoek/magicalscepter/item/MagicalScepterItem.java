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
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
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
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return use(world, user, hand, false);
    }

    @Override
    public TypedActionResult<ItemStack> attack(World world, PlayerEntity user) {
        return use(world, user, Hand.MAIN_HAND, true);
    }

    private TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand, boolean attackCast) {
        ItemStack itemStack = user.getStackInHand(hand);
        ScepterContentsComponent scepterContent =
                ScepterContentsComponent.get(itemStack).orElse(ScepterContentsComponent.DEFAULT);

        Optional<Spell> optionalSpell = (attackCast ?
                scepterContent.getAttackSpell() :
                scepterContent.getProtectSpell()).map(RegistryEntry::value);

        if (optionalSpell.isEmpty()) {
            return TypedActionResult.pass(itemStack);
        }

        Spell spell = optionalSpell.get();

        if (!user.getAbilities().creativeMode) {
            if (!scepterContent.hasEnoughExperience(user)) {
                return TypedActionResult.fail(itemStack);
            }

            int experienceCost = scepterContent.getExperienceCost();
            user.addExperience(-experienceCost);
            user.addScore(experienceCost); // Compensating for lost score in adding experience cost.
        }
        user.getItemCooldownManager().set(this, spell.getCooldown());

        user.incrementStat(Stats.USED.getOrCreateStat(this));

        int castDuration = MagicalScepterItem.castSpell(spell, user, itemStack, attackCast, hand);

        if (!world.isClient()) {
            // Correct spell duration cooldown, increase cooldown for non-creative and decrease for creative players.
            if (user.getAbilities().creativeMode ^ castDuration + 10 >= spell.getCooldown()) {
                user.getItemCooldownManager().set(this, castDuration + 10);
            }

            itemStack.damage(1, user, LivingEntity.getSlotForHand(hand));
            if (itemStack.isEmpty()) {
                itemStack = createScepter(itemStack);
            }
        }

        return TypedActionResult.success(itemStack, false);
    }

    /**
     * Cast a specified spell for a specific living entity.
     *
     * @param spell Spell to cast.
     * @param caster Living entity to cast the spell for.
     * @param itemStack Item stack that the spell is cast with.
     * @param attackCast Truth assignment, if cast is an attack and not protect.
     * @param hand Hand being used to cast the spell.
     * @return Duration that the spell takes.
     */
    public static int castSpell(@NotNull Spell spell, @NotNull LivingEntity caster,
                                @Nullable ItemStack itemStack,
                                boolean attackCast, Hand hand) {
        caster.playSound(attackCast ?
                ModSoundEvents.ITEM_MAGICAL_SCEPTER_CAST_ATTACK_SPELL :
                ModSoundEvents.ITEM_MAGICAL_SCEPTER_CAST_PROTECT_SPELL);

        SwingType swingType = attackCast ? SwingType.HIT : SwingType.PROTECT;
        ((SwingHandLivingEntity)caster).swingHand(hand, swingType);

        if (caster.getWorld().isClient()) {
            return 0;
        }

        if (caster instanceof ServerPlayerEntity serverPlayerEntity && itemStack != null) {
            ModCriteria.CAST_SCEPTER.trigger(serverPlayerEntity, itemStack);
        }

        return spell.castSpell(caster);
    }

    public ItemStack createScepter(ItemStack stack) {
        ItemStack scepterStack = ModItems.SCEPTER.getDefaultStack();
        scepterStack.applyChanges(stack.getComponentChanges());
        scepterStack.remove(ModDataComponentTypes.SCEPTER_CONTENTS);
        return scepterStack;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        ScepterContentsComponent.buildTooltip(tooltip::add, stack);
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

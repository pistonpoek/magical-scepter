package io.github.pistonpoek.magicalscepter.entity.ai.goal;

import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.entity.mob.SorcererEntity;
import io.github.pistonpoek.magicalscepter.item.MagicalScepterItem;
import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import io.github.pistonpoek.magicalscepter.util.LivingEntityHand;
import java.util.EnumSet;
import java.util.Optional;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;

/**
 * Entity attack goal for the use of a magical scepter,
 * for example used by {@link SorcererEntity}.
 *
 * @param <T> Entity type to use this goal for.
 */
public class ScepterAttackGoal<T extends Monster> extends Goal {
    private static final double ATTACKS_PER_PROTECT = Math.E;
    private final T actor;
    private final double speed;
    private final int castInterval;
    private final int protectInterval;
    private final float squaredRange;
    private int cooldown = -1;
    private int targetSeeingTicker;

    /**
     * Construct a scepter attack goal for a single entity.
     *
     * @param actor        Entity to construct the goal for.
     * @param speed        Navigation speed of the actor during the goal.
     * @param castInterval Interval to use between spell casts for this goal.
     * @param range        Range to attack targets at for this goal.
     */
    public ScepterAttackGoal(T actor, double speed, int castInterval, float range) {
        this.actor = actor;
        this.speed = speed;
        this.castInterval = castInterval;
        this.protectInterval = (int) ((ATTACKS_PER_PROTECT + 1) * this.castInterval);
        this.squaredRange = range * range;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return hasAliveTarget() && this.isHoldingScepterWithSpell();
    }

    /**
     * Check if the actor is holding a scepter with a spell.
     *
     * @return Truth assignment, if actor is holding a scepter with a spell.
     */
    protected boolean isHoldingScepterWithSpell() {
        return this.actor.isHolding(ScepterHelper.SCEPTER_WITH_SPELL);
    }

    /**
     * Check if the actor has an alive target.
     *
     * @return Truth assignment, if the actor has an alive target.
     */
    private boolean hasAliveTarget() {
        return this.actor.getTarget() != null && this.actor.getTarget().isAlive();
    }

    @Override
    public void start() {
        super.start();
        this.actor.setAggressive(true);
        if (this.actor.getSensing().hasLineOfSight(this.actor.getTarget())) {
            this.targetSeeingTicker = 15;
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.actor.setAggressive(false);
        this.actor.setTarget(null);
        this.targetSeeingTicker = 0;
    }

    @Override
    public boolean isInterruptable() {
        return this.cooldown >= 5 || this.targetSeeingTicker < 15;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = this.actor.getTarget();
        if (target == null) {
            return;
        }

        updateTargetSeeingTicker(target);
        updateNavigation(target);
        updateLookControl(target);

        // Check if it is time to cast.
        if (this.cooldown-- > 0 || this.targetSeeingTicker < 20) {
            return;
        }
        this.cooldown = this.castInterval;

        // Get the scepter stack and spells to prepare for casting.
        InteractionHand hand = LivingEntityHand.get(actor, ScepterHelper.SCEPTER_WITH_SPELL);
        ItemStack scepterStack = this.actor.getItemInHand(hand);
        Optional<Spell> attackSpell = ScepterContentsComponent.getAttackSpell(scepterStack);
        Optional<Spell> protectSpell = ScepterContentsComponent.getProtectSpell(scepterStack);

        // Determine the spell to cast
        Optional<Spell> scepterSpell;
        final boolean attack;
        if (useProtectSpell(target)) {
            attack = protectSpell.isEmpty();
            scepterSpell = protectSpell.or(() -> attackSpell);
        } else {
            attack = attackSpell.isPresent();
            scepterSpell = attackSpell.or(() -> protectSpell);
        }

        // Cast the spell and update the stack used by the actor.
        scepterSpell.ifPresent(spell -> {
            ItemStack usedScepterStack = MagicalScepterItem.castSpell(spell, this.actor,
                    scepterStack, attack, InteractionHand.MAIN_HAND);
            this.actor.setItemInHand(hand, usedScepterStack);
        });
    }

    /**
     * Determine if the actor should use the protect spell instead of the attack spell.
     *
     * @param target Current target of the actor.
     * @return Truth assignment, if the actor should use the protect spell.
     */
    private boolean useProtectSpell(LivingEntity target) {
        boolean targetNearby = actor.distanceToSqr(target) < squaredRange * 0.3F;
        boolean recentlyDamaged = this.actor.getLastDamageSource() != null;
        boolean inProtectInterval = this.actor.tickCount % this.protectInterval < this.castInterval;
        return (targetNearby || recentlyDamaged) && inProtectInterval;
    }

    /**
     * Update the target seeing ticker value using the current specified target.
     *
     * @param target Current target of the actor to update the value with.
     */
    private void updateTargetSeeingTicker(LivingEntity target) {
        boolean targetVisible = this.actor.getSensing().hasLineOfSight(target);
        boolean targetAware = this.targetSeeingTicker > 0;
        if (targetVisible != targetAware) {
            this.targetSeeingTicker = 0;
        }

        if (targetVisible) {
            this.targetSeeingTicker++;
        } else {
            this.targetSeeingTicker--;
        }
    }

    /**
     * Update the navigation using the current specified target.
     *
     * @param target Current target of the actor to update navigation with.
     */
    private void updateNavigation(LivingEntity target) {
        double squaredDistance = this.actor.distanceToSqr(target);
        if (squaredDistance <= this.squaredRange && this.targetSeeingTicker >= 5) {
            this.actor.getNavigation().stop();
        } else {
            this.actor.getNavigation().moveTo(target, this.speed);
        }
    }

    /**
     * Update the look control using the current specified target.
     *
     * @param target Current target of the actor to update look control with.
     */
    private void updateLookControl(LivingEntity target) {
        if (this.actor.getControlledVehicle() instanceof Mob mobEntity) {
            mobEntity.lookAt(target, 30.0F, 30.0F);
            mobEntity.getLookControl().setLookAt(target, 30.0F, 30.0F);
        }
        this.actor.lookAt(target, 30.0F, 30.0F);
        this.actor.getLookControl().setLookAt(target, 30.0F, 30.0F);
    }
}


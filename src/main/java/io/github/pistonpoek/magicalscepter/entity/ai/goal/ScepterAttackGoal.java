package io.github.pistonpoek.magicalscepter.entity.ai.goal;

import java.util.EnumSet;
import java.util.Optional;

import io.github.pistonpoek.magicalscepter.item.MagicalScepterItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import io.github.pistonpoek.magicalscepter.util.LivingEntityHand;

public class ScepterAttackGoal<T extends HostileEntity> extends Goal {
    private static final double ATTACKS_PER_PROTECT = Math.E;
    private final T actor;
    private final double speed;
    private final int attackInterval;
    private final float squaredRange;
    private int cooldown = -1;
    private int targetSeeingTicker;

    public ScepterAttackGoal(T actor, double speed, int attackInterval, float range) {
        this.actor = actor;
        this.speed = speed;
        this.attackInterval = attackInterval;
        this.squaredRange = range * range;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        return hasAliveTarget() && this.isHoldingScepterWithSpell();
    }

    protected boolean isHoldingScepterWithSpell() {
        return this.actor.isHolding(ScepterHelper.IS_SCEPTER_WITH_SPELL);
    }

    @Override
    public boolean shouldContinue() {
        return hasAliveTarget() && (this.canStart() || !this.actor.getNavigation().isIdle()) && this.isHoldingScepterWithSpell();
    }

    private boolean hasAliveTarget() {
        return this.actor.getTarget() != null && this.actor.getTarget().isAlive();
    }

    @Override
    public void start() {
        super.start();
        this.actor.setAttacking(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.actor.setAttacking(false);
        this.actor.setTarget(null);
        this.targetSeeingTicker = 0;
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = this.actor.getTarget();
        if (target != null) {
            double squaredDistance = this.actor.squaredDistanceTo(target.getX(), target.getY(), target.getZ());
            boolean targetVisible = this.actor.getVisibilityCache().canSee(target);
            boolean targetAware = this.targetSeeingTicker > 0;
            if (targetVisible != targetAware) {
                this.targetSeeingTicker = 0;
            }

            if (targetVisible) {
                this.targetSeeingTicker++;
            } else {
                this.targetSeeingTicker--;
            }

            if (squaredDistance <= this.squaredRange && this.targetSeeingTicker >= 5) {
                this.actor.getNavigation().stop();
            } else {
                this.actor.getNavigation().startMovingTo(target, this.speed);
            }

            if (this.actor.getControllingVehicle() instanceof MobEntity mobEntity) {
                mobEntity.lookAtEntity(target, 30.0F, 30.0F);
                mobEntity.getLookControl().lookAt(target, 30.0F, 30.0F);
            }
            this.actor.lookAtEntity(target, 30.0F, 30.0F);
            this.actor.getLookControl().lookAt(target, 30.0F, 30.0F);

            if (--this.cooldown < 0 && this.targetSeeingTicker >= 20) {
                Hand hand = LivingEntityHand.get(actor, ScepterHelper.IS_SCEPTER_WITH_SPELL);
                ItemStack scepterStack = this.actor.getStackInHand(hand);
                Optional<Spell> attackSpell = ScepterContentsComponent.getAttackSpell(scepterStack);
                Optional<Spell> protectSpell = ScepterContentsComponent.getProtectSpell(scepterStack);

                Optional<Spell> scepterSpell;
                final boolean attackCast;
                if ((this.actor.age - this.actor.getLastAttackedTime() <= 20 ||
                    squaredDistance < squaredRange * 0.3F) &&
                        this.actor.age % ((ATTACKS_PER_PROTECT + 1) * this.attackInterval) < this.attackInterval) {
                    attackCast = protectSpell.isEmpty();
                    scepterSpell = protectSpell.or(() -> attackSpell);
                } else {
                    attackCast = attackSpell.isPresent();
                    scepterSpell = attackSpell.or(() -> protectSpell);
                }

                this.cooldown = this.attackInterval;

                scepterSpell.ifPresent(spell -> {
                    ItemStack usedScepterStack = MagicalScepterItem.castSpell(spell, this.actor,
                            scepterStack, attackCast, Hand.MAIN_HAND);
                    this.actor.setStackInHand(hand, usedScepterStack);
                });
            }
        }
    }
}


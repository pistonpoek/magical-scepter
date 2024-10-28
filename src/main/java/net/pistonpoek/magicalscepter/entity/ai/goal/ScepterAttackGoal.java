package net.pistonpoek.magicalscepter.entity.ai.goal;

import java.util.EnumSet;
import java.util.Optional;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import net.pistonpoek.magicalscepter.scepter.ScepterHelper;
import net.pistonpoek.magicalscepter.spell.Spell;
import net.pistonpoek.magicalscepter.util.LivingEntityHand;

public class ScepterAttackGoal<T extends HostileEntity> extends Goal {
    private final T actor;
    private final double speed;
    private final int attackInterval;
    private final float squaredRange;
    private int cooldown = -1;
    private int targetSeeingTicker;
    private boolean movingToLeft;
    private boolean backward;
    private int combatTicks = -1;

    public ScepterAttackGoal(T actor, double speed, int attackInterval, float range) {
        this.actor = actor;
        this.speed = speed;
        this.attackInterval = attackInterval;
        this.squaredRange = range * range;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        return this.actor.getTarget() != null && this.isHoldingScepterWithSpell();
    }

    protected boolean isHoldingScepterWithSpell() {
        return this.actor.isHolding(ScepterHelper.IS_SCEPTER_WITH_SPELL);
    }

    @Override
    public boolean shouldContinue() {
        return (this.canStart() || !this.actor.getNavigation().isIdle()) && this.isHoldingScepterWithSpell();
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

            if (squaredDistance <= this.squaredRange && this.targetSeeingTicker >= 20) {
                this.actor.getNavigation().stop();
                this.combatTicks++;
            } else {
                this.actor.getNavigation().startMovingTo(target, this.speed);
                this.combatTicks = -1;
            }

            if (this.combatTicks >= 20) {
                if ((double)this.actor.getRandom().nextFloat() < 0.3) {
                    this.movingToLeft = !this.movingToLeft;
                }

                if ((double)this.actor.getRandom().nextFloat() < 0.3) {
                    this.backward = !this.backward;
                }

                this.combatTicks = 0;
            }

            if (this.combatTicks > -1) {
                if (squaredDistance > this.squaredRange * 0.75F) {
                    this.backward = false;
                } else if (squaredDistance < this.squaredRange * 0.25F) {
                    this.backward = true;
                }

                this.actor.getMoveControl().strafeTo(this.backward ? -0.5F : 0.5F, this.movingToLeft ? 0.5F : -0.5F);
                if (this.actor.getControllingVehicle() instanceof MobEntity mobEntity) {
                    mobEntity.lookAtEntity(target, 30.0F, 30.0F);
                }

                this.actor.lookAtEntity(target, 30.0F, 30.0F);
            } else {
                this.actor.getLookControl().lookAt(target, 30.0F, 30.0F);
            }

            if (--this.cooldown < 0 && this.targetSeeingTicker >= 20) {
                ItemStack scepterStack = this.actor.getStackInHand(
                        LivingEntityHand.get(actor, ScepterHelper.IS_SCEPTER_WITH_SPELL)
                );
                Optional<Spell> attackSpell = ScepterContentsComponent.getAttackSpell(scepterStack);
                Optional<Spell> protectSpell = ScepterContentsComponent.getProtectSpell(scepterStack);

                Optional<Spell> scepterSpell;
                if ((this.actor.age - this.actor.getLastAttackedTime() <= 20 ||
                    squaredDistance < squaredRange * 0.50F) && this.actor.getRandom().nextBoolean()) {
                    scepterSpell = protectSpell.or(() -> attackSpell);
                } else {
                    scepterSpell = attackSpell.or(() -> protectSpell);
                }

                this.cooldown = this.attackInterval;
                scepterSpell.ifPresent(spell -> cooldown += spell.castSpell(this.actor));
            }
        }
    }
}


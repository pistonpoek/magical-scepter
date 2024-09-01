package net.pistonpoek.magical_scepter.item.scepter.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.pistonpoek.magical_scepter.MagicalScepter;

public class BlazeScepterSpell
        extends ScepterSpell {
    private int numberOfFireballsLeft = 0;
    private int ticksUntilNextFireBall = 0;

    public BlazeScepterSpell(int experienceCost, int castCooldown) {
        super(experienceCost, castCooldown);
    }

    @Override
    public int getCastDuration() {
        return 40;
    }

    @Override
    public void castSpell(LivingEntity caster) {
        numberOfFireballsLeft = caster.getRandom().nextBetween(3, 5);
    }

    @Override
    public void displaySpell(LivingEntity caster, int remainingCastTicks) {

    }

    @Override
    public void updateSpell(LivingEntity caster, int remainingCastTicks) {
        if (ticksUntilNextFireBall > 0) {
            ticksUntilNextFireBall--;
            return;
        }

        shootSmallFireball(caster);
        numberOfFireballsLeft--;
        if (numberOfFireballsLeft <= 0) {
            caster.stopUsingItem();
            return;
        }
        ticksUntilNextFireBall = getFireballTickDelay(caster.getRandom());
    }

    @Override
    public void endSpell(LivingEntity caster, int remainingCastTicks) {

    }

    public int getFireballTickDelay(Random random) {
        return random.nextBetween(2,8);
    }

    private void shootSmallFireball(LivingEntity caster) {
        MagicalScepter.LOGGER.info("Shooting small fire ball from blaze spell " + numberOfFireballsLeft + " fireballs left.");
        double deviation = 0.2;
        Vec3d rotation = ScepterSpellUtil.getProjectileRotation(caster);
        SmallFireballEntity smallFireballEntity = new SmallFireballEntity(caster.getWorld(), caster, new Vec3d(caster.getRandom().nextTriangular(rotation.x, deviation), rotation.y, caster.getRandom().nextTriangular(rotation.z, deviation)));
        Vec3d position = ScepterSpellUtil.getProjectilePosition(caster);
        smallFireballEntity.setPosition(position.x, position.y, position.z);

        caster.getWorld().playSound(null, caster.getX(), caster.getY(), caster.getZ(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.NEUTRAL, 0.5f, 0.4f / (caster.getWorld().getRandom().nextFloat() * 0.4f + 0.8f));
        caster.getWorld().spawnEntity(smallFireballEntity);
    }
}

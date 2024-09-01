package net.pistonpoek.magical_scepter.item.scepter.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ShulkerBulletScepterSpell extends InstantScepterSpell {
    public ShulkerBulletScepterSpell(int experienceCost, int castCooldown) {
        super(experienceCost, castCooldown);
    }

    @Override
    public void castSpell(LivingEntity caster) {
        shootShulkerBullet(caster);
    }

    @Override
    public void displaySpell(LivingEntity caster, int remainingCastTicks) {

    }

    private void shootShulkerBullet(LivingEntity caster) {
        Vec3d rotation = ScepterSpellUtil.getProjectileRotation(caster);
        Direction.Axis axis = Direction.Axis.X;
        if (MathHelper.abs((float) rotation.getX()) < MathHelper.abs((float) rotation.getZ())) {
            axis = Direction.Axis.Z;
        }
        ShulkerBulletEntity scepterShulkerBulletEntity = new ShulkerBulletEntity(caster.getWorld(), caster, caster.getLastAttacker(), axis);
        Vec3d position = ScepterSpellUtil.getProjectilePosition(caster);
        scepterShulkerBulletEntity.setPosition(position.x, position.y, position.z);
        caster.getWorld().playSound(null, position.x, position.y, position.z, SoundEvents.ENTITY_SHULKER_SHOOT, SoundCategory.NEUTRAL, 0.6f, (caster.getRandom().nextFloat() - caster.getRandom().nextFloat()) * 0.2F + 1.0F); // Shulker bullet volume is 2.0f
        caster.getWorld().spawnEntity(scepterShulkerBulletEntity);
    }
}

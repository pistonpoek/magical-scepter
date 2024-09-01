package net.pistonpoek.magical_scepter.item.scepter.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class WitherSkullScepterSpell extends InstantScepterSpell {
    public WitherSkullScepterSpell(int experienceCost, int castCooldown) {
        super(experienceCost, castCooldown);
    }

    @Override
    public void castSpell(LivingEntity caster) {
        shootWitherSkull(caster);
    }

    @Override
    public void displaySpell(LivingEntity caster, int remainingCastTicks) {

    }
/*
    private void shootWitherSkull(LivingEntity caster) {
        double deviation = 0.2;
        Vec3d rotation = ScepterSpellUtil.getProjectileRotation(caster);
        WitherSkullEntity witherSkullEntity = new WitherSkullEntity(caster.getWorld(), caster,
                caster.getRandom().nextTriangular(rotation.x, deviation),
                rotation.y,
                caster.getRandom().nextTriangular(rotation.z, deviation));

        Vec3d position = ScepterSpellUtil.getProjectilePosition(caster);
        //WitherSkullEntity witherSkullEntity = new WitherSkullEntity(EntityType.WITHER_SKULL, caster.getWorld());
        witherSkullEntity.setPosition(position.x, position.y, position.z);
        // Unknown how to change the initial facing of the rendered wither skull.
        //witherSkullEntity.updateTrackedPositionAndAngles(position.x, position.y, position.z,
        //        caster.getYaw() + 180, caster.getPitch(), 0);
        Vec3d velocity = caster.getRotationVector();
        //witherSkullEntity.setVelocity(velocity);
        witherSkullEntity.prevYaw = 0.0f;
        witherSkullEntity.prevPitch = 0.0f;
        witherSkullEntity.setVelocityClient(velocity.x, velocity.y, velocity.z);
        witherSkullEntity.prevYaw = caster.getYaw() + 180;
        witherSkullEntity.prevPitch = caster.getPitch();
        //witherSkullEntity.setVelocityClient(velocity.x, velocity.y, velocity.z);
//        witherSkullEntity.refreshPositionAndAngles(position.x, position.y, position.z, caster.getYaw(), caster.getPitch());
        caster.getWorld().playSound(null, position.x, position.y, position.z, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.NEUTRAL, 0.5f, 0.4f / (caster.getWorld().getRandom().nextFloat() * 0.4f + 0.8f));
        caster.getWorld().spawnEntity(witherSkullEntity);
    }
    */

/*
    private void shootWitherSkull(LivingEntity caster) {
        double deviation = 0.2;
        Vec3d rotation = ScepterSpellUtil.getProjectileRotation(caster);
        Vec3d velocity = new Vec3d(
            caster.getRandom().nextTriangular(rotation.x, deviation),
            rotation.y,
            caster.getRandom().nextTriangular(rotation.z, deviation)
        );
        Vec3d position = ScepterSpellUtil.getProjectilePosition(caster);

        WitherSkullEntity witherSkullEntity = new WitherSkullEntity(EntityType.WITHER_SKULL, caster.getWorld());
        witherSkullEntity.setOwner(caster);
        witherSkullEntity.setVelocity(velocity);
        witherSkullEntity.setPosition(position);
//        double distance = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
//        witherSkullEntity.setPitch((float)(MathHelper.atan2(velocity.y, distance) * 57.2957763671875));
//        witherSkullEntity.setYaw((float)(MathHelper.atan2(velocity.x, velocity.z) * 57.2957763671875));
//        witherSkullEntity.prevPitch = witherSkullEntity.getPitch();
//        witherSkullEntity.prevYaw = witherSkullEntity.getYaw();
        caster.getWorld().playSound(null, position.x, position.y, position.z,
                SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.NEUTRAL,
                0.5f, 0.4f / (caster.getWorld().getRandom().nextFloat() * 0.4f + 0.8f));
        caster.getWorld().spawnEntity(witherSkullEntity);
        ProjectileUtil.setRotationFromVelocity(witherSkullEntity, 1.0f);
    }
    */

    private void shootWitherSkull(LivingEntity caster) {
        double deviation = 0.2;
        Vec3d rotation = ScepterSpellUtil.getProjectileRotation(caster);
        Vec3d velocity = new Vec3d(
                caster.getRandom().nextTriangular(rotation.x, deviation),
                rotation.y,
                caster.getRandom().nextTriangular(rotation.z, deviation)
        );
        Vec3d position = ScepterSpellUtil.getProjectilePosition(caster);

        WitherSkullEntity witherSkullEntity = new WitherSkullEntity(caster.getWorld(), caster, velocity);

        witherSkullEntity.prevYaw = caster.getYaw();
        witherSkullEntity.prevPitch = caster.getPitch();

        witherSkullEntity.setPosition(position);
        caster.getWorld().playSound(null, position.x, position.y, position.z,
                SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.NEUTRAL,
                0.5f, 0.4f / (caster.getWorld().getRandom().nextFloat() * 0.4f + 0.8f));
        caster.getWorld().spawnEntity(witherSkullEntity);

        witherSkullEntity.prevYaw = caster.getYaw();
        witherSkullEntity.prevPitch = caster.getPitch();
    }
}

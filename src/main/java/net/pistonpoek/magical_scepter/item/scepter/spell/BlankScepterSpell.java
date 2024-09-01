package net.pistonpoek.magical_scepter.item.scepter.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import net.pistonpoek.magical_scepter.MagicalScepter;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class BlankScepterSpell
        extends ScepterSpell {
    public BlankScepterSpell(int experienceCost, int castCooldown) {
        super(experienceCost, castCooldown);
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    @Override
    public int getCastDuration() {
        return 20;
    }

    @Override
    public void displaySpell(LivingEntity caster, int remainingCastTicks) {

    }

    @Override
    public void castSpell(LivingEntity caster) {
        MagicalScepter.LOGGER.info("Shooting firework whee!");
        ProjectileEntity projectileEntity;
        projectileEntity = new FireworkRocketEntity(caster.getWorld(), Items.FIREWORK_ROCKET.getDefaultStack(), caster, caster.getX(), caster.getEyeY() - (double)0.15f, caster.getZ(), true);
        caster.getRotationVector();
        Vec3d vec3d = caster.getOppositeRotationVector(1.0f);
        Quaternionf quaternionf = new Quaternionf().setAngleAxis(0.0f, vec3d.x, vec3d.y, vec3d.z);
        Vec3d vec3d2 = caster.getRotationVec(1.0f);
        Vector3f vector3f = vec3d2.toVector3f().rotate(quaternionf);
        Vec3d look = caster.getRotationVector().normalize();
        projectileEntity.setVelocity(look.x, look.y, look.z, 0.8f, 14 - caster.getWorld().getDifficulty().getId() * 4);
        caster.getWorld().spawnEntity(projectileEntity);
    }

    @Override
    public void updateSpell(LivingEntity caster, int remainingCastTicks) {
    }

    @Override
    public void endSpell(LivingEntity caster, int remainingCastTicks) {

    }

//    Get the position of projectile spawn point.
//    public static float launchHeight(LivingEntity livingEntity) {
//        var eyeHeight = livingEntity.getStandingEyeHeight();
//        var shoulderDistance = livingEntity.getHeight() * 0.15;
//        return (float) ((eyeHeight - shoulderDistance) * livingEntity.getScaleFactor());
//    }
//
//    public static Vec3d launchPoint(LivingEntity caster) {
//        return launchPoint(caster, launchPointOffsetDefault);
//    }
//
//    public static float launchPointOffsetDefault = 0.5F;
//
//    public static Vec3d launchPoint(LivingEntity caster, float forward) {
//        Vec3d look = caster.getRotationVector().multiply(forward * caster.getScaleFactor());
//        return caster.getPos().add(0, launchHeight(caster), 0).add(look);
//    }
//
//    var look = caster.getRotationVector().normalize();
//    projectile.setVelocity(look.x, look.y, look.z, velocity, divergence);
//
//    projectile.setPitch(caster.getPitch());
//    projectile.setYaw(caster.getYaw());
//
//    world.spawnEntity(projectile);


//    @Override
//    public void castPrimarySpell(World world, LivingEntity caster) {
//        ProjectileEntity projectileEntity;
//        projectileEntity = new FireworkRocketEntity(world, Items.FIREWORK_ROCKET.getDefaultStack(), caster, caster.getX(), caster.getEyeY() - (double)0.15f, caster.getZ(), true);
//
//        Vec3d vec3d = caster.getOppositeRotationVector(1.0f);
//        Quaternionf quaternionf = new Quaternionf().setAngleAxis(0.0f, vec3d.x, vec3d.y, vec3d.z);
//        Vec3d vec3d2 = caster.getRotationVec(1.0f);
//        Vector3f vector3f = vec3d2.toVector3f().rotate(quaternionf);
//        projectileEntity.setVelocity(vector3f.x(), vector3f.y(), vector3f.z(), 0.8f, 14 - caster.getWorld().getDifficulty().getId() * 4);
//        world.spawnEntity(projectileEntity);
//    }
//
//    @Override
//    public void castSecondarySpell(World world, LivingEntity caster) {
//        caster.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20 * 10, 0, false, true, true));
//    }



//        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));

//        if (!world.isClient) {
//            EnderPearlEntity enderPearlEntity = new EnderPearlEntity(world, user);
//            enderPearlEntity.setItem(itemStack);
//            enderPearlEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 1.0f);
//            world.spawnEntity(enderPearlEntity);
//        }
//        user.incrementStat(Stats.USED.getOrCreateStat(this));


//        user.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 20 * 10, 0, false, true, true));
//        if (!world.isClient) {
//            SnowballEntity snowballEntity = new SnowballEntity(world, user);
//            shootSkull(world, user);
//            //snowballEntity.setItem(itemStack);
//            snowballEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 1.0f);
//            //world.spawnEntity(snowballEntity);
//        }
//        user.giveItemStack(ScepterUtil.setScepter(new ItemStack(ModItems.SCEPTER), Scepters.BLAZE));
//        user.giveItemStack(ScepterUtil.setScepter(new ItemStack(ModItems.SCEPTER), Scepters.DRAGON));

    //{
//        Vec3d direction = caster.getRotationVector();
//        Vec3d position = new Vec3d(caster.getX(), caster.getEyeY() - (double)0.1f, caster.getZ());
//        position = position.add(direction.multiply(3));
//        DragonFireballEntity dragonFireballEntity = new DragonFireballEntity(world, caster, direction.x, direction.y, direction.z);
//        dragonFireballEntity.setOwner(caster);
//        dragonFireballEntity.setPosition(position.x, position.y, position.z);
//        world.spawnEntity(dragonFireballEntity);

//        ProjectileEntity projectileEntity;
//        projectileEntity = new FireworkRocketEntity(world, Items.FIREWORK_ROCKET.getDefaultStack(), caster, caster.getX(), caster.getEyeY() - (double)0.15f, caster.getZ(), true);
//
//        Vec3d vec3d = caster.getOppositeRotationVector(1.0f);
//        Quaternionf quaternionf = new Quaternionf().setAngleAxis(0.0f, vec3d.x, vec3d.y, vec3d.z);
//        Vec3d vec3d2 = caster.getRotationVec(1.0f);
//        Vector3f vector3f = vec3d2.toVector3f().rotate(quaternionf);
//        projectileEntity.setVelocity(vector3f.x(), vector3f.y(), vector3f.z(), 0.8f, 14 - caster.getWorld().getDifficulty().getId() * 4);
//        world.spawnEntity(projectileEntity);
//    }

//    private void shootSkull(World world, LivingEntity shooter) {
//        Vec3d direction = shooter.getRotationVector();
//        Vec3d position = new Vec3d(shooter.getX(), shooter.getEyeY() - (double)0.1f, shooter.getZ());
//        position = position.add(direction.multiply(3));
//
//        WitherSkullEntity witherSkullEntity = new WitherSkullEntity(world, shooter, direction.x, direction.y, direction.z);
//        witherSkullEntity.setOwner(shooter);
//        witherSkullEntity.setPosition(position);
//        //world.spawnEntity(witherSkullEntity);
//
//        DragonFireballEntity dragonFireballEntity = new DragonFireballEntity(world, shooter, direction.x, direction.y, direction.z);
//        dragonFireballEntity.setOwner(shooter);
//        dragonFireballEntity.setPosition(position.x, position.y, position.z);
//        world.spawnEntity(dragonFireballEntity);
//    }
}

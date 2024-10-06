package net.pistonpoek.magicalscepter.spell.effect.projectile;

import com.mojang.serialization.MapCodec;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.pistonpoek.magicalscepter.util.RotationVector;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ShulkerBulletSpellProjectile implements ShootProjectileSpellEffect {
    public static final MapCodec<ShulkerBulletSpellProjectile> CODEC = MapCodec.unit(ShulkerBulletSpellProjectile::new);

    @Override
    public void apply(ServerWorld world, LivingEntity entity, Vec3d position, float pitch, float yaw) {
        Vec3d rot = RotationVector.get(pitch, yaw);
        Direction.Axis axis = Direction.Axis.X;
        if (MathHelper.abs((float) rot.getX()) < MathHelper.abs((float) rot.getZ())) {
            axis = Direction.Axis.Z;
        }
        ShulkerBulletEntity projectile = new ShulkerBulletEntity(world, entity,
                getTarget(entity), axis);
        projectile.setPosition(position.getX(), position.getY(), position.getZ());
        world.spawnEntity(projectile);
    }

    // TODO make target search size an optional variable
    private LivingEntity getTarget(Entity caster) {
        LivingEntity target = null;
        if (caster instanceof LivingEntity) {
            target = ((LivingEntity) caster).getLastAttacker();
        }
        if (target == null) {
            List<LivingEntity> targets = caster.getEntityWorld().getOtherEntities(caster,
                    Box.of(caster.getEyePos(), 64, 64, 64),
                    entity -> entity instanceof LivingEntity).stream()
                    .map(entity -> (LivingEntity)entity).collect(Collectors.toList());

            if (caster instanceof LivingEntity) {
                return caster.getEntityWorld().getClosestEntity(targets, TargetPredicate.createAttackable(),
                        (LivingEntity) caster, caster.getX(), caster.getY(), caster.getZ());
            }
            return targets.get(caster.getEntityWorld().random.nextInt(targets.size()));
        }

        return target;
    }

    @Override
    public MapCodec<ShulkerBulletSpellProjectile> getProjectileCodec() {
        return CODEC;
    }
}

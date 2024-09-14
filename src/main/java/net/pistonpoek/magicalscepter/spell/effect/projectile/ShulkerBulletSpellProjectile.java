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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ShulkerBulletSpellProjectile implements ShootProjectileSpellEffect {
    public static final MapCodec<ShulkerBulletSpellProjectile> CODEC = MapCodec.unit(ShulkerBulletSpellProjectile::new);

    @Override
    public void apply(ServerWorld world, Entity caster, Vec3d pos) {
        Optional<ShulkerBulletEntity> projectile = Optional.empty();
        if (caster instanceof LivingEntity) {
            Vec3d rotation = caster.getRotationVector().normalize();
            Direction.Axis axis = Direction.Axis.X;
            if (MathHelper.abs((float) rotation.getX()) < MathHelper.abs((float) rotation.getZ())) {
                axis = Direction.Axis.Z;
            }
            projectile = Optional.of(new ShulkerBulletEntity(caster.getWorld(), (LivingEntity) caster,
                    getTarget(caster), axis));
            Vec3d position = SpellProjectileHelper.getProjectilePosition(caster);
            projectile.get().setPosition(position.x, position.y, position.z);
        }

        projectile.ifPresent(shulkerBulletEntity -> caster.getWorld().spawnEntity(shulkerBulletEntity));
    }

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

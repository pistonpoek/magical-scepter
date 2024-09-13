package net.pistonpoek.magicalscepter.spell.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.entity.projectile.WindChargeEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class WindChargeSpellProjectile implements SpellProjectile<WindChargeEntity> {
    @Override
    public Optional<WindChargeEntity> shoot(ServerWorld world, Entity caster, Vec3d pos) {
        Optional<WindChargeEntity> projectile = Optional.empty();
        double deviation = 0.2;
        if (caster instanceof PlayerEntity) {
            projectile = Optional.of(new WindChargeEntity((PlayerEntity) caster,
                    world, caster.getPos().getX(), caster.getEyePos().getY(), caster.getPos().getZ()));
            projectile.get().setVelocity(caster, caster.getPitch(), caster.getYaw(), 0.0F, 1.5F, 1.0F);
        } else if (caster != null) {
            Vec3d rotation = caster.getRotationVector().normalize();
            projectile = Optional.of(new WindChargeEntity(world, caster.getX(), caster.getEyePos().getY(), caster.getZ(),
                    new Vec3d(caster.getRandom().nextTriangular(rotation.x, deviation),
                            rotation.y,
                            caster.getRandom().nextTriangular(rotation.z, deviation))));
        }

        if (projectile.isPresent()) {
            return caster.getWorld().spawnEntity(projectile.get()) ? projectile : Optional.empty();
        }
        return projectile;
    }
}

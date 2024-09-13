package net.pistonpoek.magicalscepter.spell.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class WitherSkullSpellProjectile implements SpellProjectile<WitherSkullEntity> {
    @Override
    public Optional<WitherSkullEntity> shoot(ServerWorld world, Entity caster, Vec3d pos) {
        Optional<WitherSkullEntity> projectile = Optional.empty();
        double deviation = 0.2;
        if (caster instanceof LivingEntity) {
            Vec3d rotation = caster.getRotationVector().normalize();
            projectile = Optional.of(new WitherSkullEntity(world, (LivingEntity) caster,
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

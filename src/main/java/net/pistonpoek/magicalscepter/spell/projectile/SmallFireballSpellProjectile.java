package net.pistonpoek.magicalscepter.spell.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class SmallFireballSpellProjectile implements SpellProjectile<SmallFireballEntity> {
    @Override
    public Optional<SmallFireballEntity> shoot(ServerWorld world, Entity caster, Vec3d pos) {
        Optional<SmallFireballEntity> projectile = Optional.empty();
        double deviation = 0.2;
        if (caster instanceof LivingEntity) {
            Vec3d rotation = caster.getRotationVector().normalize();
            projectile = Optional.of(new SmallFireballEntity(world, (LivingEntity) caster,
                    new Vec3d(caster.getRandom().nextTriangular(rotation.x, deviation),
                            rotation.y,
                            caster.getRandom().nextTriangular(rotation.z, deviation))));
        } else if (caster != null) {
            Vec3d rotation = caster.getRotationVector().normalize();
            projectile = Optional.of(new SmallFireballEntity(world, caster.getX(), caster.getY(), caster.getZ(),
                    new Vec3d(caster.getRandom().nextTriangular(rotation.x, deviation),
                            rotation.y,
                            caster.getRandom().nextTriangular(rotation.z, deviation))));
        }

        if (pos != null) {
            projectile.ifPresent(optional -> optional.refreshPositionAfterTeleport(pos));
        }

        if (projectile.isPresent()) {
            return caster.getWorld().spawnEntity(projectile.get()) ? projectile : Optional.empty();
        }
        return projectile;
    }
}

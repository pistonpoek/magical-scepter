package net.pistonpoek.magicalscepter.spell.effect.projectile;

import com.mojang.serialization.MapCodec;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WindChargeEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.pistonpoek.magicalscepter.util.RotationVector;

import java.util.Optional;

public class WindChargeSpellProjectile implements ShootProjectileSpellEffect {
    public static final MapCodec<WindChargeSpellProjectile> CODEC = MapCodec.unit(WindChargeSpellProjectile::new);

    @Override
    public void apply(ServerWorld world, LivingEntity entity, Vec3d position, float pitch, float yaw) {
        WindChargeEntity projectile;
        double deviation = 0.2;
        Vec3d rot = RotationVector.get(pitch, yaw).normalize();
        Vec3d velocity = new Vec3d(entity.getRandom().nextTriangular(rot.getX(), deviation),
                rot.getY(),
                entity.getRandom().nextTriangular(rot.getZ(), deviation));
        if (entity instanceof PlayerEntity) {
            projectile = new WindChargeEntity((PlayerEntity) entity,
                    world, position.getX(), position.getY(), position.getZ());
            projectile.setVelocity(velocity.getX(), velocity.getY(), velocity.getZ(), 1.5F, 1.0F);
        } else {
            projectile = new WindChargeEntity(world, position.getX(), position.getY(), position.getZ(),
                    velocity);
        }

        projectile.setPosition(position.getX(), position.getY(), position.getZ());
        world.spawnEntity(projectile);
    }

    @Override
    public MapCodec<WindChargeSpellProjectile> getProjectileCodec() {
        return CODEC;
    }
}

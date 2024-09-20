package net.pistonpoek.magicalscepter.spell.effect.projectile;

import com.mojang.serialization.MapCodec;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WindChargeEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class WindChargeSpellProjectile implements ShootProjectileSpellEffect {
    public static final MapCodec<WindChargeSpellProjectile> CODEC = MapCodec.unit(WindChargeSpellProjectile::new);

    @Override
    public void apply(ServerWorld world, Entity entity, Vec3d position, Vec3d rotation) {
        Optional<WindChargeEntity> projectile;
        double deviation = 0.2;
        Vec3d rot = rotation.normalize();
        Vec3d velocity = new Vec3d(entity.getRandom().nextTriangular(rot.getX(), deviation),
                rot.getY(),
                entity.getRandom().nextTriangular(rot.getZ(), deviation));
        if (entity instanceof PlayerEntity) {
            projectile = Optional.of(new WindChargeEntity((PlayerEntity) entity,
                    world, position.getX(), position.getY(), position.getZ()));
            projectile.get().setVelocity(velocity.getX(), velocity.getY(), velocity.getZ(), 1.5F, 1.0F);
        } else {
            projectile = Optional.of(new WindChargeEntity(world, position.getX(), position.getY(), position.getZ(),
                    velocity));
        }

        projectile.get().setPosition(position.getX(), position.getY(), position.getZ());
        world.spawnEntity(projectile.get());
    }

    @Override
    public MapCodec<WindChargeSpellProjectile> getProjectileCodec() {
        return CODEC;
    }
}

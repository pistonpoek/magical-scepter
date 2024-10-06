package net.pistonpoek.magicalscepter.spell.effect.projectile;

import com.mojang.serialization.MapCodec;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.pistonpoek.magicalscepter.util.RotationVector;

import java.util.Optional;

public class WitherSkullSpellProjectile implements ShootProjectileSpellEffect {
    public static final MapCodec<WitherSkullSpellProjectile> CODEC = MapCodec.unit(WitherSkullSpellProjectile::new);

    @Override
    public void apply(ServerWorld world, LivingEntity entity, Vec3d position, float pitch, float yaw) {
        double deviation = 0.2;
        Vec3d rot = RotationVector.get(pitch, yaw).normalize();
        Vec3d velocity = new Vec3d(entity.getRandom().nextTriangular(rot.getX(), deviation),
                rot.getY(),
                entity.getRandom().nextTriangular(rot.getZ(), deviation));
        Optional<WitherSkullEntity> projectile = Optional.of(new WitherSkullEntity(world, entity, velocity));

        projectile.get().setPosition(position.getX(), position.getY(), position.getZ());
        world.spawnEntity(projectile.get());
    }

    @Override
    public MapCodec<WitherSkullSpellProjectile> getProjectileCodec() {
        return CODEC;
    }
}

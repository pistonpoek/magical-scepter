package net.pistonpoek.magicalscepter.spell.effect.projectile;

import com.mojang.serialization.MapCodec;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.pistonpoek.magicalscepter.util.RotationVector;

import java.util.Optional;

public class SmallFireballSpellProjectile implements ShootProjectileSpellEffect {
    public static final MapCodec<SmallFireballSpellProjectile> CODEC = MapCodec.unit(SmallFireballSpellProjectile::new);

    @Override
    public void apply(ServerWorld world, LivingEntity entity, Vec3d position, float pitch, float yaw) {
        double deviation = 0.2;
        Vec3d rot = RotationVector.get(pitch, yaw).normalize();
        Vec3d velocity = new Vec3d(entity.getRandom().nextTriangular(rot.getX(), deviation),
                rot.getY(),
                entity.getRandom().nextTriangular(rot.getZ(), deviation));

        SmallFireballEntity projectile = new SmallFireballEntity(world, entity, velocity);

        projectile.setPosition(position.getX(), position.getY(), position.getZ());
        entity.getWorld().spawnEntity(projectile);
    }

    @Override
    public MapCodec<SmallFireballSpellProjectile> getProjectileCodec() {
        return CODEC;
    }
}

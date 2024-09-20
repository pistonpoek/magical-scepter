package net.pistonpoek.magicalscepter.spell.effect.projectile;

import com.mojang.serialization.MapCodec;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class SmallFireballSpellProjectile implements ShootProjectileSpellEffect {
    public static final MapCodec<SmallFireballSpellProjectile> CODEC = MapCodec.unit(SmallFireballSpellProjectile::new);

    @Override
    public void apply(ServerWorld world, Entity entity, Vec3d position, Vec3d rotation) {
        Optional<SmallFireballEntity> projectile;
        double deviation = 0.2;
        Vec3d rot = rotation.normalize();
        Vec3d velocity = new Vec3d(entity.getRandom().nextTriangular(rot.getX(), deviation),
                rot.getY(),
                entity.getRandom().nextTriangular(rot.getZ(), deviation));
        if (entity instanceof LivingEntity) {
            projectile = Optional.of(new SmallFireballEntity(world, (LivingEntity) entity, velocity));
        } else {
            projectile = Optional.of(new SmallFireballEntity(world, 
                    position.getX(), position.getY(), position.getZ(), velocity));
        }

        if (projectile.isEmpty()) {
            return;
        }

        projectile.get().setPosition(position.getX(), position.getY(), position.getZ());
        entity.getWorld().spawnEntity(projectile.get());
    }

    @Override
    public MapCodec<SmallFireballSpellProjectile> getProjectileCodec() {
        return CODEC;
    }
}

package net.pistonpoek.magicalscepter.spell.effect.projectile;

import com.mojang.serialization.MapCodec;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class FireballSpellProjectile implements ShootProjectileSpellEffect {
    public static final MapCodec<FireballSpellProjectile> CODEC = MapCodec.unit(FireballSpellProjectile::new);

    @Override
    public void apply(ServerWorld world, Entity entity, Vec3d position, Vec3d rotation) {
        Optional<FireballEntity> projectile = Optional.empty();
        double deviation = 0.2;
        if (entity instanceof LivingEntity) {
            Vec3d rot = rotation.normalize();
            projectile = Optional.of(new FireballEntity(world, (LivingEntity) entity,
                    new Vec3d(entity.getRandom().nextTriangular(rot.getX(), deviation),
                            rot.getY(),
                            entity.getRandom().nextTriangular(rot.getZ(), deviation)), 1));
        }

        if (projectile.isEmpty()) {
            return;
        }

        projectile.get().setPosition(position.getX(), position.getY(), position.getZ());
        world.spawnEntity(projectile.get());
    }

    @Override
    public MapCodec<FireballSpellProjectile> getProjectileCodec() {
        return CODEC;
    }
}

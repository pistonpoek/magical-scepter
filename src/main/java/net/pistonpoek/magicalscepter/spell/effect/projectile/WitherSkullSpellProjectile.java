package net.pistonpoek.magicalscepter.spell.effect.projectile;

import com.mojang.serialization.MapCodec;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.RotationCalculator;
import net.minecraft.util.math.RotationPropertyHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class WitherSkullSpellProjectile implements ShootProjectileSpellEffect {
    public static final MapCodec<WitherSkullSpellProjectile> CODEC = MapCodec.unit(WitherSkullSpellProjectile::new);

    @Override
    public void apply(ServerWorld world, Entity entity, Vec3d position, Vec3d rotation) {
        Optional<WitherSkullEntity> projectile = Optional.empty();
        double deviation = 0.2;
        Vec3d rot = rotation.normalize();
        Vec3d velocity = new Vec3d(entity.getRandom().nextTriangular(rot.getX(), deviation),
                rot.getY(),
                entity.getRandom().nextTriangular(rot.getZ(), deviation));
        if (entity instanceof LivingEntity) {
            projectile = Optional.of(new WitherSkullEntity(world, (LivingEntity) entity, velocity));
        }

        if (projectile.isEmpty()) {
            return;
        }

        projectile.get().setPosition(position.getX(), position.getY(), position.getZ());
        world.spawnEntity(projectile.get());
    }

    @Override
    public MapCodec<WitherSkullSpellProjectile> getProjectileCodec() {
        return CODEC;
    }
}

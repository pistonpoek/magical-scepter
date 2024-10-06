package net.pistonpoek.magicalscepter.spell.effect.projectile;

import com.mojang.serialization.MapCodec;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.pistonpoek.magicalscepter.util.RotationVector;

import java.util.Optional;

public class FireballSpellProjectile implements ShootProjectileSpellEffect {
    public static final MapCodec<FireballSpellProjectile> CODEC = MapCodec.unit(FireballSpellProjectile::new);

    @Override
    public void apply(ServerWorld world, LivingEntity entity, Vec3d position, float pitch, float yaw) {
        double deviation = 0.2;
        Vec3d rot = RotationVector.get(pitch, yaw).normalize();
        FireballEntity projectile = new FireballEntity(world, entity,
                new Vec3d(entity.getRandom().nextTriangular(rot.getX(), deviation),
                        rot.getY(),
                        entity.getRandom().nextTriangular(rot.getZ(), deviation)), 1);

        projectile.setPosition(position.getX(), position.getY(), position.getZ());
        world.spawnEntity(projectile);
    }

    @Override
    public MapCodec<FireballSpellProjectile> getProjectileCodec() {
        return CODEC;
    }
}

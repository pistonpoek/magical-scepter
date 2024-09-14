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
    public void apply(ServerWorld world, Entity caster, Vec3d pos) {
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
            projectile = Optional.of(new SmallFireballEntity(world, caster.getX(), caster.getEyePos().getY(), caster.getZ(),
                    new Vec3d(caster.getRandom().nextTriangular(rotation.x, deviation),
                            rotation.y,
                            caster.getRandom().nextTriangular(rotation.z, deviation))));
        }

        if (projectile.isEmpty()) {
            return;
        }

        Vec3d position = SpellProjectileHelper.getProjectilePosition(caster);
        projectile.get().setPosition(position.x, position.y, position.z);
        caster.getWorld().spawnEntity(projectile.get());
    }

    @Override
    public MapCodec<SmallFireballSpellProjectile> getProjectileCodec() {
        return CODEC;
    }
}

package net.pistonpoek.magicalscepter.spell.effect.projectile;

import com.mojang.serialization.MapCodec;
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
    public void apply(ServerWorld world, Entity caster, Vec3d pos) {
        Optional<WitherSkullEntity> projectile = Optional.empty();
        double deviation = 0.2;
        if (caster instanceof LivingEntity) {
            Vec3d rotation = caster.getRotationVector().normalize();
            projectile = Optional.of(new WitherSkullEntity(world, (LivingEntity) caster,
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
    public MapCodec<WitherSkullSpellProjectile> getProjectileCodec() {
        return CODEC;
    }
}

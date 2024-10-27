package net.pistonpoek.magicalscepter.spell.effect.projectile;

import com.mojang.serialization.MapCodec;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.pistonpoek.magicalscepter.spell.cast.context.SpellContext;

public class WitherSkullSpellProjectile implements ShootProjectileSpellEffect {
    public static final MapCodec<WitherSkullSpellProjectile> CODEC = MapCodec.unit(WitherSkullSpellProjectile::new);

    @Override
    public void apply(SpellContext context) {
        double deviation = 0.2;
        Vec3d rotation = context.getRotationVector().normalize();
        Vec3d position = context.position();
        Random random = context.getRandom();
        LivingEntity caster = context.caster();
        ServerWorld world = context.getWorld();
        Vec3d velocity = new Vec3d(
                random.nextTriangular(rotation.getX(), deviation),
                rotation.getY(),
                random.nextTriangular(rotation.getZ(), deviation)
        );
        WitherSkullEntity projectile = new WitherSkullEntity(world, caster, velocity);

        projectile.setPosition(position.getX(), position.getY(), position.getZ());
        world.spawnEntity(projectile);
    }

    @Override
    public MapCodec<WitherSkullSpellProjectile> getProjectileCodec() {
        return CODEC;
    }
}

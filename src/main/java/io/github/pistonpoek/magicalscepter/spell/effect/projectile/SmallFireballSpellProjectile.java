package io.github.pistonpoek.magicalscepter.spell.effect.projectile;

import com.mojang.serialization.MapCodec;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;

public class SmallFireballSpellProjectile implements ShootProjectileSpellEffect {
    public static final MapCodec<SmallFireballSpellProjectile> CODEC = MapCodec.unit(SmallFireballSpellProjectile::new);

    @Override
    public void apply(SpellContext context) {
        double deviation = 0.2;
        Vec3d rotation = context.getRotationVector().normalize();
        Random random = context.getRandom();
        LivingEntity caster = context.caster();
        Vec3d position = context.position();
        ServerWorld world = context.getWorld();

        Vec3d velocity = new Vec3d(
                random.nextTriangular(rotation.getX(), deviation),
                rotation.getY(),
                random.nextTriangular(rotation.getZ(), deviation)
        );

        SmallFireballEntity projectile = new SmallFireballEntity(world, caster, velocity);

        projectile.setPosition(position.getX(), position.getY(), position.getZ());
        world.spawnEntity(projectile);
    }

    @Override
    public MapCodec<SmallFireballSpellProjectile> getProjectileCodec() {
        return CODEC;
    }
}

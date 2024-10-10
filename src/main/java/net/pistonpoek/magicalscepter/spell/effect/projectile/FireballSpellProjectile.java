package net.pistonpoek.magicalscepter.spell.effect.projectile;

import com.mojang.serialization.MapCodec;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.pistonpoek.magicalscepter.spell.cast.SpellContext;

public class FireballSpellProjectile implements ShootProjectileSpellEffect {
    public static final MapCodec<FireballSpellProjectile> CODEC = MapCodec.unit(FireballSpellProjectile::new);

    @Override
    public void apply(SpellContext context) {
        double deviation = 0.2;
        Vec3d rotation = context.getRotationVector().normalize();
        Random random = context.getRandom();
        ServerWorld world = context.getWorld();
        LivingEntity caster = context.caster();
        Vec3d position = context.position();

        FireballEntity projectile = new FireballEntity(world, caster,
                new Vec3d(random.nextTriangular(rotation.getX(), deviation),
                        rotation.getY(),
                        random.nextTriangular(rotation.getZ(), deviation)), 1);

        projectile.setPosition(position.getX(), position.getY(), position.getZ());
        world.spawnEntity(projectile);
    }

    @Override
    public MapCodec<FireballSpellProjectile> getProjectileCodec() {
        return CODEC;
    }
}

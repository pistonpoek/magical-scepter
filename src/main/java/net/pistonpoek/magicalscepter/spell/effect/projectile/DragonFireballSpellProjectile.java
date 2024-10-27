package net.pistonpoek.magicalscepter.spell.effect.projectile;

import com.mojang.serialization.MapCodec;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.pistonpoek.magicalscepter.spell.cast.context.SpellContext;

public record DragonFireballSpellProjectile() implements ShootProjectileSpellEffect {
    public static final MapCodec<DragonFireballSpellProjectile> CODEC = MapCodec.unit(DragonFireballSpellProjectile::new);

    @Override
    public void apply(SpellContext context) {
        double deviation = 0.2;
        Vec3d rotation = context.getRotationVector();
        ServerWorld world = context.getWorld();
        Random random = context.getRandom();
        LivingEntity caster = context.caster();
        Vec3d position = context.position();

        DragonFireballEntity projectile = new DragonFireballEntity(world, caster,
                new Vec3d(random.nextTriangular(rotation.getX(), deviation),
                        rotation.getY(),
                        random.nextTriangular(rotation.getZ(), deviation)));
        projectile.setPosition(position.getX(), position.getY(), position.getZ());
        world.spawnEntity(projectile);
    }

    @Override
    public MapCodec<DragonFireballSpellProjectile> getProjectileCodec() {
        return CODEC;
    }
}

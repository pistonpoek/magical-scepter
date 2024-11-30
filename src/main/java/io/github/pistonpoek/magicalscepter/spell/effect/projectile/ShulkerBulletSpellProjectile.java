package io.github.pistonpoek.magicalscepter.spell.effect.projectile;

import com.mojang.serialization.MapCodec;
import io.github.pistonpoek.magicalscepter.spell.rotation.RotationSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;

public class ShulkerBulletSpellProjectile implements ShootProjectileSpellEffect {
    public static final MapCodec<ShulkerBulletSpellProjectile> CODEC = MapCodec.unit(ShulkerBulletSpellProjectile::new);

    @Override
    public void apply(SpellContext context) {
        ServerWorld world = context.getWorld();
        Vec3d position = context.position();
        LivingEntity caster = context.caster();
        Entity target = context.target();

        Direction.Axis axis = RotationSource.getDirection(context).getAxis();

        ShulkerBulletEntity projectile = new ShulkerBulletEntity(world, caster,
                getTarget(caster, target), axis);
        projectile.setPosition(position.getX(), position.getY(), position.getZ());
        world.spawnEntity(projectile);
    }

    private Entity getTarget(LivingEntity caster, Entity target) {
        if (caster.equals(target)) {
            target = caster.getLastAttacker();
        }
        if (target == null || !EntityPredicates.VALID_ENTITY.test(target)) {
            return null; // TODO figure out way to get proper target for shulker.
        }

        return target;
    }

    @Override
    public MapCodec<ShulkerBulletSpellProjectile> getProjectileCodec() {
        return CODEC;
    }
}

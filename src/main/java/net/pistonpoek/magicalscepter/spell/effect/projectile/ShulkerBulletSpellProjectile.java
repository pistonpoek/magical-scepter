package net.pistonpoek.magicalscepter.spell.effect.projectile;

import com.mojang.serialization.MapCodec;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.pistonpoek.magicalscepter.spell.cast.context.SpellContext;

import java.util.List;
import java.util.stream.Collectors;

public class ShulkerBulletSpellProjectile implements ShootProjectileSpellEffect {
    public static final MapCodec<ShulkerBulletSpellProjectile> CODEC = MapCodec.unit(ShulkerBulletSpellProjectile::new);

    @Override
    public void apply(SpellContext context) {
        Vec3d rotation = context.getRotationVector();
        ServerWorld world = context.getWorld();
        Vec3d position = context.position();
        LivingEntity caster = context.caster();
        Entity target = context.target();

        Direction.Axis axis = Direction.Axis.X;
        if (MathHelper.abs((float) rotation.getX()) < MathHelper.abs((float) rotation.getZ())) {
            axis = Direction.Axis.Z;
        }

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
            List<LivingEntity> targets = caster.getEntityWorld().getOtherEntities(caster,
                    Box.of(caster.getEyePos(), 64, 64, 64),
                    entity -> entity instanceof LivingEntity).stream()
                    .map(entity -> (LivingEntity)entity).collect(Collectors.toList());

            return caster.getEntityWorld().getClosestEntity(targets, TargetPredicate.createAttackable(),
                    caster, caster.getX(), caster.getY(), caster.getZ());
        }

        return target;
    }

    @Override
    public MapCodec<ShulkerBulletSpellProjectile> getProjectileCodec() {
        return CODEC;
    }
}

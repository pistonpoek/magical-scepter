package net.pistonpoek.magical_scepter.item.scepter.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.util.math.Vec3d;

public class DragonFireballSpell
    extends InstantScepterSpell {

    public DragonFireballSpell(int experienceCost, int castCooldown) {
        super(experienceCost, castCooldown);
    }

    @Override
    public void castSpell(LivingEntity caster) {
        Vec3d velocity = caster.getRotationVector();
        Vec3d position = new Vec3d(caster.getX(), caster.getEyeY() - (double)0.1f, caster.getZ());
        position = position.add(velocity.multiply(3));
        DragonFireballEntity dragonFireballEntity = new DragonFireballEntity(caster.getWorld(), caster, velocity);
        dragonFireballEntity.setOwner(caster);
        dragonFireballEntity.setPosition(position.x, position.y, position.z);
        caster.getWorld().spawnEntity(dragonFireballEntity);
    }

    @Override
    public void displaySpell(LivingEntity caster, int remainingCastTicks) {

    }
}

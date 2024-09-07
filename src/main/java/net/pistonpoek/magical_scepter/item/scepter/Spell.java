package net.pistonpoek.magical_scepter.item.scepter;

import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.pistonpoek.magical_scepter.item.scepter.spell.ScepterSpellUtil;
import net.pistonpoek.magical_scepter.item.scepter.spell.projectile.ScepterFireballEntity;

public record Spell(int experienceCost, int cooldown) {

    public boolean isInstant() {
        return true;
    }

    public int getCastDuration() {
        return 0;
    }

    public void castSpell(LivingEntity caster) {
        shootFireball(caster);
    }

    public void displaySpell(LivingEntity caster, int remainingCastTicks) {
        ScepterSpellUtil.addScepterCastParticles(caster, 0xCD5CAB, ScepterSpellUtil.getProjectilePosition(caster));
        //caster.getWorld().addParticle(ParticleTypes.EFFECT);
    }

    private void shootFireball(LivingEntity caster) {
        double deviation = 0.2;
        Vec3d rotation = ScepterSpellUtil.getProjectileRotation(caster);
        ScepterFireballEntity scepterFireballEntity = new ScepterFireballEntity(caster.getWorld(), caster, new Vec3d(caster.getRandom().nextTriangular(rotation.x, deviation), rotation.y, caster.getRandom().nextTriangular(rotation.z, deviation)));
        Vec3d position = ScepterSpellUtil.getProjectilePosition(caster);
        scepterFireballEntity.setPosition(position.x, position.y, position.z);
        caster.getWorld().playSound(null, position.x, position.y, position.z, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.NEUTRAL, 0.5f, 0.4f / (caster.getWorld().getRandom().nextFloat() * 0.4f + 0.8f));
        caster.getWorld().spawnEntity(scepterFireballEntity);
    }

    public void updateSpell(LivingEntity caster, int remainingCastTicks) {

    }

    public void endSpell(LivingEntity caster, int remainingCastTicks) {

    }
}

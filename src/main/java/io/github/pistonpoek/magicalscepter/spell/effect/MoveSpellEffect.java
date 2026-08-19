package io.github.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public record MoveSpellEffect(FloatProvider power, boolean knockback) implements SpellEffect {
    public static final MapCodec<MoveSpellEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    FloatProviders.CODEC.fieldOf("power").forGetter(MoveSpellEffect::power),
                    Codec.BOOL.optionalFieldOf("knockback", false).forGetter(MoveSpellEffect::knockback)
            ).apply(instance, MoveSpellEffect::new)
    );

    @Override
    public void apply(SpellContext context) {
        RandomSource random = context.getRandom();
        Vec3 rotation = context.getRotationVector().normalize();
        Optional<LivingEntity> target = context.getLivingTarget();

        if (knockback && target.isPresent()) {
            target.get().knockback(power.sample(random), -rotation.x(), -rotation.z(),
                    context.caster().createDamageSource(), 0);
        } else {
            Entity entityTarget = context.target();
            Vec3 force = rotation.scale(power.sample(random));
            entityTarget.addDeltaMovement(force);
            entityTarget.hurtMarked = true;
            entityTarget.needsSync = true;
            if (entityTarget instanceof Player playerEntity) {
                playerEntity.applyPostImpulseGraceTime(10);
            }
        }
    }

    @Override
    public MapCodec<MoveSpellEffect> getCodec() {
        return MAP_CODEC;
    }
}

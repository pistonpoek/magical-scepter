package io.github.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.particles.ExplosionParticleInfo;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ExplodeSpellEffect(
        boolean attributeToCaster,
        Optional<Holder<DamageType>> damageType,
        Optional<FloatProvider> knockbackMultiplier,
        Optional<HolderSet<Block>> immuneBlocks,
        FloatProvider radius,
        boolean createFire,
        Level.ExplosionInteraction blockInteraction,
        ParticleOptions particle,
        Holder<SoundEvent> sound
) implements SpellEffect {
    public static final MapCodec<ExplodeSpellEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Codec.BOOL.optionalFieldOf("attribute_to_caster", false).forGetter(ExplodeSpellEffect::attributeToCaster),
                            DamageType.CODEC.optionalFieldOf("damage_type").forGetter(ExplodeSpellEffect::damageType),
                            FloatProviders.CODEC.optionalFieldOf("knockback_multiplier").forGetter(ExplodeSpellEffect::knockbackMultiplier),
                            RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("immune_blocks").forGetter(ExplodeSpellEffect::immuneBlocks),
                            FloatProviders.CODEC.fieldOf("radius").forGetter(ExplodeSpellEffect::radius),
                            Codec.BOOL.optionalFieldOf("create_fire", false).forGetter(ExplodeSpellEffect::createFire),
                            Level.ExplosionInteraction.CODEC.fieldOf("block_interaction").forGetter(ExplodeSpellEffect::blockInteraction),
                            ParticleTypes.CODEC.fieldOf("particle").forGetter(ExplodeSpellEffect::particle),
                            SoundEvent.CODEC.fieldOf("sound").forGetter(ExplodeSpellEffect::sound)
                    )
                    .apply(instance, ExplodeSpellEffect::new)
    );

    @Override
    public void apply(SpellContext context) {
        Vec3 vec3d = context.position();
        RandomSource random = context.getRandom();
        context.getWorld().explode(
                this.attributeToCaster ? context.caster() : null,
                this.getDamageSource(context.caster(), vec3d),
                new SimpleExplosionDamageCalculator(
                        this.blockInteraction != Level.ExplosionInteraction.NONE,
                        this.damageType.isPresent(),
                        this.knockbackMultiplier.map(provider -> provider.sample(random)),
                        this.immuneBlocks
                ),
                vec3d.x(),
                vec3d.y(),
                vec3d.z(),
                Math.max(this.radius.sample(random), 0.0F),
                this.createFire,
                this.blockInteraction,
                this.particle,
                this.particle,
                WeightedList.<ExplosionParticleInfo>builder()
                        .add(new ExplosionParticleInfo(ParticleTypes.POOF, 0.5F, 1.0F))
                        .add(new ExplosionParticleInfo(ParticleTypes.SMOKE, 1.0F, 1.0F))
                        .build(),
                this.sound
        );
    }

    @Nullable
    private DamageSource getDamageSource(Entity caster, Vec3 position) {
        return this.damageType.map(damageTypeRegistryEntry -> this.attributeToCaster
                ? new DamageSource(damageTypeRegistryEntry, caster)
                : new DamageSource(damageTypeRegistryEntry, position)).orElse(null);
    }

    @Override
    public MapCodec<ExplodeSpellEffect> getCodec() {
        return MAP_CODEC;
    }
}

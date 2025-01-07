package io.github.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pistonpoek.magicalscepter.spell.cast.context.SpellContext;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.explosion.AdvancedExplosionBehavior;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ExplodeSpellEffect(
        boolean attributeToUser,
        Optional<RegistryEntry<DamageType>> damageType,
        Optional<FloatProvider> knockbackMultiplier,
        Optional<RegistryEntryList<Block>> immuneBlocks,
        FloatProvider radius,
        boolean createFire,
        World.ExplosionSourceType blockInteraction,
        ParticleEffect smallParticle,
        ParticleEffect largeParticle,
        RegistryEntry<SoundEvent> sound
) implements SpellEffect {
    public static final MapCodec<ExplodeSpellEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Codec.BOOL.optionalFieldOf("attribute_to_user", false).forGetter(ExplodeSpellEffect::attributeToUser),
                            DamageType.ENTRY_CODEC.optionalFieldOf("damage_type").forGetter(ExplodeSpellEffect::damageType),
                            FloatProvider.VALUE_CODEC.optionalFieldOf("knockback_multiplier").forGetter(ExplodeSpellEffect::knockbackMultiplier),
                            RegistryCodecs.entryList(RegistryKeys.BLOCK).optionalFieldOf("immune_blocks").forGetter(ExplodeSpellEffect::immuneBlocks),
                            FloatProvider.VALUE_CODEC.fieldOf("radius").forGetter(ExplodeSpellEffect::radius),
                            Codec.BOOL.optionalFieldOf("create_fire", false).forGetter(ExplodeSpellEffect::createFire),
                            World.ExplosionSourceType.CODEC.fieldOf("block_interaction").forGetter(ExplodeSpellEffect::blockInteraction),
                            ParticleTypes.TYPE_CODEC.fieldOf("small_particle").forGetter(ExplodeSpellEffect::smallParticle),
                            ParticleTypes.TYPE_CODEC.fieldOf("large_particle").forGetter(ExplodeSpellEffect::largeParticle),
                            SoundEvent.ENTRY_CODEC.fieldOf("sound").forGetter(ExplodeSpellEffect::sound)
                    )
                    .apply(instance, ExplodeSpellEffect::new)
    );

    @Override
    public void apply(SpellContext context) {
        Vec3d vec3d = context.position();
        Random random = context.getRandom();
        context.getWorld().createExplosion(
                this.attributeToUser ? context.caster() : null,
                this.getDamageSource(context.caster(), vec3d),
                new AdvancedExplosionBehavior(
                        this.blockInteraction != World.ExplosionSourceType.NONE,
                        this.damageType.isPresent(),
                        this.knockbackMultiplier.map(provider -> provider.get(random)),
                        this.immuneBlocks
                ),
                vec3d.getX(),
                vec3d.getY(),
                vec3d.getZ(),
                Math.max(this.radius.get(random), 0.0F),
                this.createFire,
                this.blockInteraction,
                this.smallParticle,
                this.largeParticle,
                this.sound
        );
    }

    @Nullable
    private DamageSource getDamageSource(Entity caster, Vec3d position) {
        return this.damageType.map(damageTypeRegistryEntry -> this.attributeToUser
                ? new DamageSource(damageTypeRegistryEntry, caster)
                : new DamageSource(damageTypeRegistryEntry, position)).orElse(null);
    }

    @Override
    public MapCodec<ExplodeSpellEffect> getCodec() {
        return MAP_CODEC;
    }
}

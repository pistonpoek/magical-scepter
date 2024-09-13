package net.pistonpoek.magicalscepter.spell.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.pistonpoek.magicalscepter.MagicalScepter;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.pistonpoek.magicalscepter.spell.projectile.SpellProjectile;
import net.pistonpoek.magicalscepter.spell.projectile.SpellProjectileHelper;

public record ShootProjectileSpellEffect(
        RegistryEntryList<SpellProjectile<? extends Entity>> projectile,
        float velocity
) implements SpellEffect {
    public static final MapCodec<ShootProjectileSpellEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            RegistryCodecs.entryList(ModRegistryKeys.SPELL_PROJECTILE_TYPE).fieldOf("projectile").forGetter(ShootProjectileSpellEffect::projectile),
                            Codec.FLOAT.fieldOf("velocity").forGetter(ShootProjectileSpellEffect::velocity)
                    )
                    .apply(instance, ShootProjectileSpellEffect::new)
    );

    @Override
    public void apply(ServerWorld world, Entity caster, Vec3d pos) {
        BlockPos blockPos = BlockPos.ofFloored(pos);
        if (World.isValid(blockPos)) {
            Optional<RegistryEntry<SpellProjectile<? extends Entity>>> spellProjectile =
                    this.projectile.getRandom(world.getRandom());
            if (spellProjectile.isPresent()) {
                Optional<? extends Entity> projectile = spellProjectile.get().value().shoot(world, caster, pos);
                if (projectile.isPresent()) {
                    projectile.get().refreshPositionAfterTeleport(SpellProjectileHelper.getProjectilePosition(caster));
                    MagicalScepter.LOGGER.info("Shot a {}, triggered by spell", projectile.get().getClass().getSimpleName());
                }
            }
        }
    }

    @Override
    public MapCodec<ShootProjectileSpellEffect> getCodec() {
        return CODEC;
    }
}


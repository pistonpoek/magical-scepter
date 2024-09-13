package net.pistonpoek.magicalscepter.spell.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Vec3d;
import net.pistonpoek.magicalscepter.registry.ModRegistries;

import java.util.Optional;

public interface SpellProjectile<T extends Entity> {

    enum Type implements StringIdentifiable {
        SMALL_FIREBALL("small_fireball", new SmallFireballSpellProjectile()),
        FIREBALL("fireball", new FireballSpellProjectile()),
        DRAGON_FIREBALL("dragon_fireball", new DragonFireballSpellProjectile()),
        WIND_CHARGE("wind_charge", new WindChargeSpellProjectile()),
        SHULKER_BULLET("shulker_bullet", new ShulkerBulletSpellProjectile()),
        WITHER_SKULL("wither_skull", new WitherSkullSpellProjectile());

        private final String name;
        private final SpellProjectile<? extends Entity> spellProjectile;

        Type(String name, SpellProjectile<? extends Entity> spellProjectile) {
            this.name = name;
            this.spellProjectile = spellProjectile;
        }

        public RegistryEntry<SpellProjectile<? extends Entity>> getEntry() {
            return ModRegistries.SPELL_PROJECTILE_TYPE.getEntry(spellProjectile);
        }

        @Override
        public String asString() {
            return name;
        }
    }

    static void register(Registry<SpellProjectile<? extends Entity>> registry) {
        for (Type projectileType: Type.values()) {
            Registry.register(registry, projectileType.name, projectileType.spellProjectile);
        }
    }

    // TODO add more variables to the shoot method.
    Optional<T> shoot(ServerWorld world, Entity caster, Vec3d pos);
}

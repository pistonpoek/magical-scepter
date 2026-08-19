package io.github.pistonpoek.magicalscepter.entity;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.world.entity.EntityTypeIds
 */
public class ModEntityTypeIds {
    public static final ResourceKey<EntityType<?>> SORCERER = create("sorcerer");
    public static final ResourceKey<EntityType<?>> SPELL_DRAGON_FIREBALL = create("spell_dragon_fireball");
    public static final ResourceKey<EntityType<?>> SPELL_FIRE_CHARGE = create("spell_fire_charge");
    public static final ResourceKey<EntityType<?>> SPELL_FIREBALL = create("spell_fireball");
    public static final ResourceKey<EntityType<?>> SPELL_GUARDIAN_BEAM = create("spell_guardian_beam");
    public static final ResourceKey<EntityType<?>> SPELL_WITHER_SKULL = create("spell_wither_skull");

    /**
     * Create the resource key for the specified entity type name.
     *
     * @param name Mod entity type name to create key for.
     * @return Resource key for the specified id.
     */
    private static ResourceKey<EntityType<?>> create(final String name) {
        return ResourceKey.create(Registries.ENTITY_TYPE, ModIdentifier.of(name));
    }
}

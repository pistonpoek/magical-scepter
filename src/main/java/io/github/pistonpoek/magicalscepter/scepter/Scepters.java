package io.github.pistonpoek.magicalscepter.scepter;

import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.registry.tag.ModDamageTypeTags;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import io.github.pistonpoek.magicalscepter.spell.Spells;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.advancements.predicates.ContextAwarePredicate;
import net.minecraft.advancements.predicates.DamageSourcePredicate;
import net.minecraft.advancements.predicates.TagPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.predicates.entity.EntityTypePredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Collection of scepters.
 */
public class Scepters {
    public static final List<ResourceKey<Scepter>> KEYS = new ArrayList<>();
    public static final ResourceKey<Scepter> MAGICAL_KEY = of("magical");

    public static final ResourceKey<Scepter> BLAZE_KEY = of("blaze");
    public static final ResourceKey<Scepter> BREEZE_KEY = of("breeze");
    public static final ResourceKey<Scepter> DRAGON_KEY = of("dragon");
    public static final ResourceKey<Scepter> EVOKER_KEY = of("evoker");
    public static final ResourceKey<Scepter> GHAST_KEY = of("ghast");
    public static final ResourceKey<Scepter> GUARDIAN_KEY = of("guardian");
    public static final ResourceKey<Scepter> SHULKER_KEY = of("shulker");
    public static final ResourceKey<Scepter> WARDEN_KEY = of("warden");
    public static final ResourceKey<Scepter> WITHER_KEY = of("wither");
    public static final List<ResourceKey<Scepter>> ALL_INFUSED_SCEPTERS =
            List.of(BLAZE_KEY, BREEZE_KEY, DRAGON_KEY, EVOKER_KEY,
                    GHAST_KEY, GUARDIAN_KEY, SHULKER_KEY, WARDEN_KEY, WITHER_KEY);

    /**
     * Get a scepter registry key for the specified name.
     *
     * @param name String name to get scepter registry key for.
     * @return Scepter registry key with the specified name.
     */
    private static ResourceKey<Scepter> of(String name) {
        return ResourceKey.create(ModRegistryKeys.SCEPTER, ModIdentifier.of(name));
    }

    /**
     * Bootstrap the scepter registry.
     *
     * @param registry Scepter registry to bootstrap.
     */
    public static void bootstrap(BootstrapContext<Scepter> registry) {
        HolderGetter<Spell> spellRegistryEntryLookup = registry.lookup(ModRegistryKeys.SPELL);
        HolderGetter<EntityType<?>> entityTypeRegistryEntryLookup = registry.lookup(Registries.ENTITY_TYPE);
        register(registry, MAGICAL_KEY, Scepter.builder(
                                0xBC7C5C,
                                4,
                                true
                        )
                        .attackSpell(
                                spellRegistryEntryLookup.getOrThrow(Spells.MAGICAL_ATTACK_KEY)
                        )
                        .protectSpell(
                                spellRegistryEntryLookup.getOrThrow(Spells.MAGICAL_RESISTANCE_KEY)
                        )
        );

        register(registry, BLAZE_KEY, Scepter.builder(
                                0xFF9900,
                                4,
                                false
                        )
                        .attackSpell(
                                spellRegistryEntryLookup.getOrThrow(Spells.BLAZE_FIRE_CHARGES)
                        )
                        .protectSpell(
                                spellRegistryEntryLookup.getOrThrow(Spells.BLAZE_FIRE_RESISTANCE_KEY)
                        )
                        .infusion(ContextAwarePredicate.create(
                                DamageSourceCondition.hasDamageSource(
                                        DamageSourcePredicate.Builder.damageType()
                                                .source(EntityPredicate.Builder.entity()
                                                        .entityType(EntityTypePredicate.of(entityTypeRegistryEntryLookup,
                                                                EntityTypes.BLAZE)))
                                                .direct(EntityPredicate.Builder.entity()
                                                        .entityType(EntityTypePredicate.of(entityTypeRegistryEntryLookup,
                                                                EntityTypes.SMALL_FIREBALL)))
                                                .tag(TagPredicate.is(ModDamageTypeTags.BLAZE_INFUSION))
                                ).build()
                        ))
        );

        register(registry, BREEZE_KEY, Scepter.builder(
                                0xBDC9FF,
                                2,
                                false
                        )
                        .attackSpell(
                                spellRegistryEntryLookup.getOrThrow(Spells.BREEZE_WIND_CHARGE_KEY)
                        )
                        .protectSpell(
                                spellRegistryEntryLookup.getOrThrow(Spells.BREEZE_JUMP_KEY)
                        )
                        .infusion(ContextAwarePredicate.create(
                                DamageSourceCondition.hasDamageSource(
                                        DamageSourcePredicate.Builder.damageType()
                                                .source(EntityPredicate.Builder.entity()
                                                        .entityType(EntityTypePredicate.of(entityTypeRegistryEntryLookup,
                                                                EntityTypes.BREEZE)))
                                                .direct(EntityPredicate.Builder.entity()
                                                        .entityType(EntityTypePredicate.of(entityTypeRegistryEntryLookup,
                                                                EntityTypes.BREEZE_WIND_CHARGE)))
                                                .tag(TagPredicate.is(ModDamageTypeTags.BREEZE_INFUSION))
                                ).build()
                        ))
        );

        register(registry, DRAGON_KEY, Scepter.builder(
                                0xB823F5,
                                8,
                                false
                        )
                        .attackSpell(
                                spellRegistryEntryLookup.getOrThrow(Spells.DRAGON_FIREBALL_KEY)
                        )
                        .protectSpell(
                                spellRegistryEntryLookup.getOrThrow(Spells.DRAGON_GROWL_KEY)
                        )
                        .infusion(ContextAwarePredicate.create(
                                DamageSourceCondition.hasDamageSource(
                                        DamageSourcePredicate.Builder.damageType()
                                                .source(EntityPredicate.Builder.entity()
                                                        .entityType(EntityTypePredicate.of(entityTypeRegistryEntryLookup,
                                                                EntityTypes.ENDER_DRAGON)))
                                                .direct(EntityPredicate.Builder.entity()
                                                        .entityType(EntityTypePredicate.of(entityTypeRegistryEntryLookup,
                                                                EntityTypes.AREA_EFFECT_CLOUD)))
                                                .tag(TagPredicate.is(ModDamageTypeTags.DRAGON_INFUSION))
                                ).build()
                        ))
        );

        register(registry, EVOKER_KEY, Scepter.builder(
                                0x959B9B,
                                4,
                                false
                        )
                        .attackSpell(
                                spellRegistryEntryLookup.getOrThrow(Spells.EVOKER_FANG_LINE_KEY)
                        )
                        .protectSpell(
                                spellRegistryEntryLookup.getOrThrow(Spells.EVOKER_FANG_CIRCLE_KEY)
                        )
                        .infusion(ContextAwarePredicate.create(
                                DamageSourceCondition.hasDamageSource(
                                        DamageSourcePredicate.Builder.damageType()
                                                .source(EntityPredicate.Builder.entity()
                                                        .entityType(EntityTypePredicate.of(entityTypeRegistryEntryLookup,
                                                                EntityTypes.EVOKER)))
                                                .direct(EntityPredicate.Builder.entity()
                                                        .entityType(EntityTypePredicate.of(entityTypeRegistryEntryLookup,
                                                                EntityTypes.EVOKER_FANGS)))
                                                .tag(TagPredicate.is(ModDamageTypeTags.EVOKER_INFUSION))
                                ).build()
                        ))
        );

        register(registry, GHAST_KEY, Scepter.builder(
                                0xCD5CAB,
                                4,
                                false
                        )
                        .attackSpell(
                                spellRegistryEntryLookup.getOrThrow(Spells.GHAST_FIREBALL_KEY)
                        )
                        .protectSpell(
                                spellRegistryEntryLookup.getOrThrow(Spells.GHAST_REGENERATION_KEY)
                        )
                        .infusion(ContextAwarePredicate.create(
                                DamageSourceCondition.hasDamageSource(
                                        DamageSourcePredicate.Builder.damageType()
                                                .source(EntityPredicate.Builder.entity()
                                                        .entityType(EntityTypePredicate.of(entityTypeRegistryEntryLookup,
                                                                EntityTypes.GHAST)))
                                                .direct(EntityPredicate.Builder.entity()
                                                        .entityType(EntityTypePredicate.of(entityTypeRegistryEntryLookup,
                                                                EntityTypes.FIREBALL)))
                                                .tag(TagPredicate.is(ModDamageTypeTags.GHAST_INFUSION))
                                ).build()
                        ))
        );

        register(registry, GUARDIAN_KEY, Scepter.builder(
                                0x4f7d8c,
                                3,
                                false
                        )
                        .attackSpell(
                                spellRegistryEntryLookup.getOrThrow(Spells.GUARDIAN_BEAM_KEY)
                        )
                        .protectSpell(
                                spellRegistryEntryLookup.getOrThrow(Spells.GUARDIAN_HASTE_KEY)
                        )
                        .infusion(ContextAwarePredicate.create(
                                DamageSourceCondition.hasDamageSource(
                                        DamageSourcePredicate.Builder.damageType()
                                                .source(EntityPredicate.Builder.entity()
                                                        .entityType(EntityTypePredicate.of(entityTypeRegistryEntryLookup,
                                                                EntityTypes.ELDER_GUARDIAN)))
                                                .direct(EntityPredicate.Builder.entity()
                                                        .entityType(EntityTypePredicate.of(entityTypeRegistryEntryLookup,
                                                                EntityTypes.ELDER_GUARDIAN)))
                                                .tag(TagPredicate.is(ModDamageTypeTags.GUARDIAN_INFUSION))
                                ).build()
                        ))
        );

        register(registry, SHULKER_KEY, Scepter.builder(
                                0xCEFFFF,
                                3,
                                false
                        )
                        .attackSpell(
                                spellRegistryEntryLookup.getOrThrow(Spells.SHULKER_BULLET_KEY)
                        )
                        .protectSpell(
                                spellRegistryEntryLookup.getOrThrow(Spells.SHULKER_TELEPORT_KEY)
                        )
                        .infusion(ContextAwarePredicate.create(
                                DamageSourceCondition.hasDamageSource(
                                        DamageSourcePredicate.Builder.damageType()
                                                .source(EntityPredicate.Builder.entity()
                                                        .entityType(EntityTypePredicate.of(entityTypeRegistryEntryLookup,
                                                                EntityTypes.SHULKER)))
                                                .direct(EntityPredicate.Builder.entity()
                                                        .entityType(EntityTypePredicate.of(entityTypeRegistryEntryLookup,
                                                                EntityTypes.SHULKER_BULLET)))
                                                .tag(TagPredicate.is(ModDamageTypeTags.SHULKER_INFUSION))
                                ).build()
                        ))
        );

        register(registry, WARDEN_KEY, Scepter.builder(
                                0x2ce3eb,
                                5,
                                false
                        )
                        .attackSpell(
                                spellRegistryEntryLookup.getOrThrow(Spells.WARDEN_SONIC_BOOM_KEY)
                        )
                        .protectSpell(
                                spellRegistryEntryLookup.getOrThrow(Spells.WARDEN_STABILITY_KEY)
                        )
                        .infusion(ContextAwarePredicate.create(
                                DamageSourceCondition.hasDamageSource(
                                        DamageSourcePredicate.Builder.damageType()
                                                .source(EntityPredicate.Builder.entity()
                                                        .entityType(EntityTypePredicate.of(entityTypeRegistryEntryLookup,
                                                                EntityTypes.WARDEN)))
                                                .direct(EntityPredicate.Builder.entity()
                                                        .entityType(EntityTypePredicate.of(entityTypeRegistryEntryLookup,
                                                                EntityTypes.WARDEN)))
                                                .tag(TagPredicate.is(ModDamageTypeTags.WARDEN_INFUSION))
                                ).build()
                        ))
        );

        register(registry, WITHER_KEY, Scepter.builder(
                                0x736156,
                                5,
                                false
                        )
                        .attackSpell(
                                spellRegistryEntryLookup.getOrThrow(Spells.WITHER_SKULL_KEY)
                        )
                        .protectSpell(
                                spellRegistryEntryLookup.getOrThrow(Spells.WITHER_REPULSION_KEY)
                        )
                        .infusion(ContextAwarePredicate.create(
                                DamageSourceCondition.hasDamageSource(
                                        DamageSourcePredicate.Builder.damageType()
                                                .source(EntityPredicate.Builder.entity()
                                                        .entityType(EntityTypePredicate.of(entityTypeRegistryEntryLookup,
                                                                EntityTypes.WITHER)))
                                                .direct(EntityPredicate.Builder.entity()
                                                        .entityType(EntityTypePredicate.of(entityTypeRegistryEntryLookup,
                                                                EntityTypes.WITHER_SKULL)))
                                                .tag(TagPredicate.is(ModDamageTypeTags.WITHER_INFUSION))
                                ).build()
                        ))
        );
    }

    /**
     * Register a scepter to the scepter registry under the specified registry key.
     *
     * @param registry Scepter registry to register in.
     * @param key Scepter registry key to register under.
     * @param builder Scepter builder to register.
     */
    private static void register(BootstrapContext<Scepter> registry, ResourceKey<Scepter> key, Scepter.Builder builder) {
        KEYS.add(key);
        registry.register(key, builder.build());
    }

    /**
     * Get the translation key for the specified scepter registry key.
     *
     * @param scepter Scepter registry key to get translation key for.
     * @return String translation key for the specified scepter registry key.
     */
    public static String getTranslationKey(@Nullable ResourceKey<Scepter> scepter) {
        return Optional.ofNullable(scepter)
                .map(key -> key.identifier().getPath().replace("/", "."))
                .orElse("empty");
    }
}

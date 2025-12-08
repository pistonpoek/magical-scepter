package io.github.pistonpoek.magicalscepter.scepter;

import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.registry.tag.ModDamageTypeTags;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import io.github.pistonpoek.magicalscepter.spell.Spells;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.condition.DamageSourcePropertiesLootCondition;
import net.minecraft.predicate.TagPredicate;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.EntityTypePredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Collection of scepters.
 */
public class Scepters {
    public static final List<RegistryKey<Scepter>> KEYS = new ArrayList<>();
    public static final RegistryKey<Scepter> MAGICAL_KEY = of("magical");

    public static final RegistryKey<Scepter> BLAZE_KEY = of("blaze");
    public static final RegistryKey<Scepter> BREEZE_KEY = of("breeze");
    public static final RegistryKey<Scepter> DRAGON_KEY = of("dragon");
    public static final RegistryKey<Scepter> EVOKER_KEY = of("evoker");
    public static final RegistryKey<Scepter> GHAST_KEY = of("ghast");
    public static final RegistryKey<Scepter> GUARDIAN_KEY = of("guardian");
    public static final RegistryKey<Scepter> SHULKER_KEY = of("shulker");
    public static final RegistryKey<Scepter> WARDEN_KEY = of("warden");
    public static final RegistryKey<Scepter> WITHER_KEY = of("wither");
    public static final List<RegistryKey<Scepter>> ALL_INFUSED_SCEPTERS =
            List.of(BLAZE_KEY, BREEZE_KEY, DRAGON_KEY, EVOKER_KEY,
                    GHAST_KEY, GUARDIAN_KEY, SHULKER_KEY, WARDEN_KEY, WITHER_KEY);

    /**
     * Get a scepter registry key for the specified name.
     *
     * @param name String name to get scepter registry key for.
     * @return Scepter registry key with the specified name.
     */
    private static RegistryKey<Scepter> of(String name) {
        return RegistryKey.of(ModRegistryKeys.SCEPTER, ModIdentifier.of(name));
    }

    /**
     * Bootstrap the scepter registry.
     *
     * @param registry Scepter registry to bootstrap.
     */
    public static void bootstrap(Registerable<Scepter> registry) {
        RegistryEntryLookup<Spell> spellRegistryEntryLookup = registry.getRegistryLookup(ModRegistryKeys.SPELL);
        RegistryEntryLookup<EntityType<?>> entityTypeRegistryEntryLookup = registry.getRegistryLookup(RegistryKeys.ENTITY_TYPE);
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
                        .infusion(LootContextPredicate.create(
                                DamageSourcePropertiesLootCondition.builder(
                                        DamageSourcePredicate.Builder.create()
                                                .sourceEntity(EntityPredicate.Builder.create()
                                                        .type(EntityTypePredicate.create(entityTypeRegistryEntryLookup,
                                                                EntityType.BLAZE)))
                                                .directEntity(EntityPredicate.Builder.create()
                                                        .type(EntityTypePredicate.create(entityTypeRegistryEntryLookup,
                                                                EntityType.SMALL_FIREBALL)))
                                                .tag(TagPredicate.expected(ModDamageTypeTags.BLAZE_INFUSION))
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
                        .infusion(LootContextPredicate.create(
                                DamageSourcePropertiesLootCondition.builder(
                                        DamageSourcePredicate.Builder.create()
                                                .sourceEntity(EntityPredicate.Builder.create()
                                                        .type(EntityTypePredicate.create(entityTypeRegistryEntryLookup,
                                                                EntityType.BREEZE)))
                                                .directEntity(EntityPredicate.Builder.create()
                                                        .type(EntityTypePredicate.create(entityTypeRegistryEntryLookup,
                                                                EntityType.WIND_CHARGE)))
                                                .tag(TagPredicate.expected(ModDamageTypeTags.BREEZE_INFUSION))
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
                        .infusion(LootContextPredicate.create(
                                DamageSourcePropertiesLootCondition.builder(
                                        DamageSourcePredicate.Builder.create()
                                                .sourceEntity(EntityPredicate.Builder.create()
                                                        .type(EntityTypePredicate.create(entityTypeRegistryEntryLookup,
                                                                EntityType.ENDER_DRAGON)))
                                                .directEntity(EntityPredicate.Builder.create()
                                                        .type(EntityTypePredicate.create(entityTypeRegistryEntryLookup,
                                                                EntityType.AREA_EFFECT_CLOUD)))
                                                .tag(TagPredicate.expected(ModDamageTypeTags.DRAGON_INFUSION))
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
                        .infusion(LootContextPredicate.create(
                                DamageSourcePropertiesLootCondition.builder(
                                        DamageSourcePredicate.Builder.create()
                                                .sourceEntity(EntityPredicate.Builder.create()
                                                        .type(EntityTypePredicate.create(entityTypeRegistryEntryLookup,
                                                                EntityType.EVOKER)))
                                                .directEntity(EntityPredicate.Builder.create()
                                                        .type(EntityTypePredicate.create(entityTypeRegistryEntryLookup,
                                                                EntityType.EVOKER_FANGS)))
                                                .tag(TagPredicate.expected(ModDamageTypeTags.EVOKER_INFUSION))
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
                        .infusion(LootContextPredicate.create(
                                DamageSourcePropertiesLootCondition.builder(
                                        DamageSourcePredicate.Builder.create()
                                                .sourceEntity(EntityPredicate.Builder.create()
                                                        .type(EntityTypePredicate.create(entityTypeRegistryEntryLookup,
                                                                EntityType.GHAST)))
                                                .directEntity(EntityPredicate.Builder.create()
                                                        .type(EntityTypePredicate.create(entityTypeRegistryEntryLookup,
                                                                EntityType.FIREBALL)))
                                                .tag(TagPredicate.expected(ModDamageTypeTags.GHAST_INFUSION))
                                ).build()
                        ))
        );

        register(registry, GUARDIAN_KEY, Scepter.builder(
                                0x4f7d8c,
                                3,
                                false
                        )
                        .attackSpell(
                                spellRegistryEntryLookup.getOrThrow(Spells.GUARDIAN_BOLT_KEY)
                        )
                        .protectSpell(
                                spellRegistryEntryLookup.getOrThrow(Spells.GUARDIAN_HASTE_KEY)
                        )
                        .infusion(LootContextPredicate.create(
                                DamageSourcePropertiesLootCondition.builder(
                                        DamageSourcePredicate.Builder.create()
                                                .sourceEntity(EntityPredicate.Builder.create()
                                                        .type(EntityTypePredicate.create(entityTypeRegistryEntryLookup,
                                                                EntityType.ELDER_GUARDIAN)))
                                                .directEntity(EntityPredicate.Builder.create()
                                                        .type(EntityTypePredicate.create(entityTypeRegistryEntryLookup,
                                                                EntityType.ELDER_GUARDIAN)))
                                                .tag(TagPredicate.expected(ModDamageTypeTags.GUARDIAN_INFUSION))
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
                        .infusion(LootContextPredicate.create(
                                DamageSourcePropertiesLootCondition.builder(
                                        DamageSourcePredicate.Builder.create()
                                                .sourceEntity(EntityPredicate.Builder.create()
                                                        .type(EntityTypePredicate.create(entityTypeRegistryEntryLookup,
                                                                EntityType.SHULKER)))
                                                .directEntity(EntityPredicate.Builder.create()
                                                        .type(EntityTypePredicate.create(entityTypeRegistryEntryLookup,
                                                                EntityType.SHULKER_BULLET)))
                                                .tag(TagPredicate.expected(ModDamageTypeTags.SHULKER_INFUSION))
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
                        .infusion(LootContextPredicate.create(
                                DamageSourcePropertiesLootCondition.builder(
                                        DamageSourcePredicate.Builder.create()
                                                .sourceEntity(EntityPredicate.Builder.create()
                                                        .type(EntityTypePredicate.create(entityTypeRegistryEntryLookup,
                                                                EntityType.WARDEN)))
                                                .directEntity(EntityPredicate.Builder.create()
                                                        .type(EntityTypePredicate.create(entityTypeRegistryEntryLookup,
                                                                EntityType.WARDEN)))
                                                .tag(TagPredicate.expected(ModDamageTypeTags.WARDEN_INFUSION))
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
                        .infusion(LootContextPredicate.create(
                                DamageSourcePropertiesLootCondition.builder(
                                        DamageSourcePredicate.Builder.create()
                                                .sourceEntity(EntityPredicate.Builder.create()
                                                        .type(EntityTypePredicate.create(entityTypeRegistryEntryLookup,
                                                                EntityType.WITHER)))
                                                .directEntity(EntityPredicate.Builder.create()
                                                        .type(EntityTypePredicate.create(entityTypeRegistryEntryLookup,
                                                                EntityType.WITHER_SKULL)))
                                                .tag(TagPredicate.expected(ModDamageTypeTags.WITHER_INFUSION))
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
    private static void register(Registerable<Scepter> registry, RegistryKey<Scepter> key, Scepter.Builder builder) {
        KEYS.add(key);
        registry.register(key, builder.build());
    }

    /**
     * Get the translation key for the specified scepter registry key.
     *
     * @param scepter Scepter registry key to get translation key for.
     * @return String translation key for the specified scepter registry key.
     */
    public static String getTranslationKey(@Nullable RegistryKey<Scepter> scepter) {
        return Optional.ofNullable(scepter)
                .map(key -> key.getValue().getPath().replace("/", "."))
                .orElse("empty");
    }
}

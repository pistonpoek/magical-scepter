package net.pistonpoek.magicalscepter.scepter;

import net.minecraft.entity.EntityType;
import net.minecraft.loot.condition.DamageSourcePropertiesLootCondition;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.pistonpoek.magicalscepter.spell.Spell;
import net.pistonpoek.magicalscepter.spell.Spells;

import java.util.ArrayList;
import java.util.List;

public class Scepters {
    public static final List<RegistryKey<Scepter>> SCEPTER_KEYS = new ArrayList<>();
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

    private static RegistryKey<Scepter> of(String id) {
        return RegistryKey.of(ModRegistryKeys.SCEPTER, ModIdentifier.of(id));
    }

    public static void bootstrap(Registerable<Scepter> registry) {
        RegistryEntryLookup<Spell> spellRegistryEntryLookup = registry.getRegistryLookup(ModRegistryKeys.SPELL);
        register(registry, MAGICAL_KEY, Scepter.builder(
                0xBC7C5C,
                true,
                spellRegistryEntryLookup.getOrThrow(Spells.MAGICAL_ATTACK_KEY),
                spellRegistryEntryLookup.getOrThrow(Spells.MAGICAL_RESISTANCE_KEY)));

        register(registry, BLAZE_KEY, Scepter.builder(
                0xFF9900,
                false,
                spellRegistryEntryLookup.getOrThrow(Spells.BLAZE_SMALL_FIREBALL_KEY),
                spellRegistryEntryLookup.getOrThrow(Spells.BLAZE_FIRE_RESISTANCE_KEY))
                .infusion(LootContextPredicate.create(
                        DamageSourcePropertiesLootCondition.builder(
                                DamageSourcePredicate.Builder.create()
                                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.BLAZE))
                        ).build()
                ))
        );

        register(registry, BREEZE_KEY, Scepter.builder(
                        0xBDC9FF,
                        false,
                        spellRegistryEntryLookup.getOrThrow(Spells.BREEZE_WIND_CHARGE_KEY),
                        spellRegistryEntryLookup.getOrThrow(Spells.BREEZE_JUMP_KEY))
                .infusion(LootContextPredicate.create(
                        DamageSourcePropertiesLootCondition.builder(
                                DamageSourcePredicate.Builder.create()
                                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.BREEZE))
                        ).build()
                ))
        );

        register(registry, DRAGON_KEY, Scepter.builder(
                        0xB823F5,
                        false,
                        spellRegistryEntryLookup.getOrThrow(Spells.DRAGON_FIREBALL_KEY),
                        spellRegistryEntryLookup.getOrThrow(Spells.DRAGON_GROWL_KEY))
                .infusion(LootContextPredicate.create(
                        DamageSourcePropertiesLootCondition.builder(
                                DamageSourcePredicate.Builder.create()
                                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.ENDER_DRAGON))
                        ).build()
                ))
        );

        register(registry, EVOKER_KEY, Scepter.builder(
                        0x959B9B,
                        false,
                        spellRegistryEntryLookup.getOrThrow(Spells.EVOKER_FANG_LINE_KEY),
                        spellRegistryEntryLookup.getOrThrow(Spells.EVOKER_FANG_CIRCLE_KEY))
                .infusion(LootContextPredicate.create(
                        DamageSourcePropertiesLootCondition.builder(
                                DamageSourcePredicate.Builder.create()
                                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.EVOKER))
                                        .directEntity(EntityPredicate.Builder.create().type(EntityType.EVOKER_FANGS))
                        ).build()
                ))
        );

        register(registry, GHAST_KEY, Scepter.builder(
                        0xCD5CAB,
                        false,
                        spellRegistryEntryLookup.getOrThrow(Spells.GHAST_FIREBALL_KEY),
                        spellRegistryEntryLookup.getOrThrow(Spells.GHAST_REGENERATION_KEY))
                .infusion(LootContextPredicate.create(
                        DamageSourcePropertiesLootCondition.builder(
                                DamageSourcePredicate.Builder.create()
                                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.GHAST))
                        ).build()
                ))
        );

        register(registry, GUARDIAN_KEY, Scepter.builder(
                        0x4f7d8c,
                        false,
                        spellRegistryEntryLookup.getOrThrow(Spells.GUARDIAN_BEAM_KEY),
                        spellRegistryEntryLookup.getOrThrow(Spells.GUARDIAN_HASTE_KEY))
                .infusion(LootContextPredicate.create(
                        DamageSourcePropertiesLootCondition.builder(
                                DamageSourcePredicate.Builder.create()
                                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.ELDER_GUARDIAN))
                        ).build()
                ))
        );

        register(registry, SHULKER_KEY, Scepter.builder(
                        0xCEFFFF,
                        false,
                        spellRegistryEntryLookup.getOrThrow(Spells.SHULKER_BULLET_KEY),
                        spellRegistryEntryLookup.getOrThrow(Spells.SHULKER_TELEPORT_KEY))
                .infusion(LootContextPredicate.create(
                        DamageSourcePropertiesLootCondition.builder(
                                DamageSourcePredicate.Builder.create()
                                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.SHULKER))
                        ).build()
                ))
        );

        register(registry, WARDEN_KEY, Scepter.builder(
                        0x2ce3eb,
                        false,
                        spellRegistryEntryLookup.getOrThrow(Spells.WARDEN_SONIC_BOOM_KEY),
                        spellRegistryEntryLookup.getOrThrow(Spells.WARDEN_STABILITY_KEY))
                .infusion(LootContextPredicate.create(
                        DamageSourcePropertiesLootCondition.builder(
                                DamageSourcePredicate.Builder.create()
                                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.WARDEN))
                        ).build()
                ))
        );

        register(registry, WITHER_KEY, Scepter.builder(
                        0x736156,
                        false,
                        spellRegistryEntryLookup.getOrThrow(Spells.WITHER_SKULL_KEY),
                        spellRegistryEntryLookup.getOrThrow(Spells.WITHER_SHIELD_KEY))
                .infusion(LootContextPredicate.create(
                        DamageSourcePropertiesLootCondition.builder(
                                DamageSourcePredicate.Builder.create()
                                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.WITHER))
                        ).build()
                ))
        );
    }

    private static void register(Registerable<Scepter> registry, RegistryKey<Scepter> key, Scepter.Builder builder) {
        SCEPTER_KEYS.add(key);
        registry.register(key, builder.build());
    }
}

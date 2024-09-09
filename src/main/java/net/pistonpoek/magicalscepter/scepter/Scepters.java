package net.pistonpoek.magicalscepter.scepter;

import net.minecraft.entity.EntityType;
import net.minecraft.loot.condition.DamageSourcePropertiesLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Scepters {
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
    public static final RegistryKey<Scepter> DEFAULT_KEY = MAGICAL_KEY;

    public static final Scepter MAGICAL = create(0xBC7C5C, true,
            null, SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL);

    protected static LootCondition.@NotNull Builder damagedByBlaze() {
        return DamageSourcePropertiesLootCondition.builder(
                DamageSourcePredicate.Builder.create()
                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.BLAZE))
        );
    }
    public static final Scepter BLAZE = create(0xFF9900, false,
            damagedByBlaze(), SoundEvents.ENTITY_BLAZE_SHOOT);

    protected static LootCondition.@NotNull Builder damagedByBreeze() {
        return DamageSourcePropertiesLootCondition.builder(
                DamageSourcePredicate.Builder.create()
                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.BREEZE))
        );
    }
    public static final Scepter BREEZE = create(0xBDC9FF, false,
            damagedByBreeze(), SoundEvents.ENTITY_BREEZE_SHOOT);

    protected static LootCondition.@NotNull Builder damagedByDragon() {
        return DamageSourcePropertiesLootCondition.builder(
                DamageSourcePredicate.Builder.create()
                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.ENDER_DRAGON))
        );
    }
    public static final Scepter DRAGON = create(0xB823F5, false,
            damagedByDragon(), SoundEvents.ENTITY_ENDER_DRAGON_SHOOT);

    protected static LootCondition.@NotNull Builder damagedByEvokerFangs() {
        return DamageSourcePropertiesLootCondition.builder(
                DamageSourcePredicate.Builder.create()
                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.EVOKER))
                        .directEntity(EntityPredicate.Builder.create().type(EntityType.EVOKER_FANGS))
        );
    }
    public static final Scepter EVOKER = create(0x959B9B, false,
            damagedByEvokerFangs(), SoundEvents.ENTITY_EVOKER_CAST_SPELL);

    protected static LootCondition.@NotNull Builder damagedByGhast() {
        return DamageSourcePropertiesLootCondition.builder(
                DamageSourcePredicate.Builder.create()
                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.GHAST))
        );
    }
    public static final Scepter GHAST = create(0xCD5CAB, false,
            damagedByGhast(), SoundEvents.ENTITY_GHAST_SHOOT);

    protected static LootCondition.@NotNull Builder damagedByGuardian() {
        return DamageSourcePropertiesLootCondition.builder(
                DamageSourcePredicate.Builder.create()
                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.GUARDIAN))
        ).or(DamageSourcePropertiesLootCondition.builder(
                DamageSourcePredicate.Builder.create()
                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.ELDER_GUARDIAN))));
    }
    public static final Scepter GUARDIAN = create(0x4f7d8c, false,
            damagedByGuardian(), SoundEvents.ENTITY_GUARDIAN_ATTACK);

    protected static LootCondition.@NotNull Builder damagedByShulker() {
        return DamageSourcePropertiesLootCondition.builder(
                DamageSourcePredicate.Builder.create()
                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.SHULKER))
        );
    }
    public static final Scepter SHULKER = create(0xCEFFFF, false,
            damagedByShulker(), SoundEvents.ENTITY_SHULKER_SHOOT);

    protected static LootCondition.@NotNull Builder damagedByWarden() {
        return DamageSourcePropertiesLootCondition.builder(
                DamageSourcePredicate.Builder.create()
                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.WARDEN))
        );
    }
    public static final Scepter WARDEN = create(0x2ce3eb, false,
            damagedByWarden(), SoundEvents.ENTITY_WARDEN_SONIC_BOOM);

    protected static LootCondition.@NotNull Builder damagedByWither() {
        return DamageSourcePropertiesLootCondition.builder(
                DamageSourcePredicate.Builder.create()
                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.WITHER))
        );
    }
    public static final Scepter WITHER = create(0x736156, false,
            damagedByWither(), SoundEvents.ENTITY_WITHER_SHOOT);

    private static Scepter create(int color, boolean infusable,
                                  @Nullable LootCondition.Builder lootConditionBuilder,
                                  @Nullable SoundEvent soundEvent) {
        return new Scepter(color, infusable,
                Optional.ofNullable(lootConditionBuilder).map(builder -> LootContextPredicate.create(builder.build())),
                Optional.ofNullable(soundEvent).map(Registries.SOUND_EVENT::getEntry));
    }

    public static Identifier registerAndGetDefault() {
        return DEFAULT_KEY.getValue();
    }

    private static RegistryKey<Scepter> of(String id) {
        return RegistryKey.of(ModRegistryKeys.SCEPTER, ModIdentifier.of(id));
    }
}

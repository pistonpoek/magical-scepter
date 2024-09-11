package net.pistonpoek.magicalscepter.scepter;

import net.minecraft.entity.EntityType;
import net.minecraft.loot.condition.DamageSourcePropertiesLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.pistonpoek.magicalscepter.spell.Spell;
import net.pistonpoek.magicalscepter.spell.Spells;
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

    private static RegistryKey<Scepter> of(String id) {
        return RegistryKey.of(ModRegistryKeys.SCEPTER, ModIdentifier.of(id));
    }

    public static final Scepter MAGICAL = create(0xBC7C5C, true, Spells.MAGICAL,
            null);

    protected static LootCondition.@NotNull Builder damagedByBlaze() {
        return DamageSourcePropertiesLootCondition.builder(
                DamageSourcePredicate.Builder.create()
                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.BLAZE))
        );
    }
    public static final Scepter BLAZE = create(0xFF9900, false, Spells.BLAZE,
            damagedByBlaze());

    protected static LootCondition.@NotNull Builder damagedByBreeze() {
        return DamageSourcePropertiesLootCondition.builder(
                DamageSourcePredicate.Builder.create()
                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.BREEZE))
        );
    }
    public static final Scepter BREEZE = create(0xBDC9FF, false, Spells.BREEZE,
            damagedByBreeze());

    protected static LootCondition.@NotNull Builder damagedByDragon() {
        return DamageSourcePropertiesLootCondition.builder(
                DamageSourcePredicate.Builder.create()
                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.ENDER_DRAGON))
        );
    }
    public static final Scepter DRAGON = create(0xB823F5, false, Spells.DRAGON,
            damagedByDragon());

    protected static LootCondition.@NotNull Builder damagedByEvokerFangs() {
        return DamageSourcePropertiesLootCondition.builder(
                DamageSourcePredicate.Builder.create()
                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.EVOKER))
                        .directEntity(EntityPredicate.Builder.create().type(EntityType.EVOKER_FANGS))
        );
    }
    public static final Scepter EVOKER = create(0x959B9B, false, Spells.EVOKER,
            damagedByEvokerFangs());

    protected static LootCondition.@NotNull Builder damagedByGhast() {
        return DamageSourcePropertiesLootCondition.builder(
                DamageSourcePredicate.Builder.create()
                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.GHAST))
        );
    }
    public static final Scepter GHAST = create(0xCD5CAB, false, Spells.GHAST,
            damagedByGhast());

    protected static LootCondition.@NotNull Builder damagedByElderGuardian() {
        return DamageSourcePropertiesLootCondition.builder(
                DamageSourcePredicate.Builder.create()
                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.ELDER_GUARDIAN)));
    }
    public static final Scepter GUARDIAN = create(0x4f7d8c, false, Spells.GUARDIAN,
            damagedByElderGuardian());

    protected static LootCondition.@NotNull Builder damagedByShulker() {
        return DamageSourcePropertiesLootCondition.builder(
                DamageSourcePredicate.Builder.create()
                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.SHULKER))
        );
    }
    public static final Scepter SHULKER = create(0xCEFFFF, false, Spells.SHULKER,
            damagedByShulker());

    protected static LootCondition.@NotNull Builder damagedByWarden() {
        return DamageSourcePropertiesLootCondition.builder(
                DamageSourcePredicate.Builder.create()
                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.WARDEN))
        );
    }
    public static final Scepter WARDEN = create(0x2ce3eb, false, Spells.WARDEN,
            damagedByWarden());

    protected static LootCondition.@NotNull Builder damagedByWither() {
        return DamageSourcePropertiesLootCondition.builder(
                DamageSourcePredicate.Builder.create()
                        .sourceEntity(EntityPredicate.Builder.create().type(EntityType.WITHER))
        );
    }
    public static final Scepter WITHER = create(0x736156, false, Spells.WITHER,
            damagedByWither());

    public static final Scepter DEFAULT = MAGICAL;

    private static Scepter create(int color, boolean infusable, Spell spell,
                                  @Nullable LootCondition.Builder lootConditionBuilder) {
        return new Scepter(color, infusable, spell,
                Optional.ofNullable(lootConditionBuilder).map(builder -> LootContextPredicate.create(builder.build())));
    }

    public static void init() {
    }
}

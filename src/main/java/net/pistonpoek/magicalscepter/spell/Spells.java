package net.pistonpoek.magicalscepter.spell;

import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.enchantment.effect.entity.ApplyMobEffectEnchantmentEffect;
import net.minecraft.enchantment.effect.entity.PlaySoundEnchantmentEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.loot.condition.AllOfLootCondition;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.math.floatprovider.UniformFloatProvider;
import net.pistonpoek.magicalscepter.entity.effect.ModStatusEffects;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Spells {
    public static final RegistryKey<Spell> MAGICAL_KEY = of("magical");
    public static final RegistryKey<Spell> BLAZE_KEY = of("blaze");
    public static final RegistryKey<Spell> BREEZE_KEY = of("breeze");
    public static final RegistryKey<Spell> DRAGON_KEY = of("dragon");
    public static final RegistryKey<Spell> EVOKER_KEY = of("evoker");
    public static final RegistryKey<Spell> GHAST_KEY = of("ghast");
    public static final RegistryKey<Spell> GUARDIAN_KEY = of("guardian");
    public static final RegistryKey<Spell> SHULKER_KEY = of("shulker");
    public static final RegistryKey<Spell> WARDEN_KEY = of("warden");
    public static final RegistryKey<Spell> WITHER_KEY = of("wither");

    public static final RegistryKey<Spell> DEFAULT_KEY = MAGICAL_KEY;

    private static RegistryKey<Spell> of(String id) {
        return RegistryKey.of(ModRegistryKeys.SPELL, ModIdentifier.of(id));
    }

    // Second spell sound effect
    public static final Spell MAGICAL = create(null,
            new PlaySoundEnchantmentEffect(
                    RegistryEntry.of(SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL),
                    ConstantFloatProvider.create(1.0F),
                    UniformFloatProvider.create(0.8F, 1.2F)),
            new ApplyMobEffectEnchantmentEffect(
                    RegistryEntryList.of(StatusEffects.RESISTANCE),
                    EnchantmentLevelBasedValue.constant(100.0F),
                    EnchantmentLevelBasedValue.constant(400.0F),
                    EnchantmentLevelBasedValue.constant(1.0F),
                    EnchantmentLevelBasedValue.constant(3.0F)
            )); // Effect (Resistance)

    public static final Spell BLAZE = create(
            new PlaySoundEnchantmentEffect(RegistryEntry.of(SoundEvents.ENTITY_BLAZE_SHOOT), ConstantFloatProvider.create(1.0F), UniformFloatProvider.create(0.8F, 1.2F)), null); // Effect (Fire resistance)

    public static final Spell BREEZE = create(
            new PlaySoundEnchantmentEffect(RegistryEntry.of(SoundEvents.ENTITY_BREEZE_SHOOT), ConstantFloatProvider.create(1.0F), UniformFloatProvider.create(0.8F, 1.2F)), null); // SoundEvents.ENTITY_BREEZE_JUMP

    public static final Spell DRAGON = create(
            new PlaySoundEnchantmentEffect(RegistryEntry.of(SoundEvents.ENTITY_ENDER_DRAGON_SHOOT), ConstantFloatProvider.create(1.0F), UniformFloatProvider.create(0.8F, 1.2F)), null); // SoundEvents.ENTITY_ENDER_DRAGON_GROWL

    public static final Spell EVOKER = create(
            new PlaySoundEnchantmentEffect(RegistryEntry.of(SoundEvents.ENTITY_EVOKER_CAST_SPELL), ConstantFloatProvider.create(1.0F), UniformFloatProvider.create(0.8F, 1.2F)), null); // SoundEvents.ENTITY_EVOKER_CAST_SPELL

    public static final Spell GHAST = create(
            new PlaySoundEnchantmentEffect(RegistryEntry.of(SoundEvents.ENTITY_GHAST_SHOOT), ConstantFloatProvider.create(1.0F), UniformFloatProvider.create(0.8F, 1.2F)), null); // Effect (Regeneration)

    public static final Spell GUARDIAN = create(
            new PlaySoundEnchantmentEffect(RegistryEntry.of(SoundEvents.ENTITY_GUARDIAN_ATTACK), ConstantFloatProvider.create(1.0F), UniformFloatProvider.create(0.8F, 1.2F)), null); // Effect (Haste)

    public static final Spell SHULKER = create(
            new PlaySoundEnchantmentEffect(RegistryEntry.of(SoundEvents.ENTITY_SHULKER_SHOOT), ConstantFloatProvider.create(1.0F), UniformFloatProvider.create(0.8F, 1.2F)), null); // SoundEvents.ENTITY_SHULKER_TELEPORT

    public static final Spell WARDEN = create(null,
            new PlaySoundEnchantmentEffect(
                    RegistryEntry.of(SoundEvents.ENTITY_WARDEN_SONIC_BOOM),
                    ConstantFloatProvider.create(1.0F),
                    UniformFloatProvider.create(0.8F, 1.2F)),
            new ApplyMobEffectEnchantmentEffect(
                    RegistryEntryList.of(ModStatusEffects.STABILITY),
                    EnchantmentLevelBasedValue.constant(100.0F),
                    EnchantmentLevelBasedValue.constant(400.0F),
                    EnchantmentLevelBasedValue.constant(1.0F),
                    EnchantmentLevelBasedValue.constant(5.0F)
            )); // Effect (Stability)

    public static final Spell WITHER = create(
            new PlaySoundEnchantmentEffect(RegistryEntry.of(SoundEvents.ENTITY_WITHER_SHOOT), ConstantFloatProvider.create(1.0F), UniformFloatProvider.create(0.8F, 1.2F)), null); // Effect (Anti wither)


    private static Spell create(EnchantmentEntityEffect effect,
                                LootCondition.Builder requirements) {
        return create(30, 100, effect, requirements);
    }

    private static Spell create(int experienceCost, int cooldown, EnchantmentEntityEffect effect, @Nullable LootCondition.Builder requirements) {
        return new Spell(experienceCost, cooldown,
                List.of(new EnchantmentEffectEntry<>(effect, Optional.ofNullable(requirements).map(LootCondition.Builder::build))));
    }

    private static Spell create(@Nullable LootCondition.Builder requirements, EnchantmentEntityEffect ... effect) {
        return create(30, 100, requirements, effect);
    }

    private static Spell create(int experienceCost, int cooldown,  @Nullable LootCondition.Builder requirements, EnchantmentEntityEffect ... effects) {
        List<EnchantmentEffectEntry<EnchantmentEntityEffect>> effectEntries = new ArrayList<>();
        for (EnchantmentEntityEffect effect: effects) {
            effectEntries.add(new EnchantmentEffectEntry<>(effect, Optional.ofNullable(requirements).map(LootCondition.Builder::build)));
        }

        return new Spell(experienceCost, cooldown, effectEntries);
    }

    public static final Spell DEFAULT = MAGICAL;

    public static void init() {
    }

}

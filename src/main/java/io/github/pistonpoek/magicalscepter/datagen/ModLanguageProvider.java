package io.github.pistonpoek.magicalscepter.datagen;

import io.github.pistonpoek.magicalscepter.command.SpellCommand;
import io.github.pistonpoek.magicalscepter.component.ScepterContentsComponent;
import io.github.pistonpoek.magicalscepter.component.ScepterExperienceComponent;
import io.github.pistonpoek.magicalscepter.enchantment.ModEnchantments;
import io.github.pistonpoek.magicalscepter.entity.ModEntityType;
import io.github.pistonpoek.magicalscepter.entity.effect.ModStatusEffects;
import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.registry.tag.ModBiomeTags;
import io.github.pistonpoek.magicalscepter.registry.tag.ModItemTags;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import io.github.pistonpoek.magicalscepter.sound.ModSoundEvents;
import io.github.pistonpoek.magicalscepter.spell.Spells;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static io.github.pistonpoek.magicalscepter.advancement.ModAdvancements.*;

/**
 * Mod data provider for language.
 */
public class ModLanguageProvider extends FabricLanguageProvider {
    /**
     * Construct a mod language provider for data generation.
     *
     * @param output           Data output to generate language data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    protected ModLanguageProvider(FabricDataOutput output,
                                  CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup,
                                     TranslationBuilder translationBuilder) {
        translationBuilder.add(ModStatusEffects.REPULSION.value(), "Repulsion");
        translationBuilder.add(ModStatusEffects.STABILITY.value(), "Stability");

        translationBuilder.addEnchantment(ModEnchantments.INSIGHT_KEY, "Insight");

        translationBuilder.add(ModEntityType.SORCERER, "Sorcerer");
        translationBuilder.add(ModEntityType.GUARDIAN_BOLT, "Guardian Bolt");
        translationBuilder.add(ModEntityType.SPELL_FIRE_CHARGE, "Spell Fire Charge");
        translationBuilder.add(ModEntityType.SPELL_FIREBALL, "Spell Fireball");
        translationBuilder.add(ModEntityType.SPELL_WITHER_SKULL, "Spell Wither Skull");

        translationBuilder.add(ModItems.ARCANE_SCEPTER, "Arcane Scepter");
        translationBuilder.add(ModItems.SCEPTER, "Scepter");
        translationBuilder.add(ModItems.MAGICAL_SCEPTER, "Magical Scepter");
        translationBuilder.add(ModItems.SORCERER_SPAWN_EGG, "Sorcerer Spawn Egg");

        translationBuilder.add(createTitleTranslationKey(CAST_SCEPTER), "Wizardry");
        translationBuilder.add(createDescriptionTranslationKey(CAST_SCEPTER),
                "Cast a Spell with a Magical Scepter");
        translationBuilder.add(createTitleTranslationKey(ALL_SCEPTER_INFUSIONS), "Copycat");
        translationBuilder.add(createDescriptionTranslationKey(ALL_SCEPTER_INFUSIONS),
                "Acquire each Scepter variant by infusing a Magical Scepter");

        translationBuilder.add(SpellCommand.CAST_FAILED_KEY,
                "Unable to cast spell for target");
        translationBuilder.add(SpellCommand.CAST_SUCCESS_MULTIPLE_KEY,
                "Casted spell %s for %s targets");
        translationBuilder.add(SpellCommand.CAST_SUCCESS_SINGLE_KEY,
                "Casted spell %s for %s");
        translationBuilder.add(SpellCommand.CLEAR_FAILED_KEY,
                "Target has no future spell casts to remove");
        translationBuilder.add(SpellCommand.CLEAR_SUCCESS_MULTIPLE_KEY,
                "Removed all future spell casts for %s targets");
        translationBuilder.add(SpellCommand.CLEAR_SUCCESS_SINGLE_KEY,
                "Removed all future spell casts for %s");

        final Function<RegistryKey<Scepter>, String> SCEPTER_TRANSLATION_KEY =
                (scepter) -> ModItems.MAGICAL_SCEPTER.getTranslationKey() + "." +
                        Scepters.getTranslationKey(scepter);
        translationBuilder.add(SCEPTER_TRANSLATION_KEY.apply(null), "Uncraftable Scepter");
        translationBuilder.add(SCEPTER_TRANSLATION_KEY.apply(Scepters.BLAZE_KEY), "Blaze Scepter");
        translationBuilder.add(SCEPTER_TRANSLATION_KEY.apply(Scepters.BREEZE_KEY), "Breeze Scepter");
        translationBuilder.add(SCEPTER_TRANSLATION_KEY.apply(Scepters.DRAGON_KEY), "Dragon Scepter");
        translationBuilder.add(SCEPTER_TRANSLATION_KEY.apply(Scepters.EVOKER_KEY), "Evoker Scepter");
        translationBuilder.add(SCEPTER_TRANSLATION_KEY.apply(Scepters.GHAST_KEY), "Ghast Scepter");
        translationBuilder.add(SCEPTER_TRANSLATION_KEY.apply(Scepters.GUARDIAN_KEY), "Guardian Scepter");
        translationBuilder.add(SCEPTER_TRANSLATION_KEY.apply(Scepters.MAGICAL_KEY), "Magical Scepter");
        translationBuilder.add(SCEPTER_TRANSLATION_KEY.apply(Scepters.SHULKER_KEY), "Shulker Scepter");
        translationBuilder.add(SCEPTER_TRANSLATION_KEY.apply(Scepters.WARDEN_KEY), "Warden Scepter");
        translationBuilder.add(SCEPTER_TRANSLATION_KEY.apply(Scepters.WITHER_KEY), "Wither Scepter");

        translationBuilder.add(ScepterContentsComponent.MISSING_SPELL_KEY, "No Spell");
        translationBuilder.add(ScepterContentsComponent.NO_SPELLS_KEY, "No Spells");
        translationBuilder.add(ScepterContentsComponent.ON_CAST_ATTACK_KEY, "On Attack:");
        translationBuilder.add(ScepterContentsComponent.ON_CAST_PROTECT_KEY, "On Protect:");

        translationBuilder.add(ScepterExperienceComponent.EXPERIENCE_KEY, "Experience: %s");

        translationBuilder.add(Spells.getTranslationKey(Spells.BLAZE_FIRE_CHARGES), "Fire Charges");
        translationBuilder.add(Spells.getTranslationKey(Spells.BREEZE_WIND_CHARGE_KEY), "Wind Charge");
        translationBuilder.add(Spells.getTranslationKey(Spells.BREEZE_JUMP_KEY), "Breeze Jump");
        translationBuilder.add(Spells.getTranslationKey(Spells.DRAGON_FIREBALL_KEY), "Dragon Fireball");
        translationBuilder.add(Spells.getTranslationKey(Spells.DRAGON_GROWL_KEY), "Dragon Growl");
        translationBuilder.add(Spells.getTranslationKey(Spells.EVOKER_FANG_LINE_KEY), "Evoke Fang Line");
        translationBuilder.add(Spells.getTranslationKey(Spells.EVOKER_FANG_CIRCLE_KEY), "Evoke Fang Circle");
        translationBuilder.add(Spells.getTranslationKey(Spells.GHAST_FIREBALL_KEY), "Fireball");
        translationBuilder.add(Spells.getTranslationKey(Spells.GUARDIAN_BOLT_KEY), "Guardian Bolt");
        translationBuilder.add(Spells.getTranslationKey(Spells.MAGICAL_ATTACK_KEY), "Magical Attack");
        translationBuilder.add(Spells.getTranslationKey(Spells.SHULKER_BULLET_KEY), "Levitation Bullet");
        translationBuilder.add(Spells.getTranslationKey(Spells.SHULKER_TELEPORT_KEY), "Teleport");
        translationBuilder.add(Spells.getTranslationKey(Spells.WARDEN_SONIC_BOOM_KEY), "Sonic Boom");
        translationBuilder.add(Spells.getTranslationKey(Spells.WITHER_SKULL_KEY), "Wither Skull");

        translationBuilder.add(ModSoundEvents.ENTITY_PARROT_IMITATE_SORCERER, "Parrot murmurs");
        translationBuilder.add(ModSoundEvents.ENTITY_SORCERER_AMBIENT, "Sorcerer murmurs");
        translationBuilder.add(ModSoundEvents.ENTITY_SORCERER_CELEBRATE, "Sorcerer cheers");
        translationBuilder.add(ModSoundEvents.ENTITY_SORCERER_DEATH, "Sorcerer dies");
        translationBuilder.add(ModSoundEvents.ENTITY_SORCERER_HURT, "Sorcerer hurts");
        translationBuilder.add(ModSoundEvents.ITEM_ARCANE_SCEPTER_COLLECT_EXPERIENCE, "Scepter charges");
        translationBuilder.add(ModSoundEvents.ITEM_ARCANE_SCEPTER_RELEASE_EXPERIENCE, "Scepter discharges");
        translationBuilder.add(ModSoundEvents.ITEM_MAGICAL_SCEPTER_CAST_ATTACK_SPELL, "Scepter casts");
        translationBuilder.add(ModSoundEvents.ITEM_MAGICAL_SCEPTER_CAST_PROTECT_SPELL, "Scepter shields");
        translationBuilder.add(ModSoundEvents.ITEM_MAGICAL_SCEPTER_INFUSE, "Magical Scepter infuses");

        translationBuilder.add(ModItemTags.SCEPTER_ENCHANTABLE, "Scepter Enchantable");
        translationBuilder.add(ModItemTags.SCEPTERS, "Scepters");
        translationBuilder.add(ModItemTags.SORCERER_PREFERRED_WEAPONS, "Sorcerer preferred weapons");
        translationBuilder.add(ModBiomeTags.OLD_TAIGA_CABIN_HAS_STRUCTURE, "Has Structure Old Taiga Cabin");
    }
}

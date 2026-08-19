package io.github.pistonpoek.magicalscepter.datagen;

import io.github.pistonpoek.magicalscepter.advancement.criterion.CastSpellCriterion;
import io.github.pistonpoek.magicalscepter.advancement.criterion.InfuseScepterCriterion;
import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static io.github.pistonpoek.magicalscepter.advancement.ModAdvancements.*;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.data.advancements.packs.VanillaAdvancementProvider
 */
public class ModAdvancementProvider extends FabricAdvancementProvider {
    /**
     * Construct a mod advancement provider for data generation.
     *
     * @param output           Data output to generate mod advancement data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    protected ModAdvancementProvider(FabricDataOutput output,
                                     CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider registryLookup, Consumer<AdvancementHolder> consumer) {
        HolderGetter<Scepter> scepterRegistryLookup = registryLookup.lookupOrThrow(ModRegistryKeys.SCEPTER);

        AdvancementHolder adventureRootAdvancementReference = AdvancementSubProvider.createPlaceholder("adventure/root");
        AdvancementHolder castScepterAdvancement = Advancement.Builder.advancement().parent(adventureRootAdvancementReference)
                .display(
                        ModItems.MAGICAL_SCEPTER,
                        createTitleText(CAST_SCEPTER),
                        createDescriptionText(CAST_SCEPTER),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("cast_scepter", CastSpellCriterion.Conditions.create(ModItems.MAGICAL_SCEPTER))
                .save(consumer, CAST_SCEPTER.toString());

        AdvancementHolder allScepterInfusionsAdvancement = requireListedSceptersInfused(
                Advancement.Builder.advancement().parent(castScepterAdvancement)
                        .display(
                                ScepterHelper.createMagicalScepter(scepterRegistryLookup.getOrThrow(Scepters.DRAGON_KEY)),
                                createTitleText(ALL_SCEPTER_INFUSIONS),
                                createDescriptionText(ALL_SCEPTER_INFUSIONS),
                                null,
                                AdvancementType.GOAL,
                                true,
                                true,
                                false
                        ),
                registryLookup,
                Scepters.ALL_INFUSED_SCEPTERS
        ).save(consumer, ALL_SCEPTER_INFUSIONS.toString());
    }

    /**
     * Extend the specified advancement builder with infuse scepter criterion for the list of specified scepters.
     *
     * @param builder        Advancement builder to extend with condition.
     * @param registryLookup Registry lookup to access registries with.
     * @param scepters       List of scepters that each add an infuse condition to the advancement builder.
     * @return Advancement builder that is extended with a criterion for each specified scepter.
     */
    protected static Advancement.Builder requireListedSceptersInfused(Advancement.Builder builder,
                                                                      HolderLookup.Provider registryLookup,
                                                                      List<ResourceKey<Scepter>> scepters
    ) {
        HolderGetter<Scepter> scepterRegistryLookup = registryLookup.lookupOrThrow(ModRegistryKeys.SCEPTER);
        for (ResourceKey<Scepter> registryKey : scepters) {
            builder.addCriterion(
                    registryKey.identifier().toString(),
                    InfuseScepterCriterion.Conditions.create(scepterRegistryLookup.getOrThrow(registryKey))
            );
        }

        return builder;
    }

    /**
     * Create an advancements title text with the specified advancement.
     *
     * @param advancement Identifier of an advancement to create translatable text for.
     * @return Translatable text for the title of the specified advancement.
     */
    protected static MutableComponent createTitleText(Identifier advancement) {
        return Component.translatable(createTitleTranslationKey(advancement));
    }

    /**
     * Create an advancements description text with the specified advancement.
     *
     * @param advancement Identifier of an advancement to create translatable text for.
     * @return Translatable text for the description of the specified advancement.
     */
    protected static MutableComponent createDescriptionText(Identifier advancement) {
        return Component.translatable(createDescriptionTranslationKey(advancement));
    }
}
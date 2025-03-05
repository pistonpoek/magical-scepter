package io.github.pistonpoek.magicalscepter.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.data.advancement.AdvancementTabGenerator;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import io.github.pistonpoek.magicalscepter.advancement.criterion.CastSpellCriterion;
import io.github.pistonpoek.magicalscepter.advancement.criterion.InfuseScepterCriterion;
import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.scepter.Scepter;
import io.github.pistonpoek.magicalscepter.scepter.ScepterHelper;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.data.advancement.vanilla.VanillaAdvancementProviders
 */
public class ModAdvancementProvider extends FabricAdvancementProvider {
    /**
     * Construct a mod advancement provider for data generation.
     *
     * @param output Data output to generate mod advancement data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    protected ModAdvancementProvider(FabricDataOutput output,
                                     CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup registryLookup, Consumer<AdvancementEntry> consumer) {
        RegistryEntryLookup<Scepter> scepterRegistryLookup = registryLookup.getOrThrow(ModRegistryKeys.SCEPTER);

        AdvancementEntry adventureRootAdvancementReference = AdvancementTabGenerator.reference("adventure/root");
        AdvancementEntry castScepterAdvancement = Advancement.Builder.create().parent(adventureRootAdvancementReference)
                .display(
                        ModItems.MAGICAL_SCEPTER,
                        ModIdentifier.translatable("advancements.adventure.cast_scepter.title"),
                        ModIdentifier.translatable("advancements.adventure.cast_scepter.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("cast_scepter", CastSpellCriterion.Conditions.create(ModItems.MAGICAL_SCEPTER))
                .build(consumer, ModIdentifier.identifier("adventure/cast_scepter"));

        AdvancementEntry allScepterInfusionsAdvancement = requireListedSceptersInfused(
                Advancement.Builder.create().parent(castScepterAdvancement)
                .display(
                        ScepterHelper.createMagicalScepter(scepterRegistryLookup.getOrThrow(Scepters.DRAGON_KEY)),
                        ModIdentifier.translatable("advancements.adventure.all_scepter_infusions.title"),
                        ModIdentifier.translatable("advancements.adventure.all_scepter_infusions.description"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                ),
                registryLookup,
                Scepters.ALL_INFUSED_SCEPTERS
        ).build(consumer, ModIdentifier.identifier("adventure/all_scepter_infusions"));
    }

    /**
     * Extend the specified advancement builder with infuse scepter criterion for the list of specified scepters.
     *
     * @param builder Advancement builder to extend with condition.
     * @param registryLookup Registry lookup to access registries with.
     * @param scepters List of scepters that each add an infuse condition to the advancement builder.
     * @return Advancement builder that is extended with a criterion for each specified scepter.
     */
    protected static Advancement.Builder requireListedSceptersInfused(Advancement.Builder builder,
                                                                      RegistryWrapper.WrapperLookup registryLookup,
                                                                      List<RegistryKey<Scepter>> scepters
    ) {
        RegistryEntryLookup<Scepter> scepterRegistryLookup = registryLookup.getOrThrow(ModRegistryKeys.SCEPTER);
        for (RegistryKey<Scepter> registryKey : scepters) {
            builder.criterion(
                    registryKey.getValue().toString(),
                    InfuseScepterCriterion.Conditions.create(scepterRegistryLookup.getOrThrow(registryKey))
            );
        }

        return builder;
    }
}
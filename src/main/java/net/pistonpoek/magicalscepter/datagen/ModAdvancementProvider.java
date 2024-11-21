package net.pistonpoek.magicalscepter.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.data.server.advancement.AdvancementTabGenerator;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.pistonpoek.magicalscepter.advancement.criteria.CastSpellCriterion;
import net.pistonpoek.magicalscepter.advancement.criteria.InfuseScepterCriterion;
import net.pistonpoek.magicalscepter.item.ModItems;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.pistonpoek.magicalscepter.scepter.Scepter;
import net.pistonpoek.magicalscepter.scepter.ScepterHelper;
import net.pistonpoek.magicalscepter.scepter.Scepters;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends FabricAdvancementProvider {
    protected ModAdvancementProvider(FabricDataOutput output,
                                     CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup registryLookup, Consumer<AdvancementEntry> consumer) {
        RegistryEntryLookup<Scepter> scepterRegistryLookup = registryLookup.createRegistryLookup().getOrThrow(ModRegistryKeys.SCEPTER);

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
                .criterion("cast_spell", CastSpellCriterion.Conditions.create(ModItems.MAGICAL_SCEPTER))
                .build(consumer, ModIdentifier.name("adventure/cast_spell"));

        AdvancementEntry allScepterInfusionsAdvancement = requireListedSceptersInfused(
                Advancement.Builder.create().parent(castScepterAdvancement)
                .display(
                        ScepterHelper.createScepter(scepterRegistryLookup.getOrThrow(Scepters.DRAGON_KEY)),
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
        ).build(consumer, ModIdentifier.name("adventure/all_scepter_infusions"));
    }

    protected static Advancement.Builder requireListedSceptersInfused(
            Advancement.Builder builder, RegistryWrapper.WrapperLookup registryLookup, List<RegistryKey<Scepter>> scepters
    ) {
        RegistryEntryLookup<Scepter> scepterRegistryLookup = registryLookup.getWrapperOrThrow(ModRegistryKeys.SCEPTER);
        for (RegistryKey<Scepter> registryKey : scepters) {
            builder.criterion(
                    registryKey.getValue().toString(),
                    InfuseScepterCriterion.Conditions.create(scepterRegistryLookup.getOrThrow(registryKey))
            );
        }

        return builder;
    }
}
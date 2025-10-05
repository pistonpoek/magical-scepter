package io.github.pistonpoek.magicalscepter.datagen.type;

import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import io.github.pistonpoek.magicalscepter.spell.Spells;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class SpellProvider extends FabricCodecDataProvider<Spell> {
    /**
     * Construct a mod spell provider for data generation.
     *
     * @param output           Data output to generate spell data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public SpellProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture, DataOutput.OutputType.DATA_PACK,
                ModRegistryKeys.directory(ModRegistryKeys.SPELL), Spell.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, Spell> provider, RegistryWrapper.WrapperLookup registries) {
        RegistryEntryLookup<Spell> spellLookup = registries.getOrThrow(ModRegistryKeys.SPELL);

        for (RegistryKey<Spell> spellKey : Spells.SPELL_KEYS) {
            addSpell(provider, spellLookup, spellKey);
        }
    }

    /**
     * Add a spell to the specified provider.
     *
     * @param provider Provider to add the spell to.
     * @param lookup   Registry entry lookup for the spell.
     * @param key      Registry key to add to the spell provider.
     */
    private static void addSpell(BiConsumer<Identifier, Spell> provider,
                                 RegistryEntryLookup<Spell> lookup,
                                 RegistryKey<Spell> key) {
        provider.accept(key.getValue(), lookup.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Spell";
    }
}

package io.github.pistonpoek.magicalscepter.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import io.github.pistonpoek.magicalscepter.spell.Spells;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class SpellTypeProvider extends FabricCodecDataProvider<Spell> {
    public SpellTypeProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture, DataOutput.OutputType.DATA_PACK, ModRegistryKeys.SPELL.getValue().toString().replace(":","/"), Spell.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, Spell> provider, RegistryWrapper.WrapperLookup lookup) {
        RegistryEntryLookup<Spell> spellLookup = lookup.createRegistryLookup().getOrThrow(ModRegistryKeys.SPELL);

        for (RegistryKey<Spell> spellKey: Spells.SPELL_KEYS) {
            addSpell(provider, spellLookup, spellKey);
        }
    }

    private static void addSpell(BiConsumer<Identifier, Spell> provider,
                                 RegistryEntryLookup<Spell> lookup,
                                 RegistryKey<Spell> key) {
        provider.accept(key.getValue(), lookup.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "SpellTypeProvider";
    }
}

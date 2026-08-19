package io.github.pistonpoek.magicalscepter.datagen.codec;

import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.spell.Spell;
import io.github.pistonpoek.magicalscepter.spell.Spells;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * Mod data provider for spells.
 *
 * @see Spells
 */
public class SpellProvider extends FabricCodecDataProvider<Spell> {
    /**
     * Construct a mod spell provider for data generation.
     *
     * @param output           Data output to generate spell data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public SpellProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture, PackOutput.Target.DATA_PACK,
                ModRegistryKeys.directory(ModRegistryKeys.SPELL), Spell.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, Spell> provider, HolderLookup.Provider registries) {
        HolderGetter<Spell> spellLookup = registries.lookupOrThrow(ModRegistryKeys.SPELL);

        for (ResourceKey<Spell> spellKey : Spells.KEYS) {
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
                                 HolderGetter<Spell> lookup,
                                 ResourceKey<Spell> key) {
        provider.accept(key.identifier(), lookup.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Spell";
    }
}

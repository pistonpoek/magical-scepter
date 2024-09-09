package net.pistonpoek.magicalscepter.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.pistonpoek.magicalscepter.scepter.Scepter;
import net.pistonpoek.magicalscepter.scepter.Scepters;
import net.pistonpoek.magicalscepter.spell.Spell;
import net.pistonpoek.magicalscepter.spell.Spells;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class SpellTypeProvider extends FabricCodecDataProvider<Spell> {
    public SpellTypeProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture, DataOutput.OutputType.DATA_PACK, ModRegistryKeys.SPELL.getValue().toString().replace(":","/"), Spell.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, Spell> provider, RegistryWrapper.WrapperLookup lookup) {
        provider.accept(Spells.EMPTY_KEY.getValue(), Spells.EMPTY);
    }

    @Override
    public String getName() {
        return "SpellTypeProvider";
    }
}

package net.pistonpoek.magicalscepter.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
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
        provider.accept(Spells.MAGICAL_KEY.getValue(), Spells.MAGICAL);
        provider.accept(Spells.BLAZE_KEY.getValue(), Spells.BLAZE);
        provider.accept(Spells.BREEZE_KEY.getValue(), Spells.BREEZE);
        provider.accept(Spells.DRAGON_KEY.getValue(), Spells.DRAGON);
        provider.accept(Spells.EVOKER_KEY.getValue(), Spells.EVOKER);
        provider.accept(Spells.GHAST_KEY.getValue(), Spells.GHAST);
        provider.accept(Spells.GUARDIAN_KEY.getValue(), Spells.GUARDIAN);
        provider.accept(Spells.SHULKER_KEY.getValue(), Spells.SHULKER);
        provider.accept(Spells.WARDEN_KEY.getValue(), Spells.WARDEN);
        provider.accept(Spells.WITHER_KEY.getValue(), Spells.WITHER);
    }

    @Override
    public String getName() {
        return "SpellTypeProvider";
    }
}

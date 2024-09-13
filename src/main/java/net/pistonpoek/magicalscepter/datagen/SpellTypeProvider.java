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
        provider.accept(Spells.BLAZE_SMALL_FIREBALL_KEY.getValue(), Spells.BLAZE_SMALL_FIREBALL);
        provider.accept(Spells.BLAZE_FIRE_RESISTANCE_KEY.getValue(), Spells.BLAZE_FIRE_RESISTANCE);
        provider.accept(Spells.BREEZE_WIND_CHARGE_KEY.getValue(), Spells.BREEZE_WIND_CHARGE);
        provider.accept(Spells.DRAGON_FIREBALL_KEY.getValue(), Spells.DRAGON_FIREBALL);
        provider.accept(Spells.EVOKER_FANG_LINE_KEY.getValue(), Spells.EVOKER);
        provider.accept(Spells.GHAST_FIREBALL_KEY.getValue(), Spells.GHAST_FIREBALL);
        provider.accept(Spells.GHAST_REGENERATION_KEY.getValue(), Spells.GHAST_REGENERATION);
        provider.accept(Spells.GUARDIAN_KEY.getValue(), Spells.GUARDIAN);
        provider.accept(Spells.SHULKER_BULLET_KEY.getValue(), Spells.SHULKER_BULLET);
        provider.accept(Spells.SHULKER_TELEPORT_KEY.getValue(), Spells.SHULKER_TELEPORT);
        provider.accept(Spells.WARDEN_SONIC_BOOM_KEY.getValue(), Spells.WARDEN_SONIC_BOOM);
        provider.accept(Spells.WARDEN_STABILITY_KEY.getValue(), Spells.WARDEN_STABILITY);
        provider.accept(Spells.WITHER_SKULL_KEY.getValue(), Spells.WITHER_SKULL);
    }

    @Override
    public String getName() {
        return "SpellTypeProvider";
    }
}

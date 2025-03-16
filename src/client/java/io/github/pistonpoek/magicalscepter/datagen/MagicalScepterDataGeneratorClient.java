package io.github.pistonpoek.magicalscepter.datagen;

import io.github.pistonpoek.magicalscepter.datagen.tag.*;
import io.github.pistonpoek.magicalscepter.datagen.type.DamageTypeProvider;
import io.github.pistonpoek.magicalscepter.datagen.type.EnchantmentProvider;
import io.github.pistonpoek.magicalscepter.datagen.type.ScepterProvider;
import io.github.pistonpoek.magicalscepter.datagen.type.SpellProvider;
import io.github.pistonpoek.magicalscepter.enchantment.ModEnchantments;
import io.github.pistonpoek.magicalscepter.entity.damage.ModDamageTypes;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import io.github.pistonpoek.magicalscepter.spell.Spells;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

@Environment(EnvType.CLIENT)
public class MagicalScepterDataGeneratorClient implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModModelProvider::new);

        pack.addProvider(ModBiomeTagProvider::new);
        pack.addProvider(ModItemTagProvider::new);
        pack.addProvider(ModEnchantmentTagProvider::new);
        pack.addProvider(ModEntityTypeTagProvider::new);
        pack.addProvider(ModGameEventTagProvider::new);
        pack.addProvider(ScepterTagProvider::new);

        pack.addProvider(DamageTypeProvider::new);
        pack.addProvider(EnchantmentProvider::new);
        pack.addProvider(ScepterProvider::new);
        pack.addProvider(SpellProvider::new);

        pack.addProvider(ModAdvancementProvider::new);
        pack.addProvider(ModRecipeProvider::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.DAMAGE_TYPE, ModDamageTypes::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.ENCHANTMENT, ModEnchantments::bootstrap);
        registryBuilder.addRegistry(ModRegistryKeys.SPELL, Spells::bootstrap);
        registryBuilder.addRegistry(ModRegistryKeys.SCEPTER, Scepters::bootstrap);
    }
}

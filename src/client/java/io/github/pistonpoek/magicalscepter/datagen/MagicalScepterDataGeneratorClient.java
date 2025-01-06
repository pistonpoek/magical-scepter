package io.github.pistonpoek.magicalscepter.datagen;

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
        pack.addProvider(ModDamageTypeProvider::new);

        pack.addProvider(ScepterTypeProvider::new);
        pack.addProvider(SpellTypeProvider::new);
        pack.addProvider(ModAdvancementProvider::new);
        pack.addProvider(ModBlockTagProvider::new);
        pack.addProvider(ModItemTagProvider::new);
        pack.addProvider(ModBiomeTagProvider::new);
        pack.addProvider(ModScepterTagProvider::new);
        pack.addProvider(ModBlockLootTableProvider::new);
        pack.addProvider(ModRecipeProvider::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.DAMAGE_TYPE, ModDamageTypes::bootstrap);
        registryBuilder.addRegistry(ModRegistryKeys.SPELL, Spells::bootstrap);
        registryBuilder.addRegistry(ModRegistryKeys.SCEPTER, Scepters::bootstrap);
    }
}

package io.github.pistonpoek.magicalscepter.datagen;

import io.github.pistonpoek.magicalscepter.datagen.codec.*;
import io.github.pistonpoek.magicalscepter.datagen.loot.ModEntityLootTableProvider;
import io.github.pistonpoek.magicalscepter.datagen.loot.ModLootTableProviders;
import io.github.pistonpoek.magicalscepter.datagen.tag.*;
import io.github.pistonpoek.magicalscepter.enchantment.ModEnchantments;
import io.github.pistonpoek.magicalscepter.entity.damage.ModDamageTypes;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.scepter.Scepters;
import io.github.pistonpoek.magicalscepter.spell.Spells;
import io.github.pistonpoek.magicalscepter.structure.ModStructureSets;
import io.github.pistonpoek.magicalscepter.structure.pool.ModStructurePools;
import io.github.pistonpoek.magicalscepter.world.gen.structure.ModStructures;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

@Environment(EnvType.CLIENT)
public class MagicalScepterDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModModelProvider::new);
        pack.addProvider(ModSoundsProvider::new);

        pack.addProvider(DamageTypeProvider::new);
        pack.addProvider(EnchantmentProvider::new);
        pack.addProvider(ScepterProvider::new);
        pack.addProvider(SpellProvider::new);
        pack.addProvider(StructurePoolProvider::new);
        pack.addProvider(StructureProvider::new);
        pack.addProvider(StructureSetProvider::new);

        pack.addProvider(ModLootTableProviders::new);
        pack.addProvider(ModEntityLootTableProvider::new);

        pack.addProvider(ModBiomeTagProvider::new);
        pack.addProvider(ModDamageTypeTagProvider::new);
        pack.addProvider(ModItemTagProvider::new);
        pack.addProvider(ModEnchantmentTagProvider::new);
        pack.addProvider(ModEntityTypeTagProvider::new);
        pack.addProvider(ModGameEventTagProvider::new);
        pack.addProvider(ScepterTagProvider::new);

        pack.addProvider(ModAdvancementProvider::new);
        pack.addProvider(ModLanguageProvider::new);
        pack.addProvider(ModRecipeProvider::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.DAMAGE_TYPE, ModDamageTypes::bootstrap);
        registryBuilder.add(Registries.ENCHANTMENT, ModEnchantments::bootstrap);
        registryBuilder.add(ModRegistryKeys.SPELL, Spells::bootstrap);
        registryBuilder.add(ModRegistryKeys.SCEPTER, Scepters::bootstrap);
        registryBuilder.add(Registries.TEMPLATE_POOL, ModStructurePools::bootstrap);
        registryBuilder.add(Registries.STRUCTURE_SET, ModStructureSets::bootstrap);
        registryBuilder.add(Registries.STRUCTURE, ModStructures::bootstrap);
    }
}

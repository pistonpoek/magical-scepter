package io.github.pistonpoek.magicalscepter.datagen.tag;

import io.github.pistonpoek.magicalscepter.entity.ModEntityType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.tag.vanilla.VanillaEntityTypeTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.data.tag.vanilla.VanillaEntityTypeTagProvider
 */
public class ModEntityTypeTagProvider extends VanillaEntityTypeTagProvider {
    /**
     * Construct a mod entity type tag provider for data generation.
     *
     * @param output           Data output to generate entity type tag data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ModEntityTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        this.builder(EntityTypeTags.ILLAGER)
                .add(ModEntityType.SORCERER);
        this.builder(EntityTypeTags.REDIRECTABLE_PROJECTILE)
                .add(ModEntityType.SPELL_FIREBALL);
        this.builder(EntityTypeTags.IMPACT_PROJECTILES)
                .add(ModEntityType.SPELL_DRAGON_FIREBALL)
                .add(ModEntityType.SPELL_FIRE_CHARGE)
                .add(ModEntityType.SPELL_FIREBALL)
                .add(ModEntityType.SPELL_WITHER_SKULL);
    }
}

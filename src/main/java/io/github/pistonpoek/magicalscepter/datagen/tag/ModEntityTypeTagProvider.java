package io.github.pistonpoek.magicalscepter.datagen.tag;

import io.github.pistonpoek.magicalscepter.entity.ModEntityTypeIds;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.data.tags.EntityTypeTagsProvider
 */
public class ModEntityTypeTagProvider extends EntityTypeTagsProvider {
    /**
     * Construct a mod entity type tag provider for data generation.
     *
     * @param output           Data output to generate entity type tag data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ModEntityTypeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        this.tag(EntityTypeTags.ILLAGER)
                .add(ModEntityTypeIds.SORCERER);
        this.tag(EntityTypeTags.REDIRECTABLE_PROJECTILE)
                .add(ModEntityTypeIds.SPELL_FIREBALL);
        this.tag(EntityTypeTags.IMPACT_PROJECTILES)
                .add(ModEntityTypeIds.SPELL_DRAGON_FIREBALL)
                .add(ModEntityTypeIds.SPELL_FIRE_CHARGE)
                .add(ModEntityTypeIds.SPELL_FIREBALL)
                .add(ModEntityTypeIds.SPELL_WITHER_SKULL);
    }
}

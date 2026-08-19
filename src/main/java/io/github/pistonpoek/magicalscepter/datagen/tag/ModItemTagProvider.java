package io.github.pistonpoek.magicalscepter.datagen.tag;

import io.github.pistonpoek.magicalscepter.item.ModItemIds;
import io.github.pistonpoek.magicalscepter.registry.tag.ModItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.VanillaItemTagsProvider;
import net.minecraft.references.ItemIds;
import net.minecraft.tags.ItemTags;

import java.util.concurrent.CompletableFuture;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.data.tags.VanillaItemTagsProvider
 */
public class ModItemTagProvider extends VanillaItemTagsProvider {
    /**
     * Construct a mod item tag provider for data generation.
     *
     * @param output           Data output to generate item tag data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        this.tag(ModItemTags.SORCERER_PREFERRED_WEAPONS)
                .add(ModItemIds.SCEPTER)
                .add(ModItemIds.MAGICAL_SCEPTER);

        this.tag(ModItemTags.SCEPTERS)
                .add(ModItemIds.SCEPTER)
                .add(ModItemIds.MAGICAL_SCEPTER)
                .addTag(ModItemTags.ARCANE_SCEPTERS);

        this.tag(ModItemTags.ARCANE_SCEPTERS)
                .add(ModItemIds.ARCANE_SCEPTER)
                .add(ModItemIds.CHARGED_ARCANE_SCEPTER);

        this.tag(ModItemTags.SCEPTER_MATERIALS)
                .add(ItemIds.LAPIS_LAZULI);

        this.tag(ModItemTags.SCEPTER_ENCHANTABLE)
                .addTag(ModItemTags.SCEPTERS);

        this.tag(ItemTags.DURABILITY_ENCHANTABLE)
                .addTag(ModItemTags.SCEPTERS);

        this.tag(ItemTags.VANISHING_ENCHANTABLE)
                .addTag(ModItemTags.SCEPTERS);
    }
}
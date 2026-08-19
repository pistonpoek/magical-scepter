package io.github.pistonpoek.magicalscepter.datagen.tag;

import io.github.pistonpoek.magicalscepter.enchantment.ModEnchantments;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.VanillaEnchantmentTagsProvider;
import net.minecraft.tags.EnchantmentTags;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.data.tags.VanillaEnchantmentTagsProvider
 */
public class ModEnchantmentTagProvider extends VanillaEnchantmentTagsProvider {
    /**
     * Construct a mod enchantment tag provider for data generation.
     *
     * @param output           Data output to generate enchantment tag data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ModEnchantmentTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        this.tag(EnchantmentTags.NON_TREASURE)
                .add(ModEnchantments.INSIGHT_KEY);
    }
}

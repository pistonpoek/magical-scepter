package io.github.pistonpoek.magicalscepter.datagen.tag;

import io.github.pistonpoek.magicalscepter.registry.tag.ModDamageTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.world.damagesource.DamageTypes;
import java.util.concurrent.CompletableFuture;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.data.tags.DamageTypeTagsProvider
 */
public class ModDamageTypeTagProvider extends DamageTypeTagsProvider {
    /**
     * Construct a mod damage type provider for data generation.
     *
     * @param output           Data output to generate damage type tag data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ModDamageTypeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        this.getOrCreateRawBuilder(ModDamageTypeTags.BYPASSES_REPULSION)
                .addElement(DamageTypes.GENERIC_KILL.identifier())
                .addElement(DamageTypes.FELL_OUT_OF_WORLD.identifier());
        this.getOrCreateRawBuilder(ModDamageTypeTags.BYPASSES_STABILITY)
                .addElement(DamageTypes.GENERIC_KILL.identifier())
                .addElement(DamageTypes.FELL_OUT_OF_WORLD.identifier());

        this.getOrCreateRawBuilder(ModDamageTypeTags.BLAZE_INFUSION)
                .addElement(DamageTypes.FIREBALL.identifier());
        this.getOrCreateRawBuilder(ModDamageTypeTags.BREEZE_INFUSION)
                .addElement(DamageTypes.WIND_CHARGE.identifier());
        this.getOrCreateRawBuilder(ModDamageTypeTags.DRAGON_INFUSION)
                .addElement(DamageTypes.INDIRECT_MAGIC.identifier());
        this.getOrCreateRawBuilder(ModDamageTypeTags.EVOKER_INFUSION)
                .addElement(DamageTypes.INDIRECT_MAGIC.identifier());
        this.getOrCreateRawBuilder(ModDamageTypeTags.GHAST_INFUSION)
                .addElement(DamageTypes.FIREBALL.identifier());
        this.getOrCreateRawBuilder(ModDamageTypeTags.GUARDIAN_INFUSION)
                .addElement(DamageTypes.INDIRECT_MAGIC.identifier());
        this.getOrCreateRawBuilder(ModDamageTypeTags.SHULKER_INFUSION)
                .addElement(DamageTypes.MOB_PROJECTILE.identifier());
        this.getOrCreateRawBuilder(ModDamageTypeTags.WARDEN_INFUSION)
                .addElement(DamageTypes.SONIC_BOOM.identifier());
        this.getOrCreateRawBuilder(ModDamageTypeTags.WITHER_INFUSION)
                .addElement(DamageTypes.WITHER_SKULL.identifier());
    }
}
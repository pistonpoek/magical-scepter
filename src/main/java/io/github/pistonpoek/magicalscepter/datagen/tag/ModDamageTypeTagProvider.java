package io.github.pistonpoek.magicalscepter.datagen.tag;

import io.github.pistonpoek.magicalscepter.registry.tag.ModDamageTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.tag.vanilla.VanillaDamageTypeTagProvider;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.data.tag.vanilla.VanillaDamageTypeTagProvider
 */
public class ModDamageTypeTagProvider extends VanillaDamageTypeTagProvider {
    /**
     * Construct a mod damage type provider for data generation.
     *
     * @param output           Data output to generate damage type tag data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ModDamageTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getTagBuilder(ModDamageTypeTags.BYPASSES_REPULSION)
                .add(DamageTypes.GENERIC_KILL.getValue())
                .add(DamageTypes.OUT_OF_WORLD.getValue());
        this.getTagBuilder(ModDamageTypeTags.BYPASSES_STABILITY)
                .add(DamageTypes.GENERIC_KILL.getValue())
                .add(DamageTypes.OUT_OF_WORLD.getValue());

        this.getTagBuilder(ModDamageTypeTags.BLAZE_INFUSION)
                .add(DamageTypes.FIREBALL.getValue());
        this.getTagBuilder(ModDamageTypeTags.BREEZE_INFUSION)
                .add(DamageTypes.WIND_CHARGE.getValue());
        this.getTagBuilder(ModDamageTypeTags.DRAGON_INFUSION)
                .add(DamageTypes.INDIRECT_MAGIC.getValue());
        this.getTagBuilder(ModDamageTypeTags.EVOKER_INFUSION)
                .add(DamageTypes.INDIRECT_MAGIC.getValue());
        this.getTagBuilder(ModDamageTypeTags.GHAST_INFUSION)
                .add(DamageTypes.FIREBALL.getValue());
        this.getTagBuilder(ModDamageTypeTags.GUARDIAN_INFUSION)
                .add(DamageTypes.INDIRECT_MAGIC.getValue());
        this.getTagBuilder(ModDamageTypeTags.SHULKER_INFUSION)
                .add(DamageTypes.MOB_PROJECTILE.getValue());
        this.getTagBuilder(ModDamageTypeTags.WARDEN_INFUSION)
                .add(DamageTypes.SONIC_BOOM.getValue());
        this.getTagBuilder(ModDamageTypeTags.WITHER_INFUSION)
                .add(DamageTypes.WITHER_SKULL.getValue());
    }
}
package io.github.pistonpoek.magicalscepter.datagen;

import io.github.pistonpoek.magicalscepter.sound.ModSoundEvents;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundEvent;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Mod data provider for sounds.
 */
@Environment(EnvType.CLIENT)
public class ModSoundsProvider extends FabricSoundsProvider {
    /**
     * Construct a sounds provider for data generation.
     *
     * @param output           Data output to generate sounds data into.
     * @param registriesFuture Registry lookup to initialize the data provider with.
     */
    public ModSoundsProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    /**
     * Create a sound type entry builder for the specified path.
     *
     * @param path String path to create sound type entry builder with.
     * @return Constructed sound type entry builder for the specified path.
     */
    public static SoundTypeBuilder.EntryBuilder of(String path) {
        return SoundTypeBuilder.EntryBuilder.ofFile(ModIdentifier.of(path));
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, SoundExporter exporter) {
        addSound(exporter, ModSoundEvents.ENTITY_PARROT_IMITATE_SORCERER, 
                builder -> builder
                        .sound(SoundTypeBuilder.EntryBuilder.ofEvent(ModSoundEvents.ENTITY_SORCERER_AMBIENT))
        );
        addSound(exporter, ModSoundEvents.ENTITY_SORCERER_AMBIENT,
                builder -> builder
                        .sound(of("sorcerer_idle"), 4)
        );
        addSound(exporter, ModSoundEvents.ENTITY_SORCERER_CELEBRATE,
                builder -> builder
                        .sound(of("sorcerer_idle"), 4)
        );
        addSound(exporter, ModSoundEvents.ENTITY_SORCERER_DEATH,
                builder -> builder
                        .sound(of("sorcerer_death2"))
        );
        addSound(exporter, ModSoundEvents.ENTITY_SORCERER_HURT,
                builder -> builder
                        .sound(of("sorcerer_hurt"), 3)
        );
        addSound(exporter, ModSoundEvents.ITEM_ARCANE_SCEPTER_COLLECT_EXPERIENCE,
                builder -> builder
                        .sound(of("arcane_scepter_charges"))
        );
        addSound(exporter, ModSoundEvents.ITEM_ARCANE_SCEPTER_RELEASE_EXPERIENCE,
                builder -> builder
                        .sound(of("arcane_scepter_discharges"))
        );
        addSound(exporter, ModSoundEvents.ITEM_MAGICAL_SCEPTER_CAST_ATTACK_SPELL,
                builder -> builder
                        .sound(of("magical_scepter_cast"), 2)
        );
        addSound(exporter, ModSoundEvents.ITEM_MAGICAL_SCEPTER_CAST_PROTECT_SPELL,
                builder -> builder
                        .sound(of("magical_scepter_shield1"))
        );
        addSound(exporter, ModSoundEvents.ITEM_MAGICAL_SCEPTER_INFUSE,
                builder -> builder
                        .sound(of("magical_scepter_infuse"), 4)
        );
    }

    /**
     * Add a sound to the specified exporter for the specified event and type builder.
     *
     * @param exporter Sound exporter to export to.
     * @param sound Sound event to add a sound for.
     * @param builder Sound type builder to specify sound settings.
     */
    private void addSound(SoundExporter exporter, SoundEvent sound,
                          Function<SoundTypeBuilder, SoundTypeBuilder> builder) {
        exporter.add(sound, builder.apply(SoundTypeBuilder.of(sound)));
    }

    @Override
    public String getName() {
        return "Sounds";
    }
}

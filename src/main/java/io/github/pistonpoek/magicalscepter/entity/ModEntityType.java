package io.github.pistonpoek.magicalscepter.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import io.github.pistonpoek.magicalscepter.MagicalScepter;
import io.github.pistonpoek.magicalscepter.entity.mob.RefractorEntity;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class ModEntityType {
    public static void init() {
        MagicalScepter.LOGGER.info("Registering Entities for " + ModIdentifier.MOD_NAME);
    }

    public static final EntityType<RefractorEntity> REFRACTOR = register(
        "refractor",
        EntityType.Builder.create(RefractorEntity::new, SpawnGroup.MONSTER)
                .dimensions(0.6F, 1.95F)
                .passengerAttachments(2.0F)
                .vehicleAttachment(-0.6F)
                .maxTrackingRange(8)
        );

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, ModIdentifier.of(id),
                type.build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, ModIdentifier.of(id))));
    }
}

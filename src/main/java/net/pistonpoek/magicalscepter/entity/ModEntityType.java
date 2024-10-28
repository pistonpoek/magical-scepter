package net.pistonpoek.magicalscepter.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.pistonpoek.magicalscepter.MagicalScepter;
import net.pistonpoek.magicalscepter.entity.mob.RefractorEntity;
import net.pistonpoek.magicalscepter.registry.ModIdentifier;

public class ModEntityType {
    public static void registerModEntities() {
        MagicalScepter.LOGGER.info("Registering Entities for " + ModIdentifier.MOD_ID);
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
        return Registry.register(Registries.ENTITY_TYPE, ModIdentifier.of(id), type.build(id));
    }
}

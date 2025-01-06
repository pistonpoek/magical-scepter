package io.github.pistonpoek.magicalscepter.entity;

import io.github.pistonpoek.magicalscepter.entity.projectile.GuardianBoltEntity;
import io.github.pistonpoek.magicalscepter.mixson.MixsonEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.WindChargeEntity;
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

    public static final EntityType<RefractorEntity> REFRACTOR = registerMob(
        "refractor",
        EntityType.Builder.create(RefractorEntity::new, SpawnGroup.MONSTER)
                .dimensions(0.6F, 1.95F)
                .passengerAttachments(2.0F)
                .vehicleAttachment(-0.6F)
                .maxTrackingRange(8)
    );
    public static final EntityType<GuardianBoltEntity> GUARDIAN_BOLT = register(
            "guardian_bolt",
            EntityType.Builder.create(GuardianBoltEntity::new, SpawnGroup.MISC)
                    .dropsNothing()
                    .dimensions(0.3125F, 0.3125F)
                    .eyeHeight(0.0F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(10)
    );

    private static <T extends Entity> EntityType<T> registerMob(String id, EntityType.Builder<T> type) {
        EntityType<T> entityType = register(id, type);
        MixsonEvents.registerMobModification(ModIdentifier.of(id));
        return entityType;
    }

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, ModIdentifier.of(id),
                type.build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, ModIdentifier.of(id))));
    }
}

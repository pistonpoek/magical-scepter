package io.github.pistonpoek.magicalscepter.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import io.github.pistonpoek.magicalscepter.registry.ModIdentifier;

import java.util.HashSet;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class ModEntityModelLayers {
    private static final Set<EntityModelLayer> LAYERS = new HashSet<>();
    public static final EntityModelLayer REFRACTOR = registerMain("refractor");
    public static final EntityModelLayer GUARDIAN_BOLT = registerMain("guardian_bolt");

    private static EntityModelLayer registerMain(String id) {
        return register(id, "main");
    }

    private static EntityModelLayer register(String id, String layer) {
        EntityModelLayer entityModelLayer = create(id, layer);
        if (!LAYERS.add(entityModelLayer)) {
            throw new IllegalStateException("Duplicate registration for " + entityModelLayer);
        } else {
            return entityModelLayer;
        }
    }

    private static EntityModelLayer create(String id, String layer) {
        return new EntityModelLayer(ModIdentifier.of(id), layer);
    }
}

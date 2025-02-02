package io.github.pistonpoek.magicalscepter.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.client.render.entity.model.EntityModelLayers
 */
@Environment(EnvType.CLIENT)
public class ModEntityModelLayers {
    private static final Set<EntityModelLayer> LAYERS = new HashSet<>();
    public static final EntityModelLayer REFRACTOR = registerMain("refractor");
    public static final EntityModelLayer GUARDIAN_BOLT = registerMain("guardian_bolt");

    /**
     * Register a new entity model on the main layer for the specified id.
     *
     * @param id String to use as id of the entity model.
     * @return Entity model layer that has been registered with the id.
     */
    private static @NotNull EntityModelLayer registerMain(String id) {
        return register(id, "main");
    }

    /**
     * Register a new entity model for the specified layer and id.
     *
     * @param id String to use as id of the entity model.
     * @param layer String to get layer for.
     * @return Entity model layer that has been registered at the layer with the id.
     * @throws IllegalStateException When an id has already been registered for the layer.
     */
    private static @NotNull EntityModelLayer register(String id, String layer) {
        EntityModelLayer entityModelLayer = new EntityModelLayer(ModIdentifier.of(id), layer);
        if (!LAYERS.add(entityModelLayer)) {
            throw new IllegalStateException("Duplicate registration for " + entityModelLayer);
        } else {
            return entityModelLayer;
        }
    }
}

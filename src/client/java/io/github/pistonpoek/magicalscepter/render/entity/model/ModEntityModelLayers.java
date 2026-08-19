package io.github.pistonpoek.magicalscepter.render.entity.model;

import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayerLocation;
import java.util.HashSet;
import java.util.Set;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.client.model.geom.ModelLayers
 */
@Environment(EnvType.CLIENT)
public class ModEntityModelLayers {
    private static final Set<ModelLayerLocation> LAYERS = new HashSet<>();
    public static final ModelLayerLocation SORCERER = registerMain("sorcerer");
    public static final ModelLayerLocation WITHER_SKULL = registerMain("wither_skull");

    /**
     * Register a new entity model on the main layer for the specified name.
     *
     * @param name String to use as path of the entity model.
     * @return Entity model layer that has been registered with the name.
     */
    private static ModelLayerLocation registerMain(String name) {
        return register(name, "main");
    }

    /**
     * Register a new entity model for the specified layer and name.
     *
     * @param name  String to use as name of the entity model.
     * @param layer String to get layer for.
     * @return Entity model layer that has been registered at the layer with the name.
     * @throws IllegalStateException When a name has already been registered for the layer.
     */
    private static ModelLayerLocation register(String name, String layer) {
        ModelLayerLocation entityModelLayer = new ModelLayerLocation(ModIdentifier.of(name), layer);
        if (!LAYERS.add(entityModelLayer)) {
            throw new IllegalStateException("Duplicate registration for " + entityModelLayer);
        } else {
            return entityModelLayer;
        }
    }
}

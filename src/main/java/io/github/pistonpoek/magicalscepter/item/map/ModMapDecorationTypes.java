package io.github.pistonpoek.magicalscepter.item.map;

import io.github.pistonpoek.magicalscepter.item.ModItems;
import io.github.pistonpoek.magicalscepter.mixson.MixsonEvents;
import io.github.pistonpoek.magicalscepter.registry.tag.ModStructureTags;
import io.github.pistonpoek.magicalscepter.util.ModIdentifier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.world.level.saveddata.maps.MapDecorationTypes
 */
public class ModMapDecorationTypes {
    public static final Holder<MapDecorationType> OLD_TAIGA_CABIN = register("old_taiga_cabin", "old_taiga_cabin", true, false);

    /**
     * Initialize the class for the static fields.
     */
    public static void init() {
        MixsonEvents.registerAbandonedCampSecretMap(ModStructureTags.ON_OLD_TAIGA_CABIN_MAPS,
                OLD_TAIGA_CABIN, ModItems.OLD_TAIGA_CABIN_MAP);
    }

    /**
     * Register a map decoration type with a mod identifier.
     *
     * @param name Name of the map decoration type.
     * @param assetName Asset name of the map decoration type.
     * @param showOnItemFrame Truth assignment, if the map decoration should show on an item frame.
     * @param trackCount Truth assignment, if map decoration should keep track of the amount of times displayed on a map.
     * @return Map decoration type created using the mod namespace.
     */
    private static Holder<MapDecorationType> register(final String name, final String assetName, final boolean showOnItemFrame, final boolean trackCount) {
        ResourceKey<MapDecorationType> key = ResourceKey.create(Registries.MAP_DECORATION_TYPE, ModIdentifier.of(name));
        MapDecorationType type = new MapDecorationType(ModIdentifier.of(assetName), showOnItemFrame, trackCount);
        return Registry.registerForHolder(BuiltInRegistries.MAP_DECORATION_TYPE, key, type);
    }
}

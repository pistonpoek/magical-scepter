package io.github.pistonpoek.magicalscepter.entity;

import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.Heightmap;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 * @see net.minecraft.entity.SpawnRestriction
 */
public class ModSpawnRestriction {
    static {
        SpawnRestriction.register(ModEntityType.REFRACTOR, SpawnLocationTypes.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
    }

    /**
     * Initialize the class for the static fields.
     */
    public static void init() {

    }
}

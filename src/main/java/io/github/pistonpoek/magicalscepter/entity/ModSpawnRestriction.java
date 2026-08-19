package io.github.pistonpoek.magicalscepter.entity;

import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * Mod specific class that provides similar functionality to respective vanilla class.
 *
 * @see net.minecraft.world.entity.SpawnPlacements
 */
public class ModSpawnRestriction {
    static {
        SpawnPlacements.register(ModEntityTypes.SORCERER, SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
    }

    /**
     * Initialize the class for the static fields.
     */
    public static void init() {

    }
}

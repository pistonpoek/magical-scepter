package io.github.pistonpoek.magicalscepter.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

/**
 * Item that has an attack functionality for the player to perform.
 */
public interface AttackItem {
    /**
     * Perform the attack functionality for the item
     *
     * @param world World that the user performs the attack in.
     * @param user  Player entity that is to perform the attack.
     * @return Action result of the attack.
     */
    ActionResult attack(World world, PlayerEntity user);
}

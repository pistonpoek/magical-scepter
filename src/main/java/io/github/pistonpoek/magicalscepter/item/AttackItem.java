package io.github.pistonpoek.magicalscepter.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

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
    InteractionResult attack(Level world, Player user);
}

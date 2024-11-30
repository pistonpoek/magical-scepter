package io.github.pistonpoek.magicalscepter.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

public interface AttackItem {
    ActionResult attack(World world, PlayerEntity user);
}

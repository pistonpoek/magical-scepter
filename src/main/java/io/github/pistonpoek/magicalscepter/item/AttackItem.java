package io.github.pistonpoek.magicalscepter.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public interface AttackItem {
    TypedActionResult<ItemStack> attack(World world, PlayerEntity user);
    boolean preventAttack(PlayerEntity user);
}

package io.github.pistonpoek.magicalscepter.util;

import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * Class to help add vanilla spawn egg dispenser behavior.
 *
 * @see DispenseItemBehavior#bootStrap()
 */
public class SpawnEggDispenserBehavior {
    /**
     * Register the vanilla spawn egg dispenser behavior for the specified spawn egg.
     *
     * @see DispenseItemBehavior#bootStrap()
     */
    public static void addSpawnEggDispenserBehavior(Item spawnEgg) {
        DispenserBlock.registerBehavior(spawnEgg, spawnEggDispenserBehavior);
    }

    /**
     * Vanilla spawn egg dispenser behavior.
     *
     * @see DispenseItemBehavior#bootStrap()
     */
    private static final DefaultDispenseItemBehavior spawnEggDispenserBehavior = new DefaultDispenseItemBehavior() {
        @Override
        public ItemStack execute(BlockSource pointer, ItemStack stack) {
            Direction direction = pointer.state().getValue(DispenserBlock.FACING);
            EntityType<?> entityType = ((SpawnEggItem)stack.getItem()).getType(stack);
            if (entityType == null) {
                return stack;
            }

            try {
                entityType.spawn(pointer.level(), stack, null, pointer.pos().relative(direction),
                        EntitySpawnReason.DISPENSER, direction != Direction.UP, false);
            } catch (Exception exception) {
                LOGGER.error("Error while dispensing spawn egg from dispenser at {}", pointer.pos(), exception);
                return ItemStack.EMPTY;
            }

            stack.shrink(1);
            pointer.level().gameEvent(null, GameEvent.ENTITY_PLACE, pointer.pos());
            return stack;
        }
    };
}

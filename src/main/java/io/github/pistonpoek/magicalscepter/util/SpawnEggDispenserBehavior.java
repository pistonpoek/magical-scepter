package io.github.pistonpoek.magicalscepter.util;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;
import net.minecraft.world.event.GameEvent;

/**
 * Class to help add vanilla spawn egg dispenser behavior.
 *
 * @see DispenserBehavior#registerDefaults()
 */
public class SpawnEggDispenserBehavior {
    /**
     * Register the vanilla spawn egg dispenser behavior for the specified spawn egg.
     *
     * @see DispenserBehavior#registerDefaults()
     */
    public static void addSpawnEggDispenserBehavior(Item spawnEgg) {
        DispenserBlock.registerBehavior(spawnEgg, spawnEggDispenserBehavior);
    }

    /**
     * Vanilla spawn egg dispenser behavior.
     *
     * @see DispenserBehavior#registerDefaults()
     */
    private static final ItemDispenserBehavior spawnEggDispenserBehavior = new ItemDispenserBehavior() {
        @Override
        public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            Direction direction = pointer.state().get(DispenserBlock.FACING);
            EntityType<?> entityType = ((SpawnEggItem)stack.getItem()).getEntityType(stack);
            if (entityType == null) {
                return stack;
            }

            try {
                entityType.spawnFromItemStack(pointer.world(), stack, null, pointer.pos().offset(direction),
                        SpawnReason.DISPENSER, direction != Direction.UP, false);
            } catch (Exception exception) {
                LOGGER.error("Error while dispensing spawn egg from dispenser at {}", pointer.pos(), exception);
                return ItemStack.EMPTY;
            }

            stack.decrement(1);
            pointer.world().emitGameEvent(null, GameEvent.ENTITY_PLACE, pointer.pos());
            return stack;
        }
    };
}

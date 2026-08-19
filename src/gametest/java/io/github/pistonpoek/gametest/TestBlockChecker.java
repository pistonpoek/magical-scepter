package io.github.pistonpoek.gametest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.BlockBasedTestInstance;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TestBlock;
import net.minecraft.world.level.block.entity.TestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.TestBlockMode;

/**
 * Check for test blocks in game tests, which allows {@link FunctionGameTestInstance} to have active {@link TestBlock}'s.
 *
 * @see BlockBasedTestInstance
 *
 * @param context Test context to activate test blocks for.
 */
public record TestBlockChecker(GameTestHelper context) {

    /**
     * Trigger start test blocks and activate the use of other test block modes.
     */
    public void start() {
        // Make each start test block give a restone pulse.
        for (BlockPos blockPos : findTestBlocks(TestBlockMode.START)) {
            context.getBlockEntity(blockPos, TestBlockEntity.class).trigger();
        }

        // Make all other test blocks function for the remaining ticks of the test.
        context.onEachTick(() -> {
            handleTrigger(TestBlockMode.ACCEPT, testBlockEntity -> context.succeed());
            handleTrigger(TestBlockMode.FAIL, testBlockEntity ->
                    context.fail(Component.literal(testBlockEntity.getMessage())));
            handleTrigger(TestBlockMode.LOG, TestBlockEntity::trigger);
        });
    }

    /**
     * Find test blocks in the test context.
     *
     * @param mode Mode of the test blocks to find.
     * @return List of block positions for the found test blocks.
     */
    public List<BlockPos> findTestBlocks(TestBlockMode mode) {
        List<BlockPos> list = new ArrayList<>();
        context.forEveryBlockInStructure(pos -> {
            BlockState blockState = context.getBlockState(pos);
            if (blockState.is(Blocks.TEST_BLOCK) && blockState.getValue(TestBlock.MODE) == mode) {
                list.add(pos.immutable());
            }
        });
        return list;
    }

    /**
     * Get the test block entity at the specified block position.
     *
     * @param pos Block position to get the test block entity at.
     * @return Test block entity that is retrieved.
     */
    private TestBlockEntity getTestBlockEntity(BlockPos pos) {
        return context.getBlockEntity(pos, TestBlockEntity.class);
    }

    /**
     * Handle a trigger for a test block entity of a specific test block mode.
     *
     * @param mode Mode of the test block to trigger the entity with.
     * @param callback Consumer that will be applied with the test block entity from the specified position.
     */
    private void handleTrigger(TestBlockMode mode, Consumer<TestBlockEntity> callback) {
        for (BlockPos blockPos : findTestBlocks(mode)) {
            TestBlockEntity testBlockEntity = getTestBlockEntity(blockPos);
            if (testBlockEntity.hasTriggered()) {
                callback.accept(testBlockEntity);
                testBlockEntity.reset();
            }
        }
    }
}

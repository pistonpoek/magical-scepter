package io.github.pistonpoek.gametest;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TestBlock;
import net.minecraft.block.entity.TestBlockEntity;
import net.minecraft.block.enums.TestBlockMode;
import net.minecraft.test.BlockBasedTestInstance;
import net.minecraft.test.FunctionTestInstance;
import net.minecraft.test.TestContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Check for test blocks in game tests, which allows {@link FunctionTestInstance} to have active {@link TestBlock}'s.
 *
 * @see BlockBasedTestInstance
 *
 * @param context Test context to activate test blocks for.
 */
public record TestBlockChecker(TestContext context) {

    /**
     * Trigger start test blocks and activate the use of other test block modes.
     */
    public void start() {
        // Make each start test block give a restone pulse.
        for (BlockPos blockPos : findTestBlocks(TestBlockMode.START)) {
            context.getBlockEntity(blockPos, TestBlockEntity.class).trigger();
        }

        // Make all other test blocks function for the remaining ticks of the test.
        context.forEachRemainingTick(() -> {
            handleTrigger(TestBlockMode.ACCEPT, testBlockEntity -> context.complete());
            handleTrigger(TestBlockMode.FAIL, testBlockEntity ->
                    context.throwGameTestException(Text.literal(testBlockEntity.getMessage())));
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
        context.forEachRelativePos(pos -> {
            BlockState blockState = context.getBlockState(pos);
            if (blockState.isOf(Blocks.TEST_BLOCK) && blockState.get(TestBlock.MODE) == mode) {
                list.add(pos.toImmutable());
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

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

    public void start() {
        context.getBlockEntity(findStartBlockPos(), TestBlockEntity.class).trigger();

        context.forEachRemainingTick(() -> {
            List<BlockPos> list = findTestBlocks(TestBlockMode.ACCEPT);

            if (list.stream().map(this::getTestBlockEntity).anyMatch(TestBlockEntity::hasTriggered)) {
                context.complete();
                return;
            }

            handleTrigger(TestBlockMode.FAIL, testBlockEntity ->
                    context.throwGameTestException(Text.literal(testBlockEntity.getMessage())));
            handleTrigger(TestBlockMode.LOG, TestBlockEntity::trigger);
        });
    }

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

    private TestBlockEntity getTestBlockEntity(BlockPos pos) {
        return context.getBlockEntity(pos, TestBlockEntity.class);
    }

    private void handleTrigger(TestBlockMode mode, Consumer<TestBlockEntity> callback) {
        for (BlockPos blockPos : findTestBlocks(mode)) {
            TestBlockEntity testBlockEntity = getTestBlockEntity(blockPos);
            if (testBlockEntity.hasTriggered()) {
                callback.accept(testBlockEntity);
                testBlockEntity.reset();
            }
        }
    }

    private BlockPos findStartBlockPos() {
        List<BlockPos> list = findTestBlocks(TestBlockMode.START);
        if (list.isEmpty()) {
            context.throwGameTestException(Text.translatable("test_block.error.missing", TestBlockMode.START.getName()));
        }

        if (list.size() != 1) {
            context.throwGameTestException(Text.translatable("test_block.error.too_many", TestBlockMode.START.getName()));
        }

        return list.getFirst();
    }
}

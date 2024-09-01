package net.pistonpoek.magical_scepter.item.scepter.spell;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.EvokerFangsEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.event.GameEvent;

public class EvokerFangsScepterSpell extends InstantScepterSpell {
    private final FangSpell fangSpell;

    public enum FangSpell {
        FANG_LINE,
        FANG_CIRCLE
    }

    protected EvokerFangsScepterSpell(int experienceCost, int castCooldown, FangSpell fangSpell) {
        super(experienceCost, castCooldown);
        this.fangSpell = fangSpell;
    }

    @Override
    public void castSpell(LivingEntity caster) {
        double minY = caster.getY() - 3.0;
        double maxY = caster.getY() + 3.0;
        float caster_rotation = (caster.getYaw() + 90.0f) * MathHelper.RADIANS_PER_DEGREE;
        switch (this.fangSpell) {
            case FANG_CIRCLE:
                float fang_rotation;
                for (int i = 0; i < 5; ++i) {
                    fang_rotation = caster_rotation + (float) i * (float) Math.PI * 2.0f / 5.0f;
                    conjureFangs(caster,
                            caster.getX() + (double) MathHelper.cos(fang_rotation) * 1.5,
                            caster.getZ() + (double) MathHelper.sin(fang_rotation) * 1.5,
                            minY, maxY, fang_rotation, 0);
                }
                for (int i = 0; i < 8; ++i) {
                    fang_rotation = caster_rotation + (float) i * (float) Math.PI * 2.0f / 8.0f + 1.2566371f;
                    conjureFangs(caster,
                            caster.getX() + (double) MathHelper.cos(fang_rotation) * 2.5,
                            caster.getZ() + (double) MathHelper.sin(fang_rotation) * 2.5,
                            minY, maxY, fang_rotation, 3);
                }
                break;
            case FANG_LINE:
                for (int i = 0; i < 16; ++i) {
                    double distance = 1.25 * (double) (i + 1);
                    conjureFangs(caster,
                            caster.getX() + (double) MathHelper.cos(caster_rotation) * distance,
                            caster.getZ() + (double) MathHelper.sin(caster_rotation) * distance,
                            minY, maxY, caster_rotation, i);
                }
                break;
        }
    }

    @Override
    public void displaySpell(LivingEntity caster, int remainingCastTicks) {

    }

    private void conjureFangs(LivingEntity caster, double x, double z, double minY, double maxY, float yaw, int warmup) {
        BlockPos blockPos = BlockPos.ofFloored(x, maxY, z);
        boolean bl = false;
        double d = 0.0;
        do {
            BlockState blockState2;
            VoxelShape voxelShape;
            BlockPos blockPos2 = blockPos.down();
            BlockState blockState = caster.getWorld().getBlockState(blockPos2);
            if (!blockState.isSideSolidFullSquare(caster.getWorld(), blockPos2, Direction.UP)) continue;
            if (!caster.getWorld().isAir(blockPos) && !(voxelShape = (blockState2 = caster.getWorld().getBlockState(blockPos)).getCollisionShape(caster.getWorld(), blockPos)).isEmpty()) {
                d = voxelShape.getMax(Direction.Axis.Y);
            }
            bl = true;
            break;
        } while ((blockPos = blockPos.down()).getY() >= MathHelper.floor(minY) - 1);
        if (bl) {
            caster.getWorld().spawnEntity(new EvokerFangsEntity(caster.getWorld(), x, (double)blockPos.getY() + d, z, yaw, warmup, caster));
            caster.getWorld().emitGameEvent(GameEvent.ENTITY_PLACE, new Vec3d(x, (double)blockPos.getY() + d, z), GameEvent.Emitter.of(caster));
        }
    }
}

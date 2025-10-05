package io.github.pistonpoek.magicalscepter.util;

import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationVector {
    /**
     * Get the rotation vector going into the specified rotation direction.
     *
     * @param rotation Pair of floats that specifies the vertical and horizontal direction.
     * @return Vector that expresses the direction of the specified rotation.
     */
    public static Vec3d get(Pair<Float, Float> rotation) {
        return get(rotation.getLeft(), rotation.getRight());
    }

    /**
     * Get the rotation vector going into the direction of the specified pitch and yaw.
     *
     * @param pitch Float that specifies vertical direction.
     * @param yaw   Float that specifies horizontal direction.
     * @return Vector that expresses the direction of the specified pitch and yaw.
     */
    public static Vec3d get(float pitch, float yaw) {
        float f = pitch * (float) (Math.PI / 180.0);
        float g = -yaw * (float) (Math.PI / 180.0);
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = MathHelper.cos(f);
        float k = MathHelper.sin(f);
        return new Vec3d(i * j, -k, h * j);
    }
}

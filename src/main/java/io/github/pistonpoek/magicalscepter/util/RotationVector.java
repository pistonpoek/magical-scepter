package io.github.pistonpoek.magicalscepter.util;

import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.phys.Vec3;

/**
 * Util class related to vector rotation.
 */
public class RotationVector {
    /**
     * Get the rotation vector going into the specified rotation direction.
     *
     * @param rotation Pair of floats that specifies the vertical and horizontal direction.
     * @return Vector that expresses the direction of the specified rotation.
     */
    public static Vec3 get(Tuple<Float, Float> rotation) {
        return get(rotation.getA(), rotation.getB());
    }

    /**
     * Get the rotation vector going into the direction of the specified pitch and yaw.
     *
     * @param pitch Float that specifies vertical direction.
     * @param yaw   Float that specifies horizontal direction.
     * @return Vector that expresses the direction of the specified pitch and yaw.
     */
    public static Vec3 get(float pitch, float yaw) {
        float f = pitch * (float) (Math.PI / 180.0);
        float g = -yaw * (float) (Math.PI / 180.0);
        float h = Mth.cos(g);
        float i = Mth.sin(g);
        float j = Mth.cos(f);
        float k = Mth.sin(f);
        return new Vec3(i * j, -k, h * j);
    }
}

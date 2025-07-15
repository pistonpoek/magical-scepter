package io.github.pistonpoek.magicalscepter.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class PlayerExperience {
    /**
     * Get the amount of experience points in the current level of the specified player.
     *
     * @param player Player to get the amount of experience points for.
     * @return Number of experience points in the current level the specified player has.
     */
    public static int getExperiencePoints(PlayerEntity player) {
        return MathHelper.floor(player.experienceProgress * (float) player.getNextLevelExperience());
    }

    /**
     * Get the total amount of experience points for the specified player.
     *
     * @param player Player to get the total amount of experience for.
     * @return Total number of experience points the specified player has.
     */
    public static int getTotalExperience(PlayerEntity player) {
        int level = player.experienceLevel;
        int levelPoints;
        if (level >= 30) {
            levelPoints = 1395 + 112 * (level - 30) + (level - 31) * (level - 30) * 9 / 2;
        } else if (level >= 15) {
            levelPoints = 315 + 37 * (level - 15) + (level - 16) * (level - 15) * 5 / 2;
        } else {
            levelPoints = 7 * level + (level - 1) * (level);
        }
        return levelPoints + getExperiencePoints(player);
    }

    /**
     * Add experience points to the specified player without modifying their score.
     *
     * @param player Player to add experience points to.
     */
    public static void addOnlyExperience(PlayerEntity player, int experience) {
        int amount = Math.max(-getTotalExperience(player), experience);
        player.addExperience(amount);
        player.addScore(-amount);
    }
}

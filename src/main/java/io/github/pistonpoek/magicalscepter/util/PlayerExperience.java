package io.github.pistonpoek.magicalscepter.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class PlayerExperience {
    public static int getExperiencePoints(PlayerEntity player) {
        return MathHelper.floor(player.experienceProgress * (float)player.getNextLevelExperience());
    }

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
}

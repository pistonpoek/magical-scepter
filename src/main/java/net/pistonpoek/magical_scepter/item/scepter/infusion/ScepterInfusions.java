package net.pistonpoek.magical_scepter.item.scepter.infusion;

import net.minecraft.entity.EntityType;

import java.util.Set;

public class ScepterInfusions {
    public static final ScepterInfusion INFUSABLE = new InfusableScepter();
    public static final ScepterInfusion BLAZE_INFUSION = new MobAttackScepterInfusion(EntityType.BLAZE, Set.of(EntityType.BLAZE, EntityType.SMALL_FIREBALL));
    public static final ScepterInfusion DRAGON_INFUSION = new MobAttackScepterInfusion(EntityType.ENDER_DRAGON, Set.of(EntityType.ENDER_DRAGON, EntityType.DRAGON_FIREBALL, EntityType.AREA_EFFECT_CLOUD));
    public static final ScepterInfusion GHAST_INFUSION = new MobAttackScepterInfusion(EntityType.GHAST, Set.of(EntityType.FIREBALL));

}


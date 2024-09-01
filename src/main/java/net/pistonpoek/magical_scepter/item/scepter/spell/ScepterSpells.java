package net.pistonpoek.magical_scepter.item.scepter.spell;

import net.minecraft.entity.effect.StatusEffects;
import net.pistonpoek.magical_scepter.effects.ModStatusEffects;

public class ScepterSpells {
    private static final int XP_COST = 5;
    private static final int SPELL_COOLDOWN = 100;

    public static final ScepterSpell BLANK = new BlankScepterSpell(XP_COST, SPELL_COOLDOWN); //0x006BF8 (old) Resistance color 9520880, see StatusEffects.java
    public static final ScepterSpell RESISTANCE = new EffectScepterSpell(XP_COST, SPELL_COOLDOWN, StatusEffects.RESISTANCE);
    public static final ScepterSpell BLAZE = new BlazeScepterSpell(XP_COST, SPELL_COOLDOWN); //0xe38c1b (old) Fire resistance 0xFF9900
    public static final ScepterSpell FIRE_RESISTANCE = new EffectScepterSpell(XP_COST, SPELL_COOLDOWN, StatusEffects.FIRE_RESISTANCE);
    // Breeze
    public static final ScepterSpell DRAGON_FIREBALL = new DragonFireballSpell(XP_COST, SPELL_COOLDOWN); //0xc21be3 (old) Average dragon particle color 0xB823F5, see DragonBreathParticle.java
    public static final ScepterSpell DRAGON_FLAMING = new DragonFlamingScepterSpell(XP_COST, SPELL_COOLDOWN); //0xc21be3 (old) Average dragon particle color 0xB823F5, see DragonBreathParticle.java
    public static final ScepterSpell EVOKER_RANGE = new EvokerFangsScepterSpell(XP_COST, SPELL_COOLDOWN, EvokerFangsScepterSpell.FangSpell.FANG_LINE);
    public static final ScepterSpell EVOKER_NEAR = new EvokerFangsScepterSpell(XP_COST, SPELL_COOLDOWN, EvokerFangsScepterSpell.FangSpell.FANG_CIRCLE);
    public static final ScepterSpell GHAST = new GhastScepterSpell(XP_COST, SPELL_COOLDOWN);
    public static final ScepterSpell REGENERATION = new EffectScepterSpell(XP_COST, SPELL_COOLDOWN, StatusEffects.REGENERATION);
    public static final ScepterSpell GUARDIAN_BEAM = new GhastScepterSpell(XP_COST, SPELL_COOLDOWN);
    //public static final ScepterSpell THORNS =
    public static final ScepterSpell SHULKER_BULLET = new ShulkerBulletScepterSpell(XP_COST, SPELL_COOLDOWN);
    public static final ScepterSpell SLOW_FALLING = new EffectScepterSpell(XP_COST, SPELL_COOLDOWN, StatusEffects.SLOW_FALLING);
    public static final ScepterSpell WARDEN = new WardenScepterSpell(XP_COST, SPELL_COOLDOWN);
    public static final ScepterSpell KNOCKBACK_RESISTANCE = new EffectScepterSpell(XP_COST, SPELL_COOLDOWN, ModStatusEffects.KNOCKBACK_RESISTANCE, 4);
    public static final ScepterSpell WITHER_SKULL = new WitherSkullScepterSpell(XP_COST, SPELL_COOLDOWN);
}

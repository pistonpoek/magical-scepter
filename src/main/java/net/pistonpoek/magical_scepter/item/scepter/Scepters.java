package net.pistonpoek.magical_scepter.item.scepter;

import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.pistonpoek.magical_scepter.item.scepter.infusions.ScepterInfusions;
import net.pistonpoek.magical_scepter.item.scepter.spell.ScepterSpells;
import net.pistonpoek.magical_scepter.util.ModIdentifier;

import static net.pistonpoek.magical_scepter.ModRegistries.SCEPTER;

public class Scepters {
    // 786C90 ? 0xAC576E? AB6697
    public static final RegistryEntry<Scepter> MAGICAL = Scepters.register("magical", new Scepter(0xBC7C5C, ScepterSpells.BLANK, ScepterSpells.RESISTANCE, ScepterInfusions.INFUSABLE)); //0x006BF8 (old) Resistance color 9520880, see StatusEffects.java
    public static final RegistryEntry<Scepter> BLAZE = Scepters.register("blaze", new Scepter(0xFF9900, ScepterSpells.BLAZE, ScepterSpells.FIRE_RESISTANCE, ScepterInfusions.BLAZE_INFUSION)); //0xe38c1b (old) Fire resistance 0xFF9900
    public static final RegistryEntry<Scepter> BREEZE = Scepters.register("breeze", new Scepter(0xBDC9FF, ScepterSpells.BLANK, ScepterSpells.BLANK, ScepterInfusions.INFUSABLE)); //0xe38c1b (old) Fire resistance 0xFF9900
    public static final RegistryEntry<Scepter> DRAGON = Scepters.register("dragon", new Scepter(0xB823F5, ScepterSpells.DRAGON_FIREBALL, ScepterSpells.DRAGON_FLAMING, ScepterInfusions.DRAGON_INFUSION)); //0xc21be3 (old) Average dragon particle color 0xB823F5
    public static final RegistryEntry<Scepter> EVOKER = Scepters.register("evoker", new Scepter(0x959B9B, ScepterSpells.EVOKER_RANGE, ScepterSpells.EVOKER_NEAR, ScepterInfusions.INFUSABLE)); // Evoker fang spell particle color 0x664d59
    public static final RegistryEntry<Scepter> GHAST = Scepters.register("ghast", new Scepter(0xCD5CAB, ScepterSpells.GHAST, ScepterSpells.REGENERATION, ScepterInfusions.GHAST_INFUSION)); // Regeneration potion effect color 0xCD5CAB
    public static final RegistryEntry<Scepter> GUARDIAN = Scepters.register("guardian", new Scepter(0x4f7d8c, ScepterSpells.GUARDIAN_BEAM, ScepterSpells.GUARDIAN_BEAM, ScepterInfusions.INFUSABLE)); // NIGHT_VISION spell? 698C82 is cyan color from regular guardian, C2FF66 is night vision color, 96C674 is in between.
    public static final RegistryEntry<Scepter> SHULKER = Scepters.register("shulker", new Scepter(0xCEFFFF, ScepterSpells.SHULKER_BULLET, ScepterSpells.SLOW_FALLING, ScepterInfusions.INFUSABLE)); // Levitation effect color is 0xCEFFFF
    public static final RegistryEntry<Scepter> WARDEN = Scepters.register("warden", new Scepter(0x2ce3eb, ScepterSpells.WARDEN, ScepterSpells.KNOCKBACK_RESISTANCE, ScepterInfusions.INFUSABLE));
    public static final RegistryEntry<Scepter> WITHER = Scepters.register("wither", new Scepter(0x736156, ScepterSpells.WITHER_SKULL, ScepterSpells.WITHER_SKULL, ScepterInfusions.INFUSABLE)); // Wither effect color is 0x736156

    private static RegistryEntry<Scepter> register(String name, Scepter scepter) {
        return Registry.registerReference(SCEPTER, ModIdentifier.of(name), scepter);
    }

    public static Identifier registerAndGetDefault() {
        return ModIdentifier.of("magical");
    }
}

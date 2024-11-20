package net.pistonpoek.magicalscepter.scepter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.pistonpoek.magicalscepter.spell.Spell;
import net.pistonpoek.magicalscepter.util.PlayerExperience;

import java.util.*;

public record Scepter(int color, int experienceCost, boolean infusable,
                      RegistryEntry<Spell> attackSpell,
                      RegistryEntry<Spell> protectSpell,
                      Optional<LootContextPredicate> infusion) {
    public static final Codec<Scepter> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.INT.fieldOf("color").forGetter(Scepter::color),
                            Codec.INT.fieldOf("experience_cost").forGetter(Scepter::experienceCost),
                            Codec.BOOL.optionalFieldOf("infusable", false).forGetter(Scepter::infusable),
                            Spell.ENTRY_CODEC.fieldOf("spell_attack").forGetter(Scepter::attackSpell),
                            Spell.ENTRY_CODEC.fieldOf("spell_protect").forGetter(Scepter::protectSpell),
                            LootContextPredicate.CODEC.optionalFieldOf("infusion").forGetter(Scepter::infusion)
                    ).apply(instance, Scepter::new)
    );
    public static final Codec<Scepter> NETWORK_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("color").forGetter(Scepter::color),
                    Codec.INT.fieldOf("experience_cost").forGetter(Scepter::experienceCost),
                    Codec.BOOL.optionalFieldOf("infusable", false).forGetter(Scepter::infusable),
                    Spell.ENTRY_CODEC.fieldOf("spell_attack").forGetter(Scepter::attackSpell),
                    Spell.ENTRY_CODEC.fieldOf("spell_protect").forGetter(Scepter::protectSpell)
            ).apply(instance, Scepter::createClientScepter)
    );
    private static Scepter createClientScepter(int color, int experienceCost, boolean infusable,
                                               RegistryEntry<Spell> attackSpell,
                                               RegistryEntry<Spell> protectSpell) {
        return new Scepter(color, experienceCost, infusable, attackSpell, protectSpell, Optional.empty());
    }
    public static final Codec<RegistryEntry<Scepter>> ENTRY_CODEC = RegistryFixedCodec.of(ModRegistryKeys.SCEPTER);
    public static final PacketCodec<RegistryByteBuf, RegistryEntry<Scepter>> ENTRY_PACKET_CODEC =
            PacketCodecs.registryEntry(ModRegistryKeys.SCEPTER);
    public static final PacketCodec<RegistryByteBuf, Scepter> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);

    public int getColor() {
        return color;
    }

    public boolean isInfusable() {
        return infusable;
    }

    public int getExperienceCost() {
        return experienceCost;
    }

    public RegistryEntry<Spell> getAttackSpell() {
        return attackSpell;
    }

    public RegistryEntry<Spell> getProtectSpell() {
        return protectSpell;
    }

    public boolean infuses(LootContext lootContext) {
        if (infusion.isEmpty()) return false;

        LootContextPredicate lootContextPredicate = infusion.get();

        return lootContextPredicate.test(lootContext);
    }

    public static Scepter.Builder builder(int color, int experienceCost, boolean infusable,
                                          RegistryEntry<Spell> attackSpell, RegistryEntry<Spell> protectSpell) {
        return new Scepter.Builder(color, experienceCost, infusable, attackSpell, protectSpell);
    }

    public static class Builder {
        private final int color;
        private final boolean infusable;
        private final int experienceCost;
        private final RegistryEntry<Spell> attackSpell;
        private final RegistryEntry<Spell> protectSpell;
        private LootContextPredicate infusion = null;

        public Builder(int color, int experienceCost, boolean infusable,
                       RegistryEntry<Spell> attackSpell, RegistryEntry<Spell> protectSpell) {
            this.color = color;
            this.experienceCost = experienceCost;
            this.infusable = infusable;
            this.attackSpell = attackSpell;
            this.protectSpell = protectSpell;
        }

        public Scepter.Builder infusion(LootContextPredicate infusion) {
            this.infusion = infusion;
            return this;
        }

        public Scepter build() {
            return new Scepter(color, experienceCost, infusable,
                    attackSpell, protectSpell, Optional.ofNullable(infusion));
        }
    }

}

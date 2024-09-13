package net.pistonpoek.magicalscepter.scepter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.loot.context.LootContext;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.pistonpoek.magicalscepter.spell.Spell;

import java.util.Optional;

public record Scepter(int color, boolean infusable, Spell primarySpell, Optional<Spell> secondarySpell,
                      Optional<LootContextPredicate> infusion) {
    public static final Codec<Scepter> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.INT.fieldOf("color").forGetter(Scepter::color),
                            Codec.BOOL.fieldOf("infusable").forGetter(Scepter::infusable),
                            Spell.CODEC.fieldOf("primary_spell").forGetter(Scepter::primarySpell),
                            Spell.CODEC.optionalFieldOf("secondary_spell").forGetter(Scepter::secondarySpell),
                            LootContextPredicate.CODEC.optionalFieldOf("infusion").forGetter(Scepter::infusion)
                    )
                    .apply(instance, Scepter::new)
    );
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

    public boolean infuses(LootContext lootContext) {
        if (infusion.isEmpty()) return false;

        LootContextPredicate lootContextPredicate = infusion.get();

        return lootContextPredicate.test(lootContext);
    }

    public Spell getPrimarySpell() {
        return primarySpell;
    }

    public Spell getSecondarySpell() {
        return secondarySpell.orElse(primarySpell);
    }

}

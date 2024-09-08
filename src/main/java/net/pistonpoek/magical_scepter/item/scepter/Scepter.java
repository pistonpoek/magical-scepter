package net.pistonpoek.magical_scepter.item.scepter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.loot.context.LootContext;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.pistonpoek.magical_scepter.ModRegistryKeys;
import net.pistonpoek.magical_scepter.util.ModIdentifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record Scepter(String id, int color, boolean infusable,
                      Optional<LootContextPredicate> infusion,
                      Optional<RegistryEntry<SoundEvent>> sound) {
    public static final Scepter DEFAULT = new Scepter("empty", 0, false, Optional.empty(), Optional.empty());
    public static final Codec<Scepter> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.STRING.fieldOf("id").forGetter(Scepter::id),
                            Codec.INT.fieldOf("color").forGetter(Scepter::color),
                            Codec.BOOL.fieldOf("infusable").forGetter(Scepter::infusable),
                            LootContextPredicate.CODEC.optionalFieldOf("infusion").forGetter(Scepter::infusion),
                            SoundEvent.ENTRY_CODEC.optionalFieldOf("sound").forGetter(Scepter::sound)
                    )
                    .apply(instance, Scepter::new)
    );
    public static final Codec<RegistryEntry<Scepter>> ENTRY_CODEC = RegistryFixedCodec.of(ModRegistryKeys.SCEPTER);
    public static final PacketCodec<RegistryByteBuf, RegistryEntry<Scepter>> ENTRY_PACKET_CODEC =
            PacketCodecs.registryEntry(ModRegistryKeys.SCEPTER);

    public String getId() {
        return id;
    }

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

    public Spell getSpell() {
        return new Spell(30, 100);
    }
}

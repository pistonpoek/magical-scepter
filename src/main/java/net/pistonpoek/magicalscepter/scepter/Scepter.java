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
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.pistonpoek.magicalscepter.spell.Spell;

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
    public static final PacketCodec<RegistryByteBuf, Scepter> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);

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
//
//    public record Entry(Identifier id, Scepter scepter) {
//        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(
//                instance -> instance.group(
//                                Identifier.CODEC.fieldOf("id").forGetter(Entry::id),
//                                Scepter.CODEC.fieldOf("scepter").forGetter(Entry::scepter)
//                        )
//                        .apply(instance, Entry::new));
//        public static final PacketCodec<RegistryByteBuf, Entry> PACKET_CODEC =
//                PacketCodec.tuple(Identifier.PACKET_CODEC, Entry::id, Scepter.PACKET_CODEC, Entry::scepter, Entry::new);
//
//        public Entry(Identifier id, Scepter scepter) {
//            this.id = id;
//            this.scepter = scepter;
//        }
//
//        public boolean equals(Object object) {
//            if (this == object) {
//                return true;
//            } else {
//                if (object instanceof Entry entry) {
//                    return this.id.equals(entry.id);
//                }
//                return false;
//            }
//        }
//
//        public int hashCode() {
//            return this.id.hashCode();
//        }
//
//        public String toString() {
//            return this.id.toString();
//        }
//
//        public Identifier id() {
//            return this.id;
//        }
//
//        public Scepter value() {
//            return this.scepter;
//        }
//    }
}

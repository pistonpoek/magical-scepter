package net.pistonpoek.magical_scepter.item.scepter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.loot.context.LootContext;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.sound.SoundEvent;
import net.pistonpoek.magical_scepter.ModRegistryKeys;

import java.util.Optional;

public record Scepter(Scepter.Definition definition) {
    public static final Scepter DEFAULT = new Scepter(new Definition("empty", 0, false, Optional.empty(), Optional.empty()));
    public static final Codec<Scepter> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Scepter.Definition.CODEC.forGetter(Scepter::definition)
                    )
                    .apply(instance, Scepter::new)
    );
    public static final Codec<RegistryEntry<Scepter>> ENTRY_CODEC = RegistryFixedCodec.of(ModRegistryKeys.SCEPTER);
    public static final PacketCodec<RegistryByteBuf, RegistryEntry<Scepter>> ENTRY_PACKET_CODEC =
            PacketCodecs.registryEntry(ModRegistryKeys.SCEPTER);

    public String getId() {
        return definition.id;
    }

    public int getColor() {
        return definition.color;
    }

    public boolean isInfusable() {
        return definition.infusable;
    }

    public boolean infuses(LootContext lootContext) {
        if (definition.infusion.isEmpty()) return false;

        LootContextPredicate lootContextPredicate = definition.infusion.get();

        return lootContextPredicate.test(lootContext);
    }

    public Spell getSpell() {
        return new Spell(30, 100);
    }

    public static Scepter.Builder builder(Scepter.Definition definition) {
        return new Scepter.Builder(definition);
    }

    public static class Builder {
        private final Scepter.Definition definition;

        public Builder(Scepter.Definition properties) {
            this.definition = properties;
        }

        public Scepter build() {
            return new Scepter(this.definition);
        }
    }

    public record Definition(
            String id,
            int color,
            boolean infusable,
            Optional<LootContextPredicate> infusion,
            Optional<RegistryEntry<SoundEvent>> sound
    ) {
        public static final MapCodec<Scepter.Definition> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.STRING.fieldOf("id").forGetter(Scepter.Definition::id),
                    Codec.INT.fieldOf("color").forGetter(Scepter.Definition::color),
                    Codec.BOOL.fieldOf("infusable").forGetter(Scepter.Definition::infusable),
                    LootContextPredicate.CODEC.optionalFieldOf("infusion").forGetter(Scepter.Definition::infusion),
                    SoundEvent.ENTRY_CODEC.optionalFieldOf("sound").forGetter(Scepter.Definition::sound)
                )
                .apply(instance, Scepter.Definition::new)
        );
    }

}

package io.github.pistonpoek.magicalscepter.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import io.github.pistonpoek.magicalscepter.advancement.criteria.ModCriteria;
import io.github.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import io.github.pistonpoek.magicalscepter.spell.cast.SpellCast;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record Spell(List<SpellCast> casts, int cooldown, Text description) {
    public static final Codec<Spell> BASE_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            SpellCast.CODEC.listOf().fieldOf("casts").forGetter(Spell::casts),
                            Codec.INT.fieldOf("cooldown").forGetter(Spell::cooldown),
                            TextCodecs.CODEC.fieldOf("description").forGetter(Spell::description)
                    )
                    .apply(instance, Spell::new)
    );
    public static final Codec<Spell> NETWORK_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("cooldown").forGetter(Spell::cooldown),
                    TextCodecs.CODEC.fieldOf("description").forGetter(Spell::description)
            ).apply(instance, Spell::createClientSpell)
    );
    private static Spell createClientSpell(int cooldown, Text description) {
        return new Spell(List.of(), cooldown, description);
    }
    public static final Codec<RegistryEntry<Spell>> ENTRY_CODEC = RegistryFixedCodec.of(ModRegistryKeys.SPELL);
    public static final Codec<Spell> CODEC = Codec.withAlternative(BASE_CODEC, ENTRY_CODEC, RegistryEntry::value);
    public static final PacketCodec<RegistryByteBuf, RegistryEntry<Spell>> ENTRY_PACKET_CODEC =
            PacketCodecs.registryEntry(ModRegistryKeys.SPELL);
    public static final PacketCodec<RegistryByteBuf, Spell> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);

    /**
     * Cast this spell for a specific living entity.
     *
     * @param caster Living entity to cast the spell for.
     * @return Duration that the spell takes.
     */
    public int castSpell(@NotNull LivingEntity caster) {
        if (caster.getWorld().isClient()) {
            return 0;
        }

        int duration = 0;
        for (SpellCast cast : casts) {
            int castTime = cast.apply(caster);
            duration = Math.max(duration, castTime);
        }

        return duration;
    }

    public int getCooldown() {
        return cooldown;
    }

    public String toString() {
        return "Spell " + this.description.getString();
    }

    public static MutableText getName(RegistryEntry<Spell> spell) {
        return spell.value().description.copy();
    }

    public static Spell.Builder builder(int cooldown, Text description) {
        return new Spell.Builder(cooldown, description);
    }

    public static class Builder {
        private final int cooldown;
        private final Text description;
        private final List<SpellCast> casts = new ArrayList<>();

        public Builder(int cooldown, Text description) {
            this.cooldown = cooldown;
            this.description = description;
        }

        public Spell.Builder addCast(SpellCast.Builder cast) {
            casts.add(cast.build());
            return this;
        }

        public Spell build() {
            return new Spell(casts, cooldown, description);
        }
    }
}

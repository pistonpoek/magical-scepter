package net.pistonpoek.magicalscepter.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.StringIdentifiable;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.pistonpoek.magicalscepter.scepter.Scepter;
import net.pistonpoek.magicalscepter.spell.effect.SpellEffect;

import java.util.ArrayList;
import java.util.List;

public record Spell(int experienceCost, int cooldown,
                    List<SpellEffect> effects) {
    public static final Codec<Spell> BASE_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.INT.fieldOf("experience_cost").forGetter(Spell::experienceCost),
                            Codec.INT.fieldOf("cooldown").forGetter(Spell::cooldown),
                            SpellEffect.CODEC.listOf().fieldOf("effects").forGetter(Spell::effects)
                    )
                    .apply(instance, Spell::new)
    );
    public static final Codec<RegistryEntry<Spell>> ENTRY_CODEC = RegistryFixedCodec.of(ModRegistryKeys.SPELL);
    public static final Codec<Spell> CODEC = Codec.withAlternative(BASE_CODEC, ENTRY_CODEC, RegistryEntry::value);
    public static final PacketCodec<RegistryByteBuf, RegistryEntry<Spell>> ENTRY_PACKET_CODEC =
            PacketCodecs.registryEntry(ModRegistryKeys.SPELL);
    public static final PacketCodec<RegistryByteBuf, Spell> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);

    public void castSpell(LivingEntity caster) {
        ServerWorld serverWorld = (ServerWorld)caster.getWorld();
        for (SpellEffect spellEffect : effects) {
            spellEffect.apply(serverWorld, caster, caster.getPos());
        }
    }

    public static Spell.Builder builder(int experienceCost, int cooldown) {
        return new Spell.Builder(experienceCost, cooldown);
    }

    // See SpawnParticlesEnchantmentEffect
    public enum Target implements StringIdentifiable {
        CASTER("caster"),
        ENTITY("entity"),
        POSITION("position");

        private final String name;

        Target(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return name;
        }
    }

    public static class Builder {
        private final int experienceCost;
        private final int cooldown;
        private final List<SpellEffect> effects = new ArrayList<>();

        public Builder(int experienceCost, int cooldown) {
            this.experienceCost = experienceCost;
            this.cooldown = cooldown;
        }

        public Spell.Builder addEffect(SpellEffect effect) {
            effects.add(effect);
            return this;
        }

        public Spell build() {
            return new Spell(experienceCost, cooldown, effects);
        }
    }
}

package net.pistonpoek.magicalscepter.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.timer.Timer;
import net.pistonpoek.magicalscepter.registry.ModRegistryKeys;
import net.pistonpoek.magicalscepter.spell.cast.SpellCastTimerCallback;
import net.pistonpoek.magicalscepter.spell.effect.SpellEffect;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record Spell(List<Cast> casts, int cooldown, int experienceCost, Text description) {
    public static final Codec<Spell> BASE_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Cast.CODEC.listOf().fieldOf("casts").forGetter(Spell::casts),
                            Codec.INT.fieldOf("cooldown").forGetter(Spell::cooldown),
                            Codec.INT.fieldOf("experience_cost").forGetter(Spell::experienceCost),
                            TextCodecs.CODEC.fieldOf("description").forGetter(Spell::description)
                    )
                    .apply(instance, Spell::new)
    );
    public static final Codec<Spell> NETWORK_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("cooldown").forGetter(Spell::cooldown),
                    Codec.INT.fieldOf("experience_cost").forGetter(Spell::experienceCost),
                    TextCodecs.CODEC.fieldOf("description").forGetter(Spell::description)
            ).apply(instance, Spell::createClientSpell)
    );
    private static Spell createClientSpell(int cooldown, int experienceCost, Text description) {
        return new Spell(List.of(), cooldown, experienceCost, description);
    }
    public static final Codec<RegistryEntry<Spell>> ENTRY_CODEC = RegistryFixedCodec.of(ModRegistryKeys.SPELL);
    public static final Codec<Spell> CODEC = Codec.withAlternative(BASE_CODEC, ENTRY_CODEC, RegistryEntry::value);
    public static final PacketCodec<RegistryByteBuf, RegistryEntry<Spell>> ENTRY_PACKET_CODEC =
            PacketCodecs.registryEntry(ModRegistryKeys.SPELL);
    public static final PacketCodec<RegistryByteBuf, Spell> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);

    public void castSpell(@NotNull LivingEntity caster) {
        if (caster.getWorld().isClient()) {
            return;
        }

        for (Cast cast : casts) {
            cast.schedule(caster);
        }
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getExperienceCost() {
        return experienceCost;
    }

    public int getDuration() {
        int duration = 0;
        for (Cast cast : casts) {
            duration = Math.max(duration, cast.delay);
        }
        return duration;
    }

    public String toString() {
        return "Spell " + this.description.getString();
    }

    public static Text getName(RegistryEntry<Spell> spell) {
        MutableText mutableText = spell.value().description.copy();
        Texts.setStyleIfAbsent(mutableText, Style.EMPTY.withColor(Formatting.LIGHT_PURPLE));
        return mutableText;
    }

    public record Cast(int delay, List<SpellEffect> effects) {
        public static final Codec<Spell.Cast> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                Codec.INT.optionalFieldOf("delay", 0).forGetter(Cast::delay),
                                SpellEffect.CODEC.listOf().fieldOf("effects").forGetter(Cast::effects)
                        )
                        .apply(instance, Cast::new)
        );

        public void apply(LivingEntity caster) {
            apply(effects, caster);
        }

        public static void apply(List<SpellEffect> effects, LivingEntity caster) {
            ServerWorld serverWorld = (ServerWorld)caster.getWorld();
            for (SpellEffect spellEffect: effects) {
                spellEffect.apply(serverWorld, caster, caster.getPos());
            }
        }

        public void schedule(@NotNull LivingEntity caster) {
            MinecraftServer minecraftServer = caster.getServer();
            if (minecraftServer == null) {
                return;
            }
            if (delay <= 0) {
                apply(caster);
                return;
            }
            Timer<MinecraftServer> timer = minecraftServer.getSaveProperties().getMainWorldProperties().getScheduledEvents();
            long cast_time = caster.getWorld().getTime() + (long)delay;
            timer.setEvent(caster.getUuid().toString(), cast_time, new SpellCastTimerCallback(effects, caster.getUuid()));
        }

        public static void clear(@NotNull LivingEntity caster) {
            MinecraftServer minecraftServer = caster.getServer();
            if (minecraftServer == null) {
                return;
            }
            Timer<MinecraftServer> timer = minecraftServer.getSaveProperties().getMainWorldProperties().getScheduledEvents();
            timer.remove(caster.getUuid().toString());
        }

        public static void afterDeath(LivingEntity entity, DamageSource damageSource) {
            if (entity == null) {
                return;
            }
            clear(entity);
        }

        public static Spell.Cast.Builder builder() {
            return new Spell.Cast.Builder();
        }

        public static class Builder {
            private int delay = 0;
            private final List<SpellEffect> effects = new ArrayList<>();

            public Spell.Cast.Builder delay(int delay) {
                this.delay = delay;
                return this;
            }

            public Spell.Cast.Builder addEffect(SpellEffect effect) {
                effects.add(effect);
                return this;
            }

            public Spell.Cast build() {
                return new Spell.Cast(delay, effects);
            }
        }
    }

    // See SpawnParticlesEnchantmentEffect
    public enum Target implements StringIdentifiable {
        CASTER("caster"),
        ENTITY("entity"),
        POSITION("position");

        private final String description;

        Target(String description) {
            this.description = description;
        }

        @Override
        public String asString() {
            return description;
        }
    }

    public static Spell.Builder builder(int cooldown, int experienceCost, Text description) {
        return new Spell.Builder(cooldown, experienceCost, description);
    }

    public static class Builder {
        private final int cooldown;
        private final int experienceCost;
        private final Text description;
        private final List<Cast> casts = new ArrayList<>();

        public Builder(int cooldown, int experienceCost, Text description) {
            this.cooldown = cooldown;
            this.experienceCost = experienceCost;
            this.description = description;
        }

        public Spell.Builder addCast(Cast cast) {
            casts.add(cast);
            return this;
        }

        public Spell build() {
            return new Spell(casts, cooldown, experienceCost, description);
        }
    }
}
